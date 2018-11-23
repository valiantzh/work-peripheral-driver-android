package com.dcdz.drivers.demo;

import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dcdz.drivers.PDApplication;
import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.Dictionary;
import com.dcdz.drivers.utils.KeyboardUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.exit_button)
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PDApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    KeyboardUtil keyboardUtil;

    void initView() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    keyboardUtil = new KeyboardUtil(keyboardView, getBaseContext(), (EditText) view);
                    keyboardUtil.showKeyboard();
                }
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (keyboardUtil != null) {
                    keyboardUtil.showKeyboard();
                }
            }
        });
        //TODO forDebug
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.CHINA);
        String date = dateFormat.format(new Date());
        password.setText("dcdz" + date);
        Dictionary.load();
    }

    private void attemptLogin() {
        String passWd = password.getText().toString();
        if (passWd.length() != 10) {
            password.setError(getString(R.string.error_incorrect_password));
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.CHINA);
        String date = dateFormat.format(new Date());
        if (passWd.equals("dcdz" + date)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            password.setError(getString(R.string.error_incorrect_password));
        }
    }

    //屏蔽返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
