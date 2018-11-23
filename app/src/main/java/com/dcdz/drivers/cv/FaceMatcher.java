package com.dcdz.drivers.cv;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import static libsvm.svm.svm_load_model;
import static libsvm.svm.svm_predict_probability;
import static libsvm.svm.svm_save_model;
import static libsvm.svm.svm_train;
import static libsvm.svm_parameter.ONE_CLASS;
import static libsvm.svm_parameter.RBF;

public class FaceMatcher {
    final static String TAG = "FaceMatcher";

    final String model_file = "%s/%s.svm";
    final int INVALID_USER = -1;

    public static class Face {
        float[] features;
        int id;
    }

    public FaceMatcher(String directory) {
        load(directory);
    }

    Map<Integer, svm_model> models = new HashMap<Integer, svm_model>();
    public void load(String directory) {
        // iterate the models name under the directory
        File dir = new File(directory);
        File[] files = dir.listFiles();

        for(File file : files) {
            String name = file.getName();
            String temp[] = name.split(".");
            if(temp[1] != "svm") {
                continue;
            }
            int id = Integer.valueOf(temp[0]);
            try {
                svm_model model;
                model = svm_load_model(String.format(model_file, directory, id));
                models.put(id, model);
            } catch (IOException e) {
                Log.e(TAG, String.format("load model %s fail", id));
            }
        }
    }

    public void save(String directory) {
        for (final int id : models.keySet()){
            try {
                String filename = String.format(model_file, directory, id);
                svm_save_model(filename, models.get(id));
            } catch (IOException e) {
                Log.e(TAG, String.format("save model %s fail", id));
            }
        }
    }

    public void train(final Vector<Face> faces) {
        for (Face face : faces) {
            if(!models.containsKey(face.id)) {
                train(faces, face.id);
            }
        }
    }

    public void train(final Vector<Face> faces, int id) {
        svm_node[][] nodes = new svm_node[faces.size()][];
        for(int j = 0; j < faces.size(); j++) {
            float[] features = faces.get(j).features;
            svm_node[] point = new svm_node[features.length];
            for(int i = 0; i < features.length; i++) {
                point[i] = new svm_node();
                point[i].index = i + 1;
                point[i].value = features[i];
            }
            nodes[j] = point;
        }

        double[] truthes = new double[faces.size()];
        for(int j = 0; j < faces.size(); j++) {
            truthes[j] = faces.get(j).id == id? 1: 0;
        }

        svm_parameter parameter = new svm_parameter();
        parameter.svm_type = ONE_CLASS;
        parameter.kernel_type = RBF;
        parameter.gamma = 0.802;
        parameter.nu = 0.16;
        parameter.cache_size = 100;

        svm_problem problem = new svm_problem();
        problem.x = nodes;
        problem.l = nodes.length;
        problem.y = truthes;

        svm_model model = svm_train(problem, parameter);
        models.put(id, model);
    }

    public int predict(final float[] features) {
        svm_node nodes[] = new svm_node[features.length];
        double probs[] = new double[models.size()];
        int argmax_id = INVALID_USER;
        double max_proba = -Double.MAX_VALUE; // act as threshold

        for(final int id : models.keySet()) {
            svm_predict_probability(models.get(id), nodes, probs);
            if(probs[0] > max_proba) {
                argmax_id = id;
            }
        }

        return argmax_id;
    }
}
