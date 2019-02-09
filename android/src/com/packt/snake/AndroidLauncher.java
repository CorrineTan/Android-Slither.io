package com.packt.snake;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.packt.snake.SnakeGame;

public class AndroidLauncher extends AndroidApplication implements SnakeGame.MyGameCallback{
	private SocketConnect myConnect;
	private MyAssetsManager manager;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		myConnect = SocketConnect.get();
		manager = myConnect.getMyAm();
		//myConnect.setAssetManager(manager);


        Intent extraIntent = getIntent();
        Bundle extraBundle = extraIntent.getExtras();
        if (!extraBundle.isEmpty()){
            String mode = extraBundle.getString("mode");
            manager.mode = mode;
        }

		SnakeGame myGame = new SnakeGame(manager);

		//manager.game = myGame;

		myGame.setMyGameCallback(this);

		initialize(myGame, config);
	}

	@Override
	public void startScoreActivity() {
		Intent intent = new Intent(this,Score_Activity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		//do nothing
	}

	@Override
	protected void onStop() {
		System.out.println("on stop is called!");
		//myConnect.setQuit(true);
		super.onStop();
	}

}
