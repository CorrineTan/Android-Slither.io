package com.packt.snake;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class SocketConnect extends Thread {
    private static SocketConnect myConnect;

    //private String ipAdress = "192.168.43.228";
    private String ipAdress = "10.13.63.245";

    private int port = 8080;
    private DataInputStream input;
    private DataOutputStream output;
    public boolean quit = false;

    private Socket mSocket;
    private MyAssetsManager myAm = new MyAssetsManager();
    private String userlist = "", skinlist = "";
    private ArrayList<String> userarray = new ArrayList<String>();
    private ArrayList<Integer> skinarray = new ArrayList<Integer>();
    private boolean waiting = false;
    private Context mContext;

    private SocketConnect(Context context){
        mContext = context;
        SharedPreferences sp1 = mContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        int kk = sp1.getInt("skin",1);
        System.out.println("**************retrive kk = "+kk);
        if (kk > 0)
            myAm.myColor = kk;
        else
            myAm.myColor = 1;
        int kk2 = sp1.getInt("control",1);
        if (kk2 > 0)
            myAm.controlMode = kk2;
        else
            myAm.controlMode = 1;
        myAm.numberOfSkin = sp1.getInt("skinNumber",1);
        myAm.adsOff = sp1.getBoolean("adsOff",false);
        myAm.myUsername = sp1.getString("username","player1");
        myAm.ipAdress = sp1.getString("ipAddr","");
        System.out.println("******************retrive name = "+myAm.myUsername);
        System.out.println("******************retrive skin = "+myAm.myColor);
        System.out.println("******************retrive ip = "+myAm.ipAdress);

//        ipAdress = myAm.ipAdress;
    }

    public static SocketConnect getnew(Context context){
        myConnect = new SocketConnect(context);
        return myConnect;
    }
    public static SocketConnect get(){
       // if (myConnect == null){ myConnect = new SocketConnect(); }
        return myConnect;
    }

    /*
    public void renewSocket(){
        //quit = true;
        myConnect = new SocketConnect();
    }
    */
    public void saveData(){
        SharedPreferences sp1 = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sp1.edit();
        editor1.putInt("skin",myAm.myColor);
        editor1.putInt("control",myAm.controlMode);
        editor1.putBoolean("adsOff",myAm.adsOff);
        editor1.putString("username",myAm.myUsername);
        editor1.putInt("skinNumber",myAm.numberOfSkin);
        editor1.putString("ipAddr",myAm.ipAdress);
        editor1.apply();
        System.out.println("******************save name = "+myAm.myUsername);
    }

    public void setIpAdress(String ip, int port){
        ipAdress = ip;
        this.port = port;
    }


    public int initConnect(){
        try {
            ipAdress = myAm.ipAdress;
            System.out.println("create a new socket");
            System.out.println(myAm.ipAdress);
            mSocket = new Socket(ipAdress, port);
            input = new DataInputStream(mSocket.getInputStream());
            output = new DataOutputStream(mSocket.getOutputStream());
            //mSocket = new Socket(ipAdress, port);
            //System.out.println("connected");

            System.out.println("use Current socket");
            JSONObject join = new JSONObject();
            join.put("type", "askjoin");
            join.put("username", myAm.myUsername);
            join.put("skin",myAm.myColor);
            output.writeUTF(join.toString());
            String message = input.readUTF();
            System.out.println("response = "+message);
            JSONObject respond = new JSONObject(message);

            if (respond.getString("type").equals("join")){
                if (respond.getBoolean("agree")){
                    Log.d("MYTAG","initConnect"+respond.getString("usernamelist"));
                    //myAm.userlist = respond.getString("usernamelist");
                    userlist = userlist + respond.getString("usernamelist");
                    String[] x = userlist.split("\n");
                    for (String i : x){
                        userarray.add(i);
                    }
                    skinlist = respond.getString("skinlist");
                    String[] y = skinlist.split(" ");
                    for (String i : y){
                        skinarray.add(Integer.parseInt(i));
                    }
                    //userarray.add(respond.getString("usernamelist"));
                    return 1;
                } else{
                    return  respond.getInt("reason");
                    //2 - duplicate; 3 - on play
                }
            } else return 4;//other message, ignore


        } catch (IOException e) {
            Log.e("MYTAG","initConnect ERROR",e);
            return 5; //3 - connection failed
        } catch (Exception ex){
            Log.e("MYTAG","initConnect ERROR",ex);
            return 5;
        }
    }


    @Override
    public void run(){
        try {
            while (!mSocket.isClosed()) {
                if (input.available() > 0){
                    System.out.println("test running");
                    String message = input.readUTF();
                    System.out.println("thread receive msg: "+message);
                    JSONObject respond = new JSONObject(message);
                    String mtype = respond.getString("type");
                    switch (mtype){
                        case "move":
                            //int[] param = myAm.userdata.get(respond.getString("username"));
                            //param[3] = respond.getInt("direction");
                            //myAm.userdata.put(respond.getString("username"),param);
                            myAm.userdata.get(respond.getString("username"))[3] = respond.getInt("direction");
                            break;
                        case "speedup":
                            myAm.speedChanger(respond.getString("username"));
                            break;
                        case "join":
                            userlist = userlist + respond.getString("newuser")+"\n";
                            userarray.add(respond.getString("newuser"));
                            skinarray.add(respond.getInt("skin"));
                            break;
                        case "ready":
                            System.out.println("===========socekt receive: ready");
                            myAm.play = true;
                            break;
                        case "quit":
                            int n = userarray.indexOf(respond.get("username"));
                            userarray.remove(n);
                            skinarray.remove(n);
                            userlist = "";
                            for (String x: userarray){
                                userlist = userlist+x+"\n";
                            }
                            System.out.println("someone quit "+ userlist);
                            break;
                    }
                }
                if (quit){
                    //sendQuitRequest();
                    JSONObject m = new JSONObject();
                    m.put("type", "quit");
                    output.writeUTF(m.toString());
                    System.out.println("quit!!");
                    quit = false;
                    System.out.println("===========quit2 = "+myConnect.quit);
                    userlist = "";
                    skinlist = "";
                    break;
                } if (waiting){
                    //sendQuitRequest();
                    System.out.println("========ready");
                    JSONObject m = new JSONObject();
                    m.put("type", "ready");
                    output.writeUTF(m.toString());
                    //System.out.println("========ready");
                    waiting = false;
                } if (myAm.newDirection){
                    JSONObject m = new JSONObject();
                    m.put("type", "move");
                    m.put("direction",myAm.direction);
                    m.put("username",myAm.myUsername);
                    output.writeUTF(m.toString());
                    System.out.println("send move");
                    myAm.newDirection = false;
                } if (myAm.speedupchange){
                    JSONObject m = new JSONObject();
                    m.put("type","speedup");
                    m.put("username",myAm.myUsername);
                    output.writeUTF(m.toString());
                    System.out.println("send speedup change");
                    myAm.speedupchange = false;
                }
            }

            input.close();
            output.close();
            mSocket.close();
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("MYTAG","socket thread ERROR",e);
            myAm.serverError = true;
            System.err.println(e.getMessage());
            System.out.println("server error2");

        }
            System.out.println("============break success");

    }

    public String getUserlist() {
        return userlist;
    }

    public String getSkinlist() {
        return skinlist;
    }


    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    public MyAssetsManager getMyAm() {
        return myAm;
    }

    public void dumpSingleData(){
        int[] param = new int[]{1,1,0,0,0};
        param[0] = myAm.myColor;

        int[] param2 = new int[]{1,1,0,0,0};
        if (myAm.myColor + 1 > 6)
            param2[0] = myAm.myColor - 1;
        else param2[0] = myAm.myColor + 1;
        myAm.userdata.put(myAm.myUsername,param);
        myAm.userdata.put("AI",param2);
    }

    public void dumpMultiData(){  //put final data to star the game to asset manager
        myAm.userlist = userlist.split("\n");
        for (int i = 0; i < userarray.size(); i++){
            int[] param = new int[]{1,1,0,0,0};//[skin,speed,score,length,ready]
            param[0] = skinarray.get(i);
            //System.out.println("====================user" + x+" is dumped");
            myAm.userdata.put(userarray.get(i),param);
        }
    }

    public ArrayList<String> getUserarray() {
        return userarray;
    }

}
