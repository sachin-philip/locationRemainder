package com.ajce.reminder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

public class Flash extends Activity {
	
	PowerManager.WakeLock wl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.flashlight);

		WindowManager.LayoutParams lp = getWindow().getAttributes();
		float brightness = 1.0f;
		lp.screenBrightness = brightness;
		getWindow().setAttributes(lp);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
	}

	@Override
	protected void onPause() {
		super.onPause();
		wl.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		wl.acquire();
	}
}