package com.packt.snake.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.packt.snake.MyAssetsManager;
import com.packt.snake.SnakeGame;

import java.util.ArrayList;


public class Snake implements Disposable{

    /**---Initialize Parameters---**/
    private static final int initSize = 50;
    private static final int growRatio = 1;
    private static final int pooInterval = 10;
    //    private static final int step = 15;
    private int step = 20;
    private static final double maxRotate = 20.0;

    private int headPosX = 1000, headPosY = 1000;

    private Array<SnakeBody> body = new Array<SnakeBody>();
    private Texture headTexture;
    private Texture bodyTexture;

    private double currentDirection = 0.0;
    private double settingDirection = 0.0;
    private int score = 0;
    private int size = 50;
    private SnakeGame game;
    private String myUsername;
    private int pooCounter = pooInterval;
    private Pixmap pixmapOrigin, newPixmap;
    private int yellowAppleTimer = 0;

    private MyAssetsManager myAm;

    public Snake(SnakeGame game){
        this.game = game;
        myAm = this.game.getAm();
        myUsername = myAm.myUsername;
        myAm.loadSnake(myAm.myColor);
        myAm.manager.finishLoading();
        headTexture = myAm.manager.get(myAm.myheadskin);
        bodyTexture = myAm.manager.get(myAm.mybodyskin);

        resizeBody(initSize);
        for(int i=1;i<=3;i++){
            body.add(new SnakeBody(headPosX -i*step, headPosY -i*step));
        }
    }

    public Snake(SnakeGame game, int headPosX, int headPosY, String name){
        this.game = game;
        myAm = this.game.getAm();
        myUsername = name;
        this.headPosX = headPosX;
        this.headPosY = headPosY;

        //load skin based on name!!!
        myAm.loadSnake(myAm.userdata.get(myUsername)[0]);
        myAm.manager.finishLoading();
        headTexture = myAm.manager.get(myAm.myheadskin);
        bodyTexture = myAm.manager.get(myAm.mybodyskin);

        resizeBody(initSize);
        for(int i=1;i<=3;i++){
            body.add(new SnakeBody(this.headPosX -i*step, this.headPosY -i*step));
        }
        //myAm.userdata.get(myUsername)[1] = 1;
    }

    public class SnakeBody {
        private int x, y;
        //private Texture texture = new Texture("snake.png");

        public SnakeBody(int x, int y){
            this.x = x;
            this.y = y;
        }

        public void updateBodyPosition(int x, int y){
            this.x = x; this.y = y;
        }

