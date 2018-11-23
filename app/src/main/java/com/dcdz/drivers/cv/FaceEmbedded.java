package com.dcdz.drivers.cv;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class FaceEmbedded {
    private final static String TAG = "FaceEmbedded";
    // model
    private final String MODEL_FILE = "file:///android_asset/facenet.pb";
    // tensor name
    private final String FaceNetInName = "input:0";
    private final String[] FaceNetOutName = {"embeddings:0"};
    // size
    private final int InputImageSize = 160;
    private final int OutputTensorSize = 128;

    private TensorFlowInferenceInterface inferenceInterface;
    public FaceEmbedded(AssetManager assetManager) {
        loadModel(assetManager);
    }

    private boolean loadModel(AssetManager assetManager) {
        try {
            inferenceInterface = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
            Log.d(TAG, "model load success");
        } catch (Exception e){
            Log.d(TAG, "model load failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private float[] normalizeImage(Bitmap bitmap){
        int w=bitmap.getWidth();
        int h=bitmap.getHeight();
        float[] floatValues=new float[w*h*3];
        int[]   intValues=new int[w*h];
        bitmap.getPixels(intValues,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
        float imageMean=127.5f;
        float imageStd=128;

        for (int i=0;i<intValues.length;i++){
            final int val=intValues[i];
            floatValues[i * 3 + 0] = (((val >> 16) & 0xFF) - imageMean) / imageStd;
            floatValues[i * 3 + 1] = (((val >> 8) & 0xFF) - imageMean) / imageStd;
            floatValues[i * 3 + 2] = ((val & 0xFF) - imageMean) / imageStd;
        }
        return floatValues;
    }

    public float[] extract(final Bitmap avatar) {
        float[] inputTensor = new float[1 * InputImageSize * InputImageSize * 3];
        Bitmap input = Bitmap.createScaledBitmap(avatar, InputImageSize, InputImageSize, false);

        inputTensor = normalizeImage(input);
        input.recycle();

        inferenceInterface.feed(FaceNetInName, inputTensor, 1, InputImageSize, InputImageSize, 3);
        boolean[] is_training = new boolean[1];
        is_training[0] = false;
        inferenceInterface.feed("phase_train", is_training);
        inferenceInterface.run(FaceNetOutName, false);

        float[] features = new float[OutputTensorSize];
        inferenceInterface.fetch(FaceNetOutName[0], features);
        return features;
    }
}
