package com.packt.snake.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.snake.MyAssetsManager;
import com.packt.snake.Screens.MultiGameScreen;
import com.packt.snake.Screens.SingleGameScreen;
import com.packt.snake.SnakeGame;
import com.packt.snake.sprites.Radar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Hud implements Disposable{
    public Stage stage;//a box to put things in
    private Viewport viewport;//an independent vp for hud

    private Integer worldTimer;
    private float timeCount;
    //private int score;
    private OrthographicCamera camera;
    private Skin myskin,myskin2;
    private MyAssetsManager myAm;
    private int roomsize;
    private ArrayList<Label> nameLabelList = new ArrayList<Label>();
    private ArrayList<Label> scoreLabelList = new ArrayList<Label>();
    private ArrayList<String> nameList = new ArrayList<String>();
    private ArrayList<String> deadlist = new ArrayList<String>();
    private ArrayList<scorerow> rowlist = new ArrayList<scorerow>();
    private Table table;


    //Label scoreLable;
    //Label snakeLabel;

    public Hud(MyAssetsManager manager){
        myAm = manager;

        roomsize = myAm.userdata.size();

        //myskin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        myAm.loadSkin();
        myAm.loadSkin2();
        myAm.manager.finishLoading();
        myskin = myAm.manager.get(myAm.SKIN);
        myskin2 = myAm.manager.get(myAm.SKIN2);

        viewport = new FitViewport(myAm.V_WIDTH,myAm.V_HEIGHT,new OrthographicCamera());
        //camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //camera.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
        //viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera);
        stage = new Stage(viewport, myAm.batch);
        //Gdx.input.setInputProcessor(stage);
        setupScoreBand();
       // setupButton();

    }


    private void setupScoreBand(){
        table = new Table();//organize things in stage
        table.top().right().padRight(20);
        table.setFillParent(true);//set table the same size of stage

        for (Map.Entry<String, int[]> entry : myAm.userdata.entrySet()) {
            Label nameLabel = new Label(entry.getKey(),myskin,"big");
            //nameList.add(entry.getKey());
            //nameLabelList.add(nameLabel);

            Label scoreLable = new Label(String.format("%06d",0),myskin,"big");
            //Object value = entry.getValue();
            //scoreLabelList.add(scoreLable);
            table.add(nameLabel).expandX();
            table.add(scoreLable).expandX();
            table.row();

            rowlist.add(new scorerow(entry.getKey(),entry.getValue()[2]));
        }

        Button button1 = new TextButton("GO",myskin);
        button1.setSize(90,90);
        button1.setPosition(5,5);
        button1.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("button pressed");
                myAm.userdata.get(myAm.myUsername)[1] *= -1;
                return super.touchDown(event, x, y, pointer, button);
            }
        });


        //table.add(button1).expand().bottom();

        stage.addActor(button1);


        stage.addActor(table);
    }

    public void addImage(){
        myAm.loadHubResource();
        myAm.manager.finishLoading();
        Texture texture = myAm.manager.get(myAm.SPEEDUP);
        Image image1 = new Image(texture);
        image1.setPosition(0,0);
        stage.addActor(image1);

    }

    public void addRadar(Radar radar){
        Texture texture = radar.getRadarTexture();
        Image image = new Image(texture);
        image.setPosition(800,250);
        stage.addActor(image);
    }

    public void updateScore(){
        if (myAm.disconnect){
            System.out.println("==============hud know disconnect = "+myAm.disconnectP);
            for (int i = 0; i < rowlist.size(); i++){
                scorerow row = rowlist.get(i);
                System.out.println("row name = "+row.name);
                if (row.name.equals(myAm.disconnectP)){
                    row.score = -2;
                    rowlist.set(i,row);
                    myAm.disconnect = false;
                    break;
                }
            }
        }
        for (int i = 0; i < rowlist.size(); i++){
            scorerow row = rowlist.get(i);
            if (row.score >= 0)
                row.score = myAm.userdata.get(row.name)[2];
        }
        Collections.sort(rowlist);
        table.clearChildren();
        for (int i = 0; i < rowlist.size(); i++){
            scorerow row = rowlist.get(i);
            Label nameLabel = new Label(row.name,myskin,"big");
            table.add(nameLabel).expandX();
            Label scoreLable;
            if (row.score == -1){
                scoreLable = new Label("DEAD",myskin,"big");
            } else if (row.score == -2){
                scoreLable = new Label("DISCONNECT",myskin,"big");
            } else {
                scoreLable = new Label(String.format("%06d",row.score),myskin,"big");
            }
            table.add(scoreLable).expandX();
            table.row();

        }

    }

    public void updateDead(String name){
        for (int i = 0; i < rowlist.size(); i++){
            scorerow row = rowlist.get(i);
            if (row.name.equals(name)){
                row.score = -1;
                rowlist.set(i,row);
            }
        }
    }

    private class scorerow implements Comparable<scorerow>{
        String name;
        int score;

        scorerow (String n, int s){
            this.name = n;
            this.score = s;
        }

        void setRowDead(){
            this.score = -1;
        }

        @Override
        public int compareTo(scorerow row) {
            return row.score - this.score ;
        }
    }



    @Override
    public void dispose() {
        stage.dispose();
    }
}