        public void draw(Batch batch){
            if (!(x == headPosX && y == headPosY))
                batch.draw(bodyTexture, x, y);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public void updateBodyPartsPosition(int headXBeforeUpdate,int headYBeforeUpdate){
        if(body.size >= 3){
            body.removeIndex(body.size-1);
            SnakeBody tempPart = new SnakeBody(headXBeforeUpdate,headYBeforeUpdate);
            body.insert(0,tempPart);
        }
    }

    public void updateBodyPoo(int headXBeforeUpdate,int headYBeforeUpdate, Food myFood){
        if(body.size >= 3){
            body.removeIndex(body.size-1);
            if(myAm.userdata.get(myUsername)[1] < 0 && body.size >= 3){

                pooCounter--;
                if(pooCounter == 0) {
                    int foodX = body.get(body.size - 1).getX();
                    int foodY = body.get(body.size - 1).getY();
                    myFood.placeFoodAt(foodX, foodY);
                    body.removeIndex(body.size - 1);
                    pooCounter = pooInterval;
                    myAm.reduceScore(myUsername);
                }
            }
            SnakeBody tempPart = new SnakeBody(headXBeforeUpdate,headYBeforeUpdate);
            body.insert(0,tempPart);
        }
    }


    public void drawSnake(SpriteBatch sb){
        for(int i = body.size-1;i>=0;i--){
            body.get(i).draw(sb);
        }
        TextureRegion headTextureRegion = new TextureRegion(headTexture);
//        sb.draw(headTexture, headPosX, headPosY);
        sb.draw(headTextureRegion, headPosX, headPosY,
                size/2, size/2,
                headTexture.getWidth(), headTexture.getHeight(),
                1, 1, (float)currentDirection);
    }

    public void lengthenBody(int foodX, int foodY){
        SnakeBody newBodyPart = new SnakeBody(foodX, foodY);
        body.insert(0,newBodyPart); //insert at front of array - body of the snake
    }

    public void moveSnake(){
        double deviation = settingDirection - currentDirection;
        double deltaX;
        double deltaY;
        if(Math.abs(deviation) <= maxRotate){
            deltaX = step*Math.cos(settingDirection/180*Math.PI);
            deltaY = step*Math.sin(settingDirection/180*Math.PI);
            this.currentDirection = this.settingDirection;
        }else {
            if(deviation>0){
                if(deviation <= 180){
                    this.currentDirection = this.currentDirection+this.maxRotate;
                    if(this.currentDirection >= 360){
                        this.currentDirection -= 360;
                    }
                }else{
                    this.currentDirection = this.currentDirection-this.maxRotate;
                    if(this.currentDirection < 0){
                        this.currentDirection += 360;
                    }
                }

            }else{
                if(deviation >= -180){
                    this.currentDirection = this.currentDirection-this.maxRotate;
                    if(this.currentDirection < 0){
                        this.currentDirection += 360;
                    }
                }else{
                    this.currentDirection = this.currentDirection+this.maxRotate;
                    if(this.currentDirection >= 360){
                        this.currentDirection -= 360;
                    }
                }

            }
            deltaX = step*Math.cos(this.currentDirection/180*Math.PI);
            deltaY = step*Math.sin(this.currentDirection/180*Math.PI);
        }
        this.headPosX += deltaX;
        this.headPosY += deltaY;
    }

    public boolean checkEdge(){ //need to change
        if (headPosX + 50 + size >= myAm.mapsize){
            //headPosX = 0;
            return true;
        }
        if (headPosX - 50 <= 0){
            //headPosX = Gdx.graphics.getWidth() - SNAKE_MOVEMENT;
            return true;
        }
        if (headPosY + 50 + size >= myAm.mapsize){
            //headPosY = 0;
            return true;
        }
        if (headPosY - 50 <= 0){
            //headPosY = Gdx.graphics.getHeight() - SNAKE_MOVEMENT;
            return true;
        }
        return false;
    }

    public boolean checkSnakeBodyCollision(){
        for (SnakeBody bodypart : body){
            if (bodypart.x == headPosX && bodypart.y == headPosY)
                return true;
        }
        return false;
    }

    public void restartSnake(){
        body.clear();
        currentDirection = 0.0;
        headPosX = 0;
        headPosY = 0;
    }

    private void resizeBody(int size){

        pixmapOrigin = new Pixmap(Gdx.files.internal(myAm.getHeadAdress(myUsername)));

        newPixmap = new Pixmap(size, size, pixmapOrigin.getFormat());
        newPixmap.drawPixmap(pixmapOrigin,
                0, 0, pixmapOrigin.getWidth(), pixmapOrigin.getHeight(),
                0, 0, newPixmap.getWidth(), newPixmap.getHeight()
        );
        headTexture = new Texture(newPixmap);

        pixmapOrigin = new Pixmap(Gdx.files.internal(myAm.getBodyAdress(myUsername)));
        newPixmap = new Pixmap(size, size, pixmapOrigin.getFormat());
        newPixmap.drawPixmap(pixmapOrigin,
                0, 0, pixmapOrigin.getWidth(), pixmapOrigin.getHeight(),
                0, 0, newPixmap.getWidth(), newPixmap.getHeight()
        );
        bodyTexture = new Texture(newPixmap);

    }

    public ArrayList<Array<Integer>> getDeadSnake(){
        ArrayList<Array<Integer>> deadSnake = new ArrayList<Array<Integer>>();
        Array<Integer> headPos = new Array<Integer>();
        headPos.add(headPosX);
        headPos.add(headPosX);
        deadSnake.add(headPos);
        for (SnakeBody sb: body){
            Array<Integer> nodePos = new Array<Integer>();
            nodePos.add(sb.x+size/2);
            nodePos.add(sb.y+size/2);
            deadSnake.add(nodePos);
        }
        return deadSnake;
    }


    @Override
    public void dispose() {
        //headTexture.dispose();
        pixmapOrigin.dispose();
        newPixmap.dispose();
//        bodyTexture.dispose();
    }

    /**--Setters and Getters--**/

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void updateSize(){
        size = initSize + body.size;
        resizeBody(size);
        if(yellowAppleTimer > 0){
            yellowAppleTimer--;
        }
    }

    public int getScore() {
        return body.size-3;
    }

    public void updateScore(){
        myAm.addScore(myUsername);
    }

    public double getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(double currentDirection) {
        this.currentDirection = currentDirection;
    }

    public double getSettingDirection() {
        return settingDirection;
    }

    public void setSettingDirection(double settingDirection) {
        this.settingDirection = settingDirection;
    }

    public int getHeadPosX() {
        return headPosX;
    }

    public int getHeadPosY() {
        return headPosY;
    }

    public void setHeadPosX(int i) {
        this.headPosX = i;
    }

    public void setHeadPosY(int i) {
        this.headPosY = i;
    }

    public Array<SnakeBody> getBody() {
        return body;
    }

    public String getMyUsername() {
        return myUsername;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getYellowAppleTimer() {
        return yellowAppleTimer;
    }

    public void setYellowAppleTimer(int yellowAppleTimer) {
        this.yellowAppleTimer = yellowAppleTimer;
    }
}
