package com.luk.presentspresents;

import android.os.Handler;
public class PerformDelay {
	private boolean isStarted = false;
	private Handler handler = new Handler();
	private MainActivity activity;
	private Runnable stepTimer = new Runnable() { 
		@Override
		public void run() {
			;
		}
	};
	/**
	 * @param view - this
	 * @param id_funkcji - funkcja która ma zostać wywołana z opóźnieniem
	 * 0 - @CollectPoints
	 * 1- @BlendziorHit
	 * @param czas - opóźnienie czasowe
	 */
	public  void start(MainActivity view, int id_funkcji, int czas) {
		if(!isStarted) {
			handler.postDelayed(stepTimer, czas);
			isStarted = true;
		}
		activity = view;
		if(id_funkcji==0){
			activity.CollectPoints();
		}
		else if(id_funkcji==1){
			activity.BlendziorHit();
			
		}
	}
}