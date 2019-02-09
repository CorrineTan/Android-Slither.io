package com.packt.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.packt.snake.Screens.GameScreen;
import com.packt.snake.Screens.ReadyScreen;
import com.packt.snake.Screens.SingleGameScreen;
import com.packt.snake.Screens.WelcomeScreen;

import java.util.HashMap;

public class SnakeGame extends Game {
	public SpriteBatch batch;//only need one, allow all screen to acces
	private MyAssetsManager myAssetManager;

	//////////setup android activity callback//////////////////
	private static MyGameCallback myGameCallback;

	public interface MyGameCallback {
		public void startScoreActivity();
	}

	public void setMyGameCallback(MyGameCallback callback){
		myGameCallback = callback;
	}

	public void startGameOver(){
		if (myGameCallback != null){
			 //go to gameover screen
			myGameCallback.startScoreActivity();
		}

	}
	///////////////////////////
	public SnakeGame(MyAssetsManager manager){
		myAssetManager = manager;
		//myAssetManager.game = this;
	}
	@Override
	public void create () {
	
		batch = new SpriteBatch();
		myAssetManager.batch = this.batch;
		//myConnect = new SocketConnect(this);
		if (myAssetManager.mode.equals("single"))
			setScreen(new SingleGameScreen(this));
		else
			setScreen(new ReadyScreen(this));

	}


	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}

	public MyAssetsManager getAm(){
		return myAssetManager;
	}


}
