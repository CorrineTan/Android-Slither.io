package com.packt.snake.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;

public class Radar {
    private int mapWeidth;
    private int mapHeight;
    private int radarLength = 150;
    private Texture radarTexture;
    private Pixmap pixmap;

    public Radar(int mapWeidth, int mapHeight) {
        this.mapWeidth = mapWeidth;
        this.mapHeight = mapHeight;
    }

    public void drawInitRadar(){
        pixmap = new Pixmap(radarLength,radarLength,Pixmap.Format.RGBA4444);
        pixmap.setColor(Color.BLACK);
//        pixmap.drawLine(0, 0, pixmap.getWidth() - 1, 0);
//        pixmap.drawLine(pixmap.getWidth() - 1, 0, pixmap.getWidth() - 1, pixmap.getHeight() - 1);
//        pixmap.drawLine(pixmap.getWidth() - 1, pixmap.getHeight() - 1,0, pixmap.getHeight() - 1);
//        pixmap.drawLine(0,pixmap.getHeight() - 1,0,0);
        pixmap.drawRectangle(0,0,radarLength,radarLength);
        pixmap.setColor(Color.GRAY);
        pixmap.fillRectangle(0,0,radarLength,radarLength);
    }

    private double relativeX(int snakeHeadX){
        return (snakeHeadX+1)/(double)mapWeidth*radarLength;
    }

    private double relativeY(int snakeHeadY){
        return (mapHeight-snakeHeadY)/(double)mapHeight*radarLength;
    }

    public void updateRadar(int snakeHeadX,int snakeHeadY,Food foods){
        drawInitRadar();

        pixmap.setColor(Color.RED);
        int headXInRadar = (int)relativeX(snakeHeadX);
        int headYInRadar = (int)relativeY(snakeHeadY);
//        pixmap.drawCircle(headXInRadar,headYInRadar,2);
        pixmap.fillCircle(headXInRadar,headYInRadar,2);

        pixmap.setColor(Color.WHITE);
        ArrayList<Integer> foodsPos = new ArrayList<Integer>();
        for(int[] food : foods.getFoodlist()){
            int foodXInRadar = (int)relativeX(food[0]);
            int foodYInRadar = (int)relativeY(food[1]);
//            pixmap.drawCircle(foodXInRadar,foodYInRadar,2);
            pixmap.fillCircle(foodXInRadar,foodYInRadar,2);
        }
        radarTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public Texture getRadarTexture() {
        return radarTexture;
    }

}
