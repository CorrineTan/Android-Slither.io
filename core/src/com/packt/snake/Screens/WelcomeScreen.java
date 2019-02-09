package com.packt.snake.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.snake.SnakeGame;

public class WelcomeScreen implements Screen {
    private Texture background;
    private Skin myskin;
    private Texture playComUpButton, playComDownButton;
    //private Texture playComUpButton, playComDownButton;
    private OrthographicCamera camera;
    private SnakeGame game;
    private Viewport viewport;
    private Stage stage;
    private boolean next = false, multi = false, connecttest = false, waiting = false;
    private float waittime = 0.5f;
    private float timer = waittime;
    private Dialog joinDialog;
    private Label dialogLabel;




    public WelcomeScreen(SnakeGame game){
        this.game = game;
        viewport = new ScreenViewport();
        myskin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        //game.score = 0;
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        Button button1 = new TextButton("Single Mode!",myskin);
        button1.setSize(300,200);
        button1.setPosition(Gdx.graphics.getWidth()/2-500,Gdx.graphics.getHeight()/2-100);

        button1.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                next = true;
                return super.touchDown(event, x, y, pointer, button);
                //return true;
            }
        });

        Button button2 = new TextButton("Multi-player",myskin);
        button2.setSize(300,200);
        button2.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2-100);
        button2.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                multi = true;
                return super.touchDown(event, x, y, pointer, button);
                //return true;
            }
        });

        Button button3 = new TextButton("Server test",myskin);
        button3.setSize(300,200);
        button3.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2-500);
        button3.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                connecttest = true;

                return super.touchDown(event, x, y, pointer, button);
                //return true;
            }
        });

        Label titleLabel = new Label("Crazy Slither ", myskin,"title");
        titleLabel.setFontScale(3f);
        titleLabel.setPosition(Gdx.graphics.getWidth()/2-titleLabel.getWidth()/2*3,Gdx.graphics.getHeight()-300);

        joinDialog = new Dialog("waiting for join", myskin) {
            public void result(Object obj) {
                System.out.println("result "+obj);
            }
        };
        joinDialog.setSize(1000,400);
        joinDialog.button("Yes", true); //sends "true" as the result
        joinDialog.button("No", false);  //sends "false" as the result

        joinDialog.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
        joinDialog.setPosition(Gdx.graphics.getWidth()/2-joinDialog.getWidth()/2,Gdx.graphics.getHeight()/2-joinDialog.getHeight()/2);

        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(button3);
        stage.addActor(titleLabel);

        background = new Texture("bg.png");

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //game.batch.setProjectionMatrix(camera.combined);
        timer -= delta;
        game.batch.begin();
        game.batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //game.batch.draw(playbutton,camera.position.x-playbutton.getWidth()/2,camera.position.y-playbutton.getHeight()/2);
        //game.batch.draw(playbutton,0,0);
        //stage.act();

        game.batch.end();

        if (multi){
            Dialog dialog = new Dialog("Warning", myskin) {
                public void result(Object obj) {
                    System.out.println("result "+obj);
                }
            };
            dialog.setSize(1000,400);
            Label quit = new Label("Are you sure you want to quit?", myskin);
            quit.setFontScale(3f);
            //dialog.text("Are you sure you want to quit?",);
            dialog.text(quit);
            dialog.button("Yes", true); //sends "true" as the result
            dialog.button("No", false);  //sends "false" as the result

            dialog.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
            dialog.setPosition(Gdx.graphics.getWidth()/2-dialog.getWidth()/2,Gdx.graphics.getHeight()/2-dialog.getHeight()/2);
            //dialog.show();

            stage.addActor(dialog);

            multi = false;
        }
        /*
        if(connecttest){
            int x = game.myConnect.initConnect();
            if (x ==1)
                game.myConnect.start();
            dialogLabel = new Label(game.userlist, myskin);
            dialogLabel.setFontScale(3f);
            //dialog.text("Are you sure you want to quit?",);
            //System.out.println("userlist = "+game.getUserlist());
            joinDialog.text(dialogLabel);

            stage.addActor(joinDialog);
            connecttest = false;
            waiting = true;

        }
        */
        if (waiting && timer < 0){
            timer = waittime;
            //dialogLabel.setText(game.userlist);
           // waiting = false;
        }

        stage.draw();
        //handleInput();
        /*
        if (next && timer < 0){
            game.setScreen(new GameScreen(game));
            dispose();
        }
        */

    }

    private void setupTexture(){
        Pixmap pixmapOrigin = new Pixmap(Gdx.files.internal("playwithcomputer.png"));
        Pixmap playComUp = new Pixmap(200, 200, pixmapOrigin.getFormat());
        playComUp.drawPixmap(pixmapOrigin,
                0, 0, pixmapOrigin.getWidth(), pixmapOrigin.getHeight(),
                0, 0, playComUp.getWidth(), playComUp.getHeight()
        );
        //playComUpButton = new Texture(playComUp);
        pixmapOrigin.dispose();
        playComUp.dispose();
    }

    /*
    private void handleInput() {
        if(Gdx.input.justTouched()){
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }
    */

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
        background.dispose();

    }
}
