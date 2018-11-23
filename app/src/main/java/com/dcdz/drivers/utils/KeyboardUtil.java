package com.dcdz.drivers.utils;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.dcdz.drivers.R;

import java.util.List;

public class KeyboardUtil {
    private KeyboardView keyboardView;
    private Keyboard letter;// 字母键盘
    private Keyboard digital;// 数字键盘
    public boolean isNumber = false;// 是否数据键盘
    public boolean isUpper = false;// 是否大写

    private EditText ed;

    public KeyboardUtil(KeyboardView view, Context ctx, EditText edit) {
        this.ed = edit;
        letter = new Keyboard(ctx, R.xml.qwerty);
        digital = new Keyboard(ctx, R.xml.symbols);
        keyboardView = view;
        keyboardView.setKeyboard(letter);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    public KeyboardUtil(Activity activity) {
        keyboardView = activity.findViewById(R.id.keyboard_view);
        letter = new Keyboard(activity.getBaseContext(), R.xml.qwerty);
        digital = new Keyboard(activity.getBaseContext(), R.xml.symbols);
        keyboardView.setKeyboard(letter);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    public void showKeyboard(EditText editText) {
        this.ed = editText;
        if (ed.getInputType() == InputType.TYPE_CLASS_NUMBER) {
            keyboardView.setKeyboard(digital);
        } else {
            keyboardView.setKeyboard(letter);
        }
        keyboardView.setOnKeyboardActionListener(listener);
        showKeyboard();
    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                changeKey();
                keyboardView.setKeyboard(letter);

            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
                if (isNumber) {
                    isNumber = false;
                    keyboardView.setKeyboard(letter);
                } else {
                    isNumber = true;
                    keyboardView.setKeyboard(digital);
                }
            } else if (ed != null) {
                Editable editable = ed.getText();
                int start = ed.getSelectionStart();
                if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                } else if (primaryCode == 57419) { // go left
                    if (start > 0) {
                        ed.setSelection(start - 1);
                    }
                } else if (primaryCode == 57421) { // go right
                    if (start < ed.length()) {
                        ed.setSelection(start + 1);
                    }
                } else {
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }
        }
    };

    /**
     * 键盘大小写切换
     */
    private void changeKey() {
        List<Key> keylist = letter.getKeys();
        if (isUpper) {//大写切换小写
            isUpper = false;
            for (Key key : keylist) {
                if (key.label != null && isword(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else {//小写切换大写
            isUpper = true;
            for (Key key : keylist) {
                if (key.label != null && isword(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isword(String str) {
        String wordstr = "abcdefghijklmnopqrstuvwxyz";
        if (wordstr.indexOf(str.toLowerCase()) > -1) {
            return true;
        }
        return false;
    }


}
