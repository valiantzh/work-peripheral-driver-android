package com.dcdz.drivers.demo.fragment.image;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcdz.drivers.BuildConfig;
import com.dcdz.drivers.R;
import com.dcdz.drivers.cv.Box;
import com.dcdz.drivers.cv.FaceDetector;
import com.dcdz.drivers.cv.FaceEmbedded;
import com.dcdz.drivers.cv.FaceMatcher;
import com.dcdz.drivers.cv.TrackingOverlay;
import com.dcdz.drivers.utils.BaseFragment;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Frame;
import com.otaliastudios.cameraview.FrameProcessor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;

/**
 * A simple {@link Fragment} subclass.
 * 人脸检测
 */
public class FaceRecognitionFragment extends BaseFragment implements FrameProcessor{
    final static String TAG = "FaceRecognitionFragment";
    final String DATA_DIR = new File(Environment.getExternalStorageDirectory(), BuildConfig.APPLICATION_ID).toString();
    final String MODELS_DIR = new File(DATA_DIR, "models").toString();

    final boolean scaled = false;
    final int scaledHeight = 0;
    final int scaledWidth = 0;
    final int minfacesize = 300;
    final int rotation = 270; // should be one of values {0, 90, 180, 270}

    Unbinder unbinder;
    @BindView(R.id.camera)
    CameraView cameraView;
    @BindView(R.id.tracking_overlay)
    TrackingOverlay overlay;

    FaceDetector detector;
    FaceEmbedded extractor;
    FaceMatcher matcher;
    boolean isProcessing;

    private Handler handler;
    private HandlerThread handlerThread;

    public FaceRecognitionFragment() {
    }

    public static FaceRecognitionFragment newInstance() {
        FaceRecognitionFragment fragment = new FaceRecognitionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }

    private boolean createDirectoryIfNotExist(String path) {
        File dir = new File(path);
        boolean success = true;
        if(!dir.exists()) {
            success = dir.mkdir();
        }

        return success;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_face_recognition, container, false);
        unbinder = ButterKnife.bind(this, view);

        cameraView = view.findViewById(R.id.camera);
        cameraView.start();
        createDirectoryIfNotExist(DATA_DIR);
        createDirectoryIfNotExist(MODELS_DIR);
        detector = new FaceDetector(getActivity().getAssets());
        extractor = new FaceEmbedded(getActivity().getAssets());
        matcher = new FaceMatcher(MODELS_DIR);

        cameraView.addFrameProcessor(this);

        handlerThread = new HandlerThread("background");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        return view;
    }

    @Override
    public void onDestroyView() {
        detector = null;
        cameraView.removeFrameProcessor(this);
        cameraView.stop();

        handlerThread.quit();
        try {
            handlerThread.join();
            handlerThread = null;
            handler = null;
        } catch (final InterruptedException e) {

        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected synchronized  void runInBackground(final Runnable r) {
        if (handler != null) {
            handler.post(r);
        }
    }

    @Override
    public void process(@NonNull Frame frame) {
        if(isProcessing) {
            Log.i(TAG, "face detector is still running, skip this frame");
            return;
        }
        isProcessing = true;
        final int width = frame.getSize().getWidth();
        final int height = frame.getSize().getHeight();
        Log.i(TAG, String.format("frame width: %d, height: %d", width, height));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuv = new YuvImage(frame.getData(), frame.getFormat(), frame.getSize().getWidth(), frame.getSize().getHeight(), null);
        yuv.compressToJpeg(new Rect(0, 0, width, height), 100, out);
        byte[] imageBytes = out.toByteArray();
        Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        Bitmap scaledImage;
        if(scaled) {
            float factor;
            if(scaledWidth != 0 || scaledHeight != 0) {
                if(scaledWidth == 0) {
                    factor = height / scaledHeight;
                } else if (scaledHeight == 0) {
                    factor = width / scaledWidth;
                } else {
                    factor = min(width / scaledWidth, height / scaledHeight);
                }
                scaledImage = Bitmap.createScaledBitmap(image, round(width / factor), round(height / factor), false);
                image.recycle();
            }
        }
        Bitmap temp = scaled? scaledImage: image;
        Matrix m = new Matrix();
        m.postRotate(rotation);
        //m.postScale(-1, 1, temp.getWidth() / 2, temp.getHeight() / 2);
        final Bitmap input = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), m, false);
        temp.recycle();
        runInBackground(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run inference");
                if(detector != null) {
                    Vector<Box> results = detector.detectFaces(input, minfacesize);

                    Log.i(TAG, String.format("find %d face(s)", results.size()));
                    overlay.setCameraSize(width, height);
                    overlay.setTrackingResults(results, rotation);
                    overlay.postInvalidate();

                    final Vector<Bitmap> avatars = new Vector<>();
                    for(Box box : results) {
                        Log.i(TAG, String.format("Width: %d, Height: %d, X: %d, Y: %d", box.width(), box.height(), box.left(), box.top()));
                        int left = max(box.left(), 0);
                        int top = max(box.top(), 0);
                        int width = min(input.getWidth() - box.left(), box.width());
                        int height = min(input.getHeight() - box.top(), box.height());
                        Bitmap temp = Bitmap.createBitmap(input, left, top, width, height);
                        avatars.add(Bitmap.createScaledBitmap(temp, 200, 200, false));
                        temp.recycle();
                    }
                    input.recycle();

                    float[] features;
                    for(Bitmap avatar : avatars) {
                        features = extractor.extract(avatar);
                        Log.e(TAG, "features: " + Arrays.toString(features));
                        int id = matcher.predict(features);
                        Log.e(TAG, String.format("match user id %d", id));
                    }


                    //                    for(int i = 0; i < avatars.size(); i++) {
                    //                        String filename = Environment.getExternalStorageDirectory() + "/" + i + ".png";
                    //                        try (FileOutputStream out = new FileOutputStream(filename)) {
                    //                            avatars.get(i).compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    //                            // PNG is a lossless format, the compression factor (100) is ignored
                    //                        } catch (IOException e) {
                    //                            e.printStackTrace();
                    //                        }
                    //                    }

                }
                isProcessing = false;
            }
        });
    }
}
