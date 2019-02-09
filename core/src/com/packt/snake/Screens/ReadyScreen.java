package com.packt.snake.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.snake.MyAssetsManager;
import com.packt.snake.SnakeGame;

public class ReadyScreen implements Screen {
    private Viewport viewport;
    private MyAssetsManager mAM;
    private Skin myskin;
    private Stage stage;
    private SnakeGame game;

    public ReadyScreen(SnakeGame game){
        this.game = game;
        mAM = this.game.getAm();
        mAM.loadSkin();
        mAM.manager.finishLoading();
        myskin = mAM.manager.get(mAM.SKIN);

        viewport = new FitViewport(mAM.V_WIDTH,mAM.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,this.game.batch);


        Label gameOverLabel = new Label("READY", myskin,"title");
        gameOverLabel.setFontScale(2f);
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(gameOverLabel).expandX().padTop(20);
        stage.addActor(table);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.WHITE.r,Color.WHITE.g,Color.WHITE.b,Color.WHITE.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        if (mAM.play){
            //if(Gdx.input.justTouched()){

            game.setScreen(new MultiGameScreen(game));
            mAM.play = false;
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
