package com.packt.snake.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packt.snake.MyAssetsManager;
import com.packt.snake.Scenes.Hud;
import com.packt.snake.SnakeGame;
import com.packt.snake.sprites.Food;
import com.packt.snake.sprites.Radar;
import com.packt.snake.sprites.Snake;

import java.util.ArrayList;

public class MultiGameScreen implements Screen {

    private static float speed = 0.1f;
    private float timer = speed;
    private static final float roomOutRatio = 0.2f;

    private enum STATE {
        NORMAL, GAME_OVER
    }

    private STATE state = STATE.NORMAL;
    private STATE previousState = STATE.NORMAL;

    private Texture background;
    private float screenWidth;
    private float screenHeight;

    private SnakeGame game;
    private MyAssetsManager myAM;
    private Hud hud;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Food myfood;
    private Snake mySnake;
    private ArrayList<Snake> snakeList = new ArrayList<Snake>();
    private int directionDegree = 0, lastdirection = directionDegree;
    private Radar radar;

    private float stateTimer;//count how many seconds the current state last
    //private BitmapFont bitmapFont;
    //private GlyphLayout layout = new GlyphLayout();
    FlingDirection myFlingDirection;
    private Stage myStage;
    private Skin mySkin, mySkin2;
    private Touchpad touchpad;

    private float gravityX;
    private float gravityY;

    public MultiGameScreen(SnakeGame game) {
//        super(game);
        this.game = game;
        myAM = this.game.getAm();
        myfood = new Food(this.game);
        //initiate the snake controlled my device owner
        int position = getInitialPosition(myAM.myUsername);
        mySnake = new Snake(this.game,position,position,myAM.myUsername);
        radar = new Radar(5100,5100);

        //initial snakes for remote users
        for (String name : myAM.userdata.keySet()){
            if (!name.equals(myAM.myUsername)){
                int position2 = getInitialPosition(name);
                snakeList.add(new Snake(this.game,position2,position2,name));
            }
        }
        snakeList.add(mySnake);

        viewport = new FitViewport(myAM.getvWidth(), myAM.getvHeight());
        //camera = new OrthographicCamera(screenWidth, screenHeight);
        camera = new OrthographicCamera();
        viewport.apply();

        camera.position.set(mySnake.getHeadPosX(), mySnake.getHeadPosY(), 0);
        screenWidth = myAM.getvWidth()*2;
        screenHeight = myAM.getvHeight()*2;
        camera.viewportWidth = screenWidth;
        camera.viewportHeight = screenHeight;

    }

    @Override
    public void show() {
        camera.update();
        addUI();
        setControl();
    }

    public void addUI(){
        hud = new Hud(myAM);
        myAM.loadMap();
        myAM.manager.finishLoading();
        background = myAM.manager.get(myAM.MAP1);
        myAM.mapsize = background.getWidth();

        myStage = new Stage(viewport, myAM.batch);

        myAM.loadHubResource();
        myAM.manager.finishLoading();
        Texture texture = myAM.manager.get(myAM.SPEEDUP);
        Image image1 = new Image(texture);
        image1.setPosition(0,0);
        myStage.addActor(image1);
    }

