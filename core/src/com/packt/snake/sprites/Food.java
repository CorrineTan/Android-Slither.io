package com.packt.snake.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.packt.snake.MyAssetsManager;
import com.packt.snake.SnakeGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Food implements Disposable{
    private Texture food,background;
//    private ArrayList<int[]> foodlist = new ArrayList<int[]>();
    private ArrayList<SpeFood> foodlist = new ArrayList<SpeFood>();
    private static final int padding = 500;
    private java.util.Random randomX = new java.util.Random(250);
    private java.util.Random randomY = new java.util.Random(3800);
    private java.util.Random randomGlod = new java.util.Random(800);
    private int maxWidth;
    private int maxHeigh;
    private SnakeGame mygame;
    private MyAssetsManager myAm;

    public Food (SnakeGame game){
        mygame = game;
        myAm = mygame.getAm();
        myAm.loadMap();
        myAm.manager.finishLoading();
        background = myAm.manager.get(myAm.MAP1);
        maxWidth = background.getWidth() - 2*padding;
        maxHeigh = background.getHeight() - 2*padding;
        food = new Texture("apple.png");
//        System.out.println("x: "+maxWidth);
//        System.out.println("y: "+maxHeigh);
        placeFood();
    }

    public class SpeFood{
        private String type;
        private Texture texture;
        private int posX;
        private int posY;

        public SpeFood(String type, int posX, int posY) {
            this.type = type;
            this.posX = posX;
            this.posY = posY;

            if(type.equals("Normal")){
                texture = new Texture("apple.png");
            }else if(type.equals("RevDirection")){
                texture = new Texture("greenapple.png");
            }else if(type.equals("SpeedUp")){
                texture = new Texture("yellowapple.png");
            }
        }

        public Texture getTexture() {
            return texture;
        }

        public int getPosX() {
            return posX;
        }

        public int getPosY() {
            return posY;
        }

        public String getType() {
            return type;
        }
    }

    public void placeFood(){ //randomly put food on screen
        if (foodlist.size()<30){ //no food no screen
            do{
                //need to change to unified canvas size
                int foodX = padding+randomX.nextInt(maxWidth);
                int foodY = padding+randomY.nextInt(maxHeigh);
                //need a method to avoid food on snake!!
//                int[] x = {foodX,foodY};
                SpeFood temp;
                if(randomGlod.nextDouble() < 0.8){
                    temp = new SpeFood("Normal",foodX,foodY);
                }
                else{
                    temp = new SpeFood("SpeedUp",foodX,foodY);
                }
                foodlist.add(temp);
            }while (foodlist.size() < 30);
        }
    }

    public void placeFood(ArrayList<Array<Integer>> deadSnake){
        for(Array<Integer> pos : deadSnake){
            Random randomX = new Random();
            Random randomY = new Random();
            int[] foodPos = {pos.get(0)+randomX.nextInt(25),pos.get(1)+randomY.nextInt(25)};
            SpeFood temp = new SpeFood("Normal",foodPos[0],foodPos[1]);
            foodlist.add(temp);
//            foodlist.add(foodPos);
        }
    }

    public void placeFoodAt(int x, int y){
        int[] pos = {x,y};
//        foodlist.add(pos);
        SpeFood temp = new SpeFood("Normal",pos[0],pos[1]);
        foodlist.add(temp);
    }

    public ArrayList<int[]> getFoodlist() {
        ArrayList<int[]> temp = new ArrayList<int[]>();
        for(SpeFood x : foodlist){
            int[] cao = {x.getPosX(),x.getPosY()};
            temp.add(cao);
        }
        return temp;
    }

    public ArrayList<SpeFood> getFoodObj() {
        return foodlist;
    }

    public Texture getFood() {
        return food;
    }



//    public void removeFood(int[] headpostion){
//        arraylistRemove(foodlist,headpostion);
//    }
//
//    private ArrayList<int[]> arraylistRemove(ArrayList<int[]> al, int[] lst){
//        for (int[] x: al){
//            if (Arrays.equals(x,lst)){
//                al.remove(x);
//                return al;
//            }
//        }
//        return al;
//    }

    public void removeFood(SpeFood headpostion){
        arraylistRemove(foodlist,headpostion);
    }

    private ArrayList<SpeFood> arraylistRemove(ArrayList<SpeFood> al, SpeFood lst){
        for (SpeFood x: al){
            if (x.getPosX() == lst.getPosX() && x.getPosY() == lst.getPosY()){
                al.remove(x);
                return al;
            }
        }
        return al;
    }

    @Override
    public void dispose() {
        food.dispose();
    }
}
