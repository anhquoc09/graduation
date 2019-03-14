package com.example.realestate.ui.widget;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


/**
 * @author anhquoc09
 * @since 10/03/2019
 */

public class DebounceEditText {

    private static final int MESSAGE_TEXT_CHANGED = 1;

    private static final int MESSAGE_DELAY_MS = 500;

    private DebounceHandler mHandler;

    private EditText mEditText;

    public DebounceEditText(EditText editText, PublishResultListener listener) {
        mEditText = editText;

        mHandler = new DebounceHandler(listener);

        init();
    }

    private void init() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Message message = mHandler.obtainMessage();

                message.what = MESSAGE_TEXT_CHANGED;
                message.obj = s.toString();

                mHandler.sendMessageDelayed(message, MESSAGE_DELAY_MS);
            }
        });
    }

    public void destroy() {
        mEditText = null;

        mHandler.removeCallbacks(null);
    }

    /**
     *  {@link DebounceHandler}
     */
    private static class DebounceHandler extends Handler {

        private PublishResultListener mListener;

        public DebounceHandler(PublishResultListener listener) {
            mListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TEXT_CHANGED:
                    if (mListener != null) {
                        mListener.onPublishResult((String) msg.obj);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /**
     * {@link PublishResultListener}
     */
    public interface PublishResultListener {
        void onPublishResult(CharSequence result);
    }
}