    public void setControl(){
        if (myAM.controlMode == 1 || myAM.controlMode == 3){ //fling control
            myFlingDirection = new FlingDirection();
            Gdx.input.setInputProcessor(new GestureDetector(myFlingDirection));
            hud.addImage();


        } else if (myAM.controlMode == 2){ //joystick
            Gdx.input.setInputProcessor(myStage);
            myAM.loadSkin();
            myAM.loadSkin2();
            myAM.manager.finishLoading();
            mySkin = myAM.manager.get(myAM.SKIN);
            mySkin2 = myAM.manager.get(myAM.SKIN2);

            Button button1 = new TextButton("GO",mySkin);
            button1.setSize(90,90);
            button1.setPosition(5,5);

            button1.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("button pressed");
                    myAM.userdata.get(myAM.myUsername)[1] *= -1;
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
            myStage.addActor(button1);

            Texture touchpadbase = new Texture("handlebase.png");
            Texture knob = new Texture("handle.png");
            TextureRegionDrawable baseRegion = new TextureRegionDrawable(new TextureRegion(touchpadbase,0,0,200,200));
            TextureRegionDrawable knobRegion = new TextureRegionDrawable(new TextureRegion(knob,0,0,50,50));
            Touchpad.TouchpadStyle style= new Touchpad.TouchpadStyle(baseRegion,knobRegion);
            touchpad = new Touchpad(30,style);
            touchpad.setBounds(myAM.getvWidth()-155,5,150,150);

            touchpad.addListener(new InputListener(){

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    super.touchDragged(event, x, y, pointer);
                    float deltaX = touchpad.getKnobPercentX();
                    float deltaY = touchpad.getKnobPercentY();
                    System.out.println("===============x = "+ deltaX+"; y = "+deltaY);
                }
            });

            myStage.addActor(touchpad);
            hud.addImage();

        } else { //gravity
            Gdx.input.setInputProcessor(hud.stage);
            myAM.loadHubResource();
            myAM.manager.finishLoading();
            Texture texture = myAM.manager.get(myAM.SPEEDUP);
            Image image1 = new Image(texture);
            image1.setPosition(0,0);
            myStage.addActor(image1);

        }
    }

    public int getInitialPosition(String name){
        for (int i = 0; i < myAM.userlist.length;i++){
            if (name.equals(myAM.userlist[i]))
                return (i+1)*500;
        }
        return 500;
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
        mySnake.dispose();
        hud.dispose();
    }

    @Override
    public void render(float delta) {
        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = state == previousState ? stateTimer + delta : 0;
        //update previous state
        previousState = state;
        switch (state) {
            case NORMAL: {
                updateAllSnakes(delta);
            }
            break;
            case GAME_OVER: {
                if (stateTimer >= 1) { //wait for 1 second after died
                    game.startGameOver();
                    dispose();
                }
            }
            break;
        }
        clearScreen();
        draw();
    }

    public void clearScreen() {
        Gdx.gl.glClearColor(Color.WHITE.r,Color.WHITE.g,Color.WHITE.b,Color.WHITE.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void draw() {
        myAM.batch.setProjectionMatrix(camera.projection);
        myAM.batch.setTransformMatrix(camera.view);

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        mySnake.drawSnake(game.batch);
        for (Snake snake : snakeList) {
            snake.drawSnake(game.batch);
        }
        //draw food
        for (int[] x : myfood.getFoodlist()) {
            myAM.batch.draw(myfood.getFood(), x[0], x[1]);
        }
        myAM.batch.end();

        myStage.draw();
        //draw the score hub
        myAM.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    public void checkFoodCollision(Snake snake) {
        int[] head = {snake.getHeadPosX(), snake.getHeadPosY()};
        if (checkContain(myfood.getFoodObj(), head, snake)) {
            snake.lengthenBody(head[0], head[1]);
            snake.updateScore();
        }
    }

    public boolean checkContain(ArrayList<Food.SpeFood> al, int[] lst, Snake snake) {
        for (Food.SpeFood x : al) {
            double collisionRadius = distance(x.getPosX(),x.getPosY(),lst[0]+snake.getSize()/2,lst[1]+snake.getSize()/2);
            double foodRadius = 32;
            if (collisionRadius <= (snake.getSize()/2+foodRadius/2)) {
                if(x.getType().equals("SpeedUp")){
                    snake.setYellowAppleTimer(40);
                }
                myfood.removeFood(x);
                return true;
            }
        }
        return false;
    }

    public void updateAllSnakes(float delta){
        timer-=delta;
        if(timer<=0){
            timer=speed;

            //////////update local snake//////////////////
            int speedLimit = 1;
            if(myAM.userdata.get(mySnake.getMyUsername())[1] < 0){
                if(mySnake.getBody().size > 3){
                    //speedLimit = 1+myAM.userdata.get(mySnake.getMyUsername())[1];
                    speedLimit = 2;
                    System.out.println("Speed limit = "+speedLimit);
                }
            }

            if(mySnake.getYellowAppleTimer()>0){
                speedLimit = 2;
            }

            if (myAM.controlMode == 3)
                directionDegree = getGravityDegree(gravityX,gravityY);
            else if (myAM.controlMode == 2)
                getTouchpadDegree();

            //tell server if there is a new direction
            if (lastdirection != directionDegree){
                lastdirection = directionDegree;
                myAM.setNewDirection(directionDegree);
            }

            for(int i=0;i<speedLimit;i++) {
                int snakeXBeforeUpdate = mySnake.getHeadPosX();
                int snakeYBeforeUpdate = mySnake.getHeadPosY();
                mySnake.setSettingDirection(myAM.getMyDirection());
                mySnake.moveSnake();
                if (mySnake.checkEdge()){
                    state = STATE.GAME_OVER;
                }

                checkFoodCollision(mySnake);
                myfood.placeFood();
                mySnake.updateSize();

                boolean isBodyCollision = bodyCollision(mySnake, snakeList.size() - 1, snakeList);
                if (isBodyCollision) {
                    state = STATE.GAME_OVER;
                }

                if (state == STATE.GAME_OVER) {
                    mySnake.setHeadPosX(snakeXBeforeUpdate);
                    mySnake.setHeadPosY(snakeYBeforeUpdate);
                    myfood.placeFood(mySnake.getDeadSnake());
                    break;
                } else {
                    mySnake.updateBodyPoo(snakeXBeforeUpdate, snakeYBeforeUpdate, myfood);
                }
            }


            /////////update remote snake//////////////////////
            if (myAM.disconnect) { //get rid of the disconnected snake
                for (int i = snakeList.size() - 1; i >= 0; i--) {
                    Snake snake = snakeList.get(i);
                    if (snake.getMyUsername().equals(myAM.disconnectP)) {
                        snakeList.remove(i);
                        break;
                    }
                }
            }

            for (int i = snakeList.size()-2;i>=0;i--){
                Snake snake = snakeList.get(i);

                speedLimit = 1;
                if(myAM.userdata.get(snake.getMyUsername())[1] < 0){
                    if(snake.getBody().size > 3){
                        speedLimit = 2;
                    }
                }

                for(int j=0;j<speedLimit;j++) {
                    int snkXB4Update = snake.getHeadPosX();
                    int snkYB4Update = snake .getHeadPosY();
                    snake.setSettingDirection(myAM.getRemoteDirection(snake.getMyUsername()));
                    snake.moveSnake();

                    if (snake.checkEdge() || bodyCollision(snake, i, snakeList)) {
                        snake.setHeadPosX(snkXB4Update);
                        snake.setHeadPosY(snkYB4Update);
                        myfood.placeFood(snake.getDeadSnake());
                        hud.updateDead(snake.getMyUsername());
                        snakeList.remove(i);
                        break;
                    } else {
                        snake.updateBodyPoo(snkXB4Update,snkYB4Update,myfood);
                    }
                    checkFoodCollision(snake);
                    myfood.placeFood();
                    snake.updateSize();
                }

            }


            ////////////////////////////////////////////////////
            camera.position.set(mySnake.getHeadPosX(), mySnake.getHeadPosY(), 0);
            float i = mySnake.getScore()/10;
            camera.viewportWidth = screenWidth * (1+i*roomOutRatio);
            camera.viewportHeight = screenHeight * (1+i*roomOutRatio);

            camera.update();

            hud.updateScore();

            /*---update radar---*/
            radar.updateRadar(mySnake.getHeadPosX(),mySnake.getHeadPosY(),myfood);
            hud.addRadar(radar);
        }
    }

    public class FlingDirection implements GestureDetector.GestureListener {
        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            System.out.println("x = "+x+"; y = "+y);
            if (x<185 && y > 845) {
                speedUp();
            }
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

        @Override
        public void pinchStop() {

        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            //System.out.println("fling called!");
            if (myAM.controlMode == 3) //gravity mode, disable fling
                return false;
            if (stateTimer < 0.5)
                return false;
            directionDegree = computeDirectionDegree(velocityX,velocityY);
            return false;
        }


    }

    public double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public int getGravityDegree(float gravityX, float gravityY){
        float myX = Gdx.input.getAccelerometerY();
        float myY = Gdx.input.getAccelerometerX();
        //System.out.println("X = "+myX+"; Y = "+myY);
        int degree = computeDirectionDegree(myX,myY);
        directionDegree = degree;
        return degree;
    }

    public int computeDirectionDegree(float velocityX ,float velocityY){
        int degree;
        if (velocityX >= 0 && velocityY >= 0) {
            degree = 360 - (int)Math.toDegrees(Math.atan(velocityY / velocityX));
            //System.out.println("degree = " + directionDegree);
        } else if (velocityX >= 0 && velocityY <= 0) {
            degree = (int)Math.toDegrees(Math.atan(-velocityY / velocityX));
            //System.out.println("degree = " + directionDegree);
        } else if (velocityX <= 0 && velocityY >= 0) {
            degree = (int)Math.toDegrees(Math.atan(-velocityY / velocityX)) + 180;
            //System.out.println("degree = " + directionDegree);
        } else {
            degree = 180 - (int)Math.toDegrees(Math.atan(velocityY / velocityX));
            //System.out.println("degree = " + directionDegree);
        }
        //System.out.println("==============degree = "+degree);
        return degree;
    }

    public void getTouchpadDegree(){
        if (touchpad.isTouched()){
            float deltaX = touchpad.getKnobPercentX();
            float deltaY = touchpad.getKnobPercentY();
            int degree = computeDirectionDegree(deltaX,-deltaY);
            directionDegree = degree;
            //System.out.println("x = "+ deltaX+"; y = "+deltaY+"; degree = "+degree);

        }
    }

    public boolean bodyCollision(Snake mySnake,int myIndex, ArrayList<Snake> snakeList){

        for(int i = 0;i<snakeList.size();i++){
            if(myIndex == i)continue;
            Snake snake = snakeList.get(i);
            Array<Snake.SnakeBody> snakeBody = snake.getBody();
            for(Snake.SnakeBody sb:snakeBody){
                if(distance(mySnake.getHeadPosX()+mySnake.getSize()/2,mySnake.getHeadPosY()+mySnake.getSize()/2,
                        sb.getX()+snake.getSize()/2,sb.getY()+snake.getSize()/2)
                        < (snake.getSize()/2+mySnake.getSize()/2)){
                    return true;
                }
            }
        }
        return false;
    }

    public void speedUp(){
        //myAM.userdata.get(mySnake.getMyUsername())[1] *= -1;
        myAM.speedupchange = true;
    }
}

