package com.packt.snake.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.snake.MyAssetsManager;
import com.packt.snake.SnakeGame;

public class GameOverScreen implements Screen{
    private Viewport viewport;
    private Stage stage;
    private SnakeGame game;
    private Skin myskin;
    private boolean restart = false,share = false;
    private MyAssetsManager myAm;

    public GameOverScreen(MyAssetsManager manager){
        //this.game = game;
        this.myAm = manager;
        viewport = new FitViewport(myAm.V_WIDTH,myAm.V_HEIGHT,new OrthographicCamera());
        myskin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        stage = new Stage(viewport,((SnakeGame)game).batch);
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.BLACK);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("GAME OVER", myskin,"title");
        Label score = new Label("", myskin,"title");
        gameOverLabel.setFontScale(2f);
        score.setFontScale(2f);

        Button button1 = new TextButton("RESTART",myskin);
        //button1.setSize(300,200);
        //button1.setScale(2f);
        //button1.setPosition(Gdx.graphics.getWidth()/2-150,100);
        button1.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                restart = true;
                return super.touchDown(event, x, y, pointer, button);
                //return true;
            }
        });

        Button button2 = new TextButton("SHARE",myskin);
        button2.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                share = true;
                return super.touchDown(event, x, y, pointer, button);
                //return true;
            }
        });

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(score).expandX();
        table.row();
        table.add(button1).expandX();
        table.row();
        table.add(button2).expandX().padTop(20);

        //table.scaleBy(10f);
        stage.addActor(table);
        //stage.addActor(button1);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.WHITE.r,Color.WHITE.g,Color.WHITE.b,Color.WHITE.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        if (restart){
        //if(Gdx.input.justTouched()){
            //game.setScreen(new GameScreen(game));
            dispose();
        }
        if (share){
            shareScore();
        }
    }

    private void shareScore(){
        // do something
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
        stage.dispose();
        myskin.dispose();
    }
}
