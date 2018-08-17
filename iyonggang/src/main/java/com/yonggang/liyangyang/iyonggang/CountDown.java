package com.yonggang.liyangyang.iyonggang;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class CountDown extends Thread {
	private int second;
	private Handler handler;
	
	public CountDown(int second, Handler handler) {
		super();
		this.second = second;
		this.handler = handler;
	}

	@Override
	public void run() {
		while (second > 0) {
			second--;
			try {
				sleep(1000);
				Message msgTime = new Message();
				msgTime.what = 0x123;
				Bundle time = new Bundle();
				time.putInt("time", second);
				msgTime.setData(time);
				handler.sendMessage(msgTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		handler.sendEmptyMessage(0x321);
	}
}
