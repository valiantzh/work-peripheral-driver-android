package com.dcdz.drivers.demo.fragment.general;


import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.BaseFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LJW on 2018/3/2.
 * 声音调试
 */
public class SoundTestFragment extends BaseFragment {


    @BindView(R.id.test_sound_play)
    Button play;
    @BindView(R.id.test_sound_pause)
    Button pause;
    @BindView(R.id.test_sound_stop)
    Button stop;
    Unbinder unbinder;


    private MediaPlayer mediaPlayer = new MediaPlayer();


    public SoundTestFragment() {
        // Required empty public constructor
    }

    public static SoundTestFragment newInstance() {
        Bundle args = new Bundle();
        SoundTestFragment fragment = new SoundTestFragment();
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
    public void onDestroyView() {
        super.onDestroyView();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sound_test, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @OnClick({R.id.test_sound_play, R.id.test_sound_pause, R.id.test_sound_stop})
    public void onButtonClick(View v){
        switch (v.getId()){
            case R.id.test_sound_play:
                Log.d("SoundTest","Play");
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                break;
            case R.id.test_sound_pause:
                Log.d("SoundTest","Pause");
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.test_sound_stop:
                Log.d("SoundTest","Stop");
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    initMediaPlayer();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        initMediaPlayer();
    }

    private void initMediaPlayer(){
        try{
            AssetFileDescriptor assetFileDescriptor = getActivity().getResources().openRawResourceFd(R.raw.music);
            if(assetFileDescriptor == null) return;
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
            assetFileDescriptor.close();
            mediaPlayer.prepare();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                } else {
                    getActivity().finish();
                }
                break;
            default:
        }
    }
}
