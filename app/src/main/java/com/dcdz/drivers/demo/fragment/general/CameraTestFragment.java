package com.dcdz.drivers.demo.fragment.general;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.BaseFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LJW on 2018/3/2.
 * 相机测试
 */
public class CameraTestFragment extends BaseFragment {

    @BindView(R.id.button_capture)
    Button buttonCapture;
    Unbinder unbinder;
    @BindView(R.id.camera_preview)
    FrameLayout cameraPreview;
    @BindView(R.id.open_camera)
    Button openCamera;
    @BindView(R.id.button_record)
    Button buttonRecord;
    @BindView(R.id.button_stop_record)
    Button buttonStopRecord;
    @BindView(R.id.open_folder)
    Button buttonFolder;


    private Camera mCamera;
    private CameraPreview mPreview;
    private View mCameraView;
    private MediaRecorder mediaRecorder;

    private int rotate = 0;

    public CameraTestFragment() {
        // Required empty public constructor
    }

    public static CameraTestFragment newInstance() {
        Bundle args = new Bundle();
        CameraTestFragment fragment = new CameraTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseCameraAndPreview();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseCameraAndPreview();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera_test, container, false);
        unbinder = ButterKnife.bind(this,view);

        // Create our Preview view and set it as the content of our activity.
        if(Build.VERSION.SDK_INT > 23) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 30);
            }
        }
        mCameraView = view;
        return view;
    }

    /**
     * onClick method for take photo button.
     */
    @OnClick(R.id.button_capture)
    public void onCaptureClick() {
        // get an image from the camera
        Log.d("CameraTest", "Capture Button Clicked!");
        if(mCamera!=null){
            mCamera.takePicture(null, null, mPicture);
        } else{
            Toast.makeText(getActivity(),"Open Camera First",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * onClick method for take open camera button.
     */
    @OnClick(R.id.open_camera)
    public void onOpenClick() {
        Log.d("CameraTest", "Open Button Clicked!");
        boolean opened = false;
        if(opened){
            Log.d("CameraTest","Camera opened");
        } else {
            opened = safeCameraOpenInView(mCameraView);
            if(opened){
                buttonCapture.setEnabled(true);
                buttonFolder.setEnabled(true);
                buttonRecord.setEnabled(true);
                buttonStopRecord.setEnabled(true);
            }
        }
    }

    /**
     * onClick method for record button.
     */
    @OnClick(R.id.button_record)
    public void onRecordClick(){
        Log.d("CameraTest","Record Button Clicked!");
        if(mediaRecorder==null) {
            mPreview.startRecord();
            buttonRecord.setTextColor(Color.RED);
        }
    }

    /**
     * onClick method for stop record button.
     */
    @OnClick(R.id.button_stop_record)
    public void onStopClick(){
        Log.d("CameraTest","Stop Button Clicked!");
        if(mediaRecorder!=null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mCamera.lock();
            safeCameraOpenInView(mCameraView);
            buttonRecord.setTextColor(buttonCapture.getTextColors().getDefaultColor());
        }
    }

    @OnClick(R.id.open_folder)
    public void onFolderClick(){
        openFolder();
    }

    @OnClick(R.id.rotate)
    public void onRotateClick(){
        if (rotate == 270) {
            rotate = 0;
        } else {
            rotate += 90;
        }
        releaseCameraAndPreview();
        safeCameraOpenInView(mCameraView);
    }


    /**
     * Recommended "safe" way to open the camera.
     */
    private boolean safeCameraOpenInView(View view) {
        boolean qOpened = false;
        releaseCameraAndPreview();
        mCamera = getCameraInstance();
        qOpened = (mCamera != null);

        if(qOpened){
            mPreview = new CameraPreview(getActivity().getBaseContext(), mCamera,view,rotate);
            FrameLayout preview = view.findViewById(R.id.camera_preview);
            preview.removeAllViews();
            preview.addView(mPreview);
            mPreview.startCameraPreview();
        } else{
            Log.d("CameraTest","Error, Camera failed to open");
        }
        return qOpened;
    }


    /**
     * Safe method for getting a camera instance.
     */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Clear any existing preview / camera.
     */
    private void releaseCameraAndPreview() {

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if(mPreview != null){
            mPreview.destroyDrawingCache();
            mPreview.mCamera = null;
        }
    }

    public void openFolder()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
        intent.setDataAndType(uri, "*/*");
        startActivityForResult(intent,15);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 15:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    } else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }



    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(),uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri, null);
        } else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }

        if(imagePath.substring(imagePath.length()-3)=="mp4"){
            openVideo(imagePath);
        } else{
            openImage(imagePath);
        }
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        if(imagePath.substring(imagePath.length()-3)=="mp4"){
            openVideo(imagePath);
        } else{
            openImage(imagePath);
        }
    }

    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri,null, selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void openImage(String uri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://"+uri),"image/*");
        startActivity(intent);
    }

    private void openVideo(String uri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://"+uri),"video/mp4");
        startActivity(intent);
    }

    /**
     * Surface on which the camera projects it's capture results.
     */
    class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

        // SurfaceHolder
        private SurfaceHolder mHolder;

        // Our Camera.
        private Camera mCamera;

        // Parent Context.
        private Context mContext;

        // Camera Sizing (For rotation, orientation changes)
        private Camera.Size mPreviewSize;

        // List of supported preview sizes
        private List<Camera.Size> mSupportedPreviewSizes;

        // Flash modes supported by this camera
        private List<String> mSupportedFlashModes;

        // View holding this camera.
        private View mCameraView;

        //
        private int rotate;

        public CameraPreview(Context context, Camera camera, View cameraView, int rotate) {
            super(context);

            // Capture the context
            mCameraView = cameraView;
            mContext = context;
            setCamera(camera);

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setKeepScreenOn(true);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            this.rotate = rotate;
        }

        /**
         * Start recording method
         */
        public void startRecord(){
            mediaRecorder = new MediaRecorder();
            mCamera.lock();
            mCamera.unlock();

            mediaRecorder.setCamera(mCamera);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setVideoSize(1280,720);
            mediaRecorder.setPreviewDisplay(mHolder.getSurface());
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES), "CameraTest");
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d("CameraTest", "Required media storage does not exist");
                }
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename="/VID_"+timeStamp+".mp4";
            File file=new File(mediaStorageDir,filename);
            mediaRecorder.setOutputFile(mediaStorageDir+filename);
            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaRecorder.start();
        }

        /**
         * Begin the preview of the camera input.
         */
        public void startCameraPreview()
        {
            try{
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        /**
         * Extract supported preview and flash modes from the camera.
         */
        private void setCamera(Camera camera)
        {
            mCamera = camera;
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportedFlashModes = mCamera.getParameters().getSupportedFlashModes();

            // Set the camera to Auto Flash mode.
            if (mSupportedFlashModes != null && mSupportedFlashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)){
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                mCamera.setParameters(parameters);
            }

            requestLayout();
        }

        /**
         * The Surface has been created, now tell the camera where to draw the preview.
         */
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setPreviewDisplay(holder);
                Log.d("CameraTest","Surface Created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Dispose of the camera preview.
         */
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mCamera != null){
                mCamera.stopPreview();
            }
        }

        /**
         * React to surface changed events
         */
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                Camera.Parameters parameters = mCamera.getParameters();

                // Set the auto-focus mode to "continuous"
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

                // Preview size must exist.
                if(mPreviewSize != null) {
                    Camera.Size previewSize = mPreviewSize;
                    parameters.setPreviewSize(previewSize.width, previewSize.height);
                }

                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (Exception e){
                Log.d("CameraTest","Null parameters passed");
            }
        }

        /**
         * Calculate the measurements of the layout
         */
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            setMeasuredDimension(width, height);

            if (mSupportedPreviewSizes != null){
                mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
            }
        }

        /**
         * Update the layout based on rotation and orientation changes.
         */
        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom){
            mCamera.setDisplayOrientation(rotate);
        }


        private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height)
        {
            Camera.Size optimalSize = null;

            final double ASPECT_TOLERANCE = 0.1;
            double targetRatio = (double) height / width;

            // Try to find a size match which suits the whole screen minus the menu on the left.
            for (Camera.Size size : sizes){

                if (size.height != width) continue;
                double ratio = (double) size.width / size.height;
                if (ratio <= targetRatio + ASPECT_TOLERANCE && ratio >= targetRatio - ASPECT_TOLERANCE){
                    optimalSize = size;
                }
            }

            // If we cannot find the one that matches the aspect ratio, ignore the requirement.
            if (optimalSize == null) {
                optimalSize = sizes.get(0);
            }

            return optimalSize;
        }
    }

    /**
     * Picture Callback for handling a picture capture and saving it out to a file.
     */
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile();
            if (pictureFile == null){
                Toast.makeText(getActivity(), "Image retrieval failed.", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                // Restart the camera preview.
                safeCameraOpenInView(mCameraView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Used to return the camera File output.
     */
    private File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraTest");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("CameraTest", "Required media storage does not exist");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        Log.d("CameraTest","Image successfully saved");

        return mediaFile;
    }


    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
    }

}
