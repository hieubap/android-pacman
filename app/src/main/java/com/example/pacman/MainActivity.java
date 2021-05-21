package com.example.pacman;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity {
	public static final int WIDTH = 720;
	public static final int HEIGHT = 1280;
	public Control control;

	@Override
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		control = new Control(this);
		setContentView(control);

//		registerForContextMenu(control);
	}

}
