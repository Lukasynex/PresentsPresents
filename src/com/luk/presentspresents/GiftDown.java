package com.luk.presentspresents;

import android.os.Handler;

public class GiftDown {
	private boolean isStarted = false;
	private int id;
	private Handler handler = new Handler();
	private MainActivity activity;
	private Runnable stepTimer = new Runnable() { 
		@Override
		public void run() {
			activity.PresentFall(id);
			handler.postDelayed(this, 50);
		}
	};
	public  void start(MainActivity view, int ID) {
		if(!isStarted) {
			handler.postDelayed(stepTimer, 0);
			isStarted = true;
		}
		activity = view;
		id=ID;
	}
}