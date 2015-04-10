package com.luk.presentspresents;

import android.os.Handler;

public class OnBlendziorDeath {
	public static boolean isStarted = false;
	private static Handler handler = new Handler();
	private static MainActivity activity;
	private static Runnable stepTimer = new Runnable() { 
		@Override
		public  void run() {
			activity.OnOpponentDeath();
			handler.postDelayed(this, 25);
		}
	};
	public static void start(MainActivity view) {
		if(!isStarted) {
			handler.postDelayed(stepTimer, 0);
			isStarted = true;
		}
		activity = view;
	}
}