package com.example.liveliness_diary;

import android.os.Handler;
import android.os.Message;

public class IntroThread extends Thread {
    private Handler handler;

    public IntroThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {

        Message msg = new Message();

        try {
            Thread.sleep(3000); //3초 뒤에 변환
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
