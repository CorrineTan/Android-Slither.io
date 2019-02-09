package com.packt.snake;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAssetsManager{
    public final AssetManager manager = new AssetManager();

    //resource
    public final String SNAKEHEAD1 = "head1.png";
    public final String SNAKEBODY1 = "body1.png";
    public final String SNAKEHEAD2 = "head2.png";
    public final String SNAKEBODY2 = "body2.png";
    public final String SNAKEHEAD3 = "head3.png";
    public final String SNAKEBODY3 = "body3.png";
    public final String SNAKEHEAD4 = "head4.png";
    public final String SNAKEBODY4 = "body4.png";
    public final String SNAKEHEAD5 = "head5.png";
    public final String SNAKEBODY5 = "body5.png";
    public final String SNAKEHEAD6 = "head6.png";
    public final String SNAKEBODY6 = "body6.png";

    public final String MAP1 = "map5001.png";
    public final String SKIN = "skin/comic-ui.json";
    public final String SKIN2 = "skin2/flat-earth-ui.json";
    public final String SPEEDUP = "speedup.png";

    //parameters
    public static final int V_WIDTH = 960;
    public static final int V_HEIGHT = 520;
    private static final int POINTS_PER_FOOD = 10;
    public SpriteBatch batch;//only need one, allow all screen to acces
    //public int score = 0;
    //public SocketConnect myConnect;
    public Map<String,int[]> userdata = new HashMap<String,int[]>(); //<username, [skin,speed,score,direction,ready]>
    public String[] userlist;
    public int mapsize;
    public boolean play = false;
    //public SnakeGame game;
    public String mode = "single";
    public String myUsername = "player";
    public String ipAdress="0.0.0.0";
    public boolean newDirection = false;
    public int direction = 0;
    public boolean disconnect = false;
    public String disconnectP = "";
    public int controlMode = 1; //1 - touch; 2 - joystick; 3 - gravity
    public String myheadskin = SNAKEHEAD1, mybodyskin = SNAKEBODY1;
    public boolean serverError = false;
    public boolean adsOff = false;
    public int myColor = 1;
    public boolean speedupchange = false;
    public int numberOfSkin = 1;


    /////////////////resource loader//////////////////////

    //need to change: load specific texture based on player choice
    public void loadMap(){
        manager.load(MAP1, Texture.class);
    }

    public void loadSnake(int skincode){//load based on skincode
        switch (skincode){
            case 1:
                manager.load(SNAKEHEAD1, Texture.class);
                manager.load(SNAKEBODY1,Texture.class);
                myheadskin = SNAKEHEAD1;
                mybodyskin = SNAKEBODY1;
                break;
            case 2:
                manager.load(SNAKEHEAD2, Texture.class);
                manager.load(SNAKEBODY2,Texture.class);
                myheadskin = SNAKEHEAD2;
                mybodyskin = SNAKEBODY2;
                break;
            case 3:
                manager.load(SNAKEHEAD3, Texture.class);
                manager.load(SNAKEBODY3,Texture.class);
                myheadskin = SNAKEHEAD3;
                mybodyskin = SNAKEBODY3;
                break;
            case 4:
                manager.load(SNAKEHEAD4, Texture.class);
                manager.load(SNAKEBODY4,Texture.class);
                myheadskin = SNAKEHEAD4;
                mybodyskin = SNAKEBODY4;
                break;
            case 5:
                manager.load(SNAKEHEAD5, Texture.class);
                manager.load(SNAKEBODY5,Texture.class);
                //System.out.println("==============skin 5 is selected");
                myheadskin = SNAKEHEAD5;
                mybodyskin = SNAKEBODY5;
                break;
            case 6:
                manager.load(SNAKEHEAD6, Texture.class);
                manager.load(SNAKEBODY6,Texture.class);
                myheadskin = SNAKEHEAD6;
                mybodyskin = SNAKEBODY6;
                break;
            default:
                    manager.load(SNAKEHEAD1, Texture.class);
                    manager.load(SNAKEBODY1,Texture.class);
                    myheadskin = SNAKEHEAD1;
                    mybodyskin = SNAKEBODY1;

        }

    }

    public void loadSkin(){
        manager.load(SKIN, Skin.class);
        //manager.load(BACKGROUND1,Texture.class);
    }

    public void loadSkin2(){
        manager.load(SKIN2, Skin.class);
    }

    public void loadHubResource(){
        manager.load(SPEEDUP,Texture.class);
    }

    ////////////////parameter modifier////////////////
    public void addScore(String name){
        int[] param = userdata.get(name);
        param[2] = param[2]+POINTS_PER_FOOD;
        userdata.put(name,param);
        //System.out.println(name + "get score! "+param[2]);
    }

    public void reduceScore(String name){
        int[] param = userdata.get(name);
        param[2] = param[2]-POINTS_PER_FOOD;
        userdata.put(name,param);
    }

    public void setNewDirection(int newd){
        newDirection = true;
        //userdata.get(myUsername)[3] = newd;
        direction = newd;
    }

    public int getMyDirection(){
        return userdata.get(myUsername)[3];
    }

    public int getRemoteDirection(String name){
        return userdata.get(name)[3];
    }

    public int getvWidth() {
        return V_WIDTH;
    }

    public int getvHeight() {
        return V_HEIGHT;
    }

    public String getHeadAdress(String name){
        String addr = "";
        int skincode = userdata.get(name)[0];
        switch (skincode){
            case 1:
                addr = SNAKEHEAD1;break;
            case 2:
                addr = SNAKEHEAD2;break;
            case 3:
                addr = SNAKEHEAD3;break;
            case 4:
                addr = SNAKEHEAD4;break;
            case 5:
                addr = SNAKEHEAD5;break;
            case 6:
                addr = SNAKEHEAD6;break;
            default:
                addr = SNAKEHEAD1;
        }
        return addr;
    }

    public String getBodyAdress(String name){
        String addr = "";
        int skincode = userdata.get(name)[0];
        switch (skincode){
            case 1:
                addr = SNAKEBODY1;break;
            case 2:
                addr = SNAKEBODY2;break;
            case 3:
                addr = SNAKEBODY3;break;
            case 4:
                addr = SNAKEBODY4;break;
            case 5:
                addr = SNAKEBODY5;break;
            case 6:
                addr = SNAKEBODY6;break;
            default:
                addr = SNAKEBODY1;
        }
        return addr;
    }

    public void updateSetting(int color, int control, boolean ad, String ipAddr){
        myColor = color;
        controlMode = control;
        adsOff = ad;
        ipAdress = ipAddr;
    }

    public void speedChanger(String name){
        userdata.get(name)[1] *= -1;
    }

    public void incrementSkin(){
        if (numberOfSkin < 6)
            System.out.println("share"+numberOfSkin);
            numberOfSkin ++;
            System.out.println("share"+numberOfSkin);

    }
}
