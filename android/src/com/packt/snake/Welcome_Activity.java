package com.packt.snake;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Welcome_Activity extends AppCompatActivity {
    private Button startButton, multiButton,submitButton,settingButton;
    private SocketConnect myConnect;
    //AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
    //AlertDialog alert;

    private boolean waiting = false;
    private String alertMessage = "";
    private int connectResponse;
    private int skin;
    private int controlMode;
    private boolean adsOff;
    private String username;
    private String ipAddr;
    private MyAssetsManager myAm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_);
        myConnect = SocketConnect.getnew(this);
        myAm = myConnect.getMyAm();

        Intent extraIntent = getIntent();
        Bundle extraBundle = extraIntent.getExtras();
        try {
            if (!extraBundle.isEmpty()) {

                skin = extraBundle.getInt("skin");
                controlMode = extraBundle.getInt("control");
                adsOff = extraBundle.getBoolean("adsoff");
                username = extraBundle.getString("name");
                ipAddr = extraBundle.getString("ipAddr");
                if (skin > 0){
                    myAm.updateSetting(skin, controlMode, adsOff, ipAddr);
                    myAm.myUsername = username;
                    System.out.println("***********read bundle");
                    myConnect.saveData();
                }

            }
        } catch (Exception e){
            //bundle not exist, pass
        }

        getSupportActionBar().hide();
        initiateUI();

    }

    private void initiateUI(){

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome_Activity.this, AndroidLauncher.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode","single");
                intent.putExtras(bundle);
                myConnect.dumpSingleData();
                startActivity(intent);
                Welcome_Activity.this.finish();
            }
        });


        multiButton = findViewById(R.id.multi_button);
        multiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("===========quit = "+myConnect.quit);
                myConnect.setQuit(false);
                new Join().execute();
                //while(true){ alert.setMessage("Connection Failed"); }
            }
        });

        settingButton = findViewById(R.id.setting_button);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome_Activity.this, Setting_Activity.class);
                startActivity(intent);
                Welcome_Activity.this.finish();
            }
        });


    }

    class Join extends AsyncTask<String,Void,Integer>{
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Welcome_Activity.this);
        AlertDialog alert;
        CountDownTimer MyTimer;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Integer doInBackground(String... strings) {


            int response = myConnect.initConnect();
            System.out.println("doinbackground "+response);
            return response;
        }
        @Override
        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);
            //alert.cancel();
            MyTimer = new CountDownTimer(1000000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    alert.setMessage(myConnect.getUserlist());
                    //System.out.println("00:"+ (millisUntilFinished/1000));
                }

                @Override
                public void onFinish() {
                    //info.setVisibility(View.GONE);
                }
            };
            mBuilder.setTitle("Waiting for join").setMessage("")
                    .setPositiveButton("Play", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Welcome_Activity.this, AndroidLauncher.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("mode","multi");
                            intent.putExtras(bundle);
                            startActivity(intent);

                            System.out.println("===========quit = "+myConnect.quit);
                            myConnect.setWaiting(true);
                            myConnect.dumpMultiData();
                            //myConnect.dumpSingleData();
                            Welcome_Activity.this.finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyTimer.cancel();
                            myConnect.setQuit(true);
                            System.out.println("===========cancel is triggerd");
                            myConnect = SocketConnect.getnew(Welcome_Activity.this);
                            myAm = myConnect.getMyAm();
                            if (skin != 0){
                                myAm.updateSetting(skin,controlMode,adsOff,ipAddr);
                                myAm.myUsername = username;
                                myConnect.saveData();
                            }

                            dialog.dismiss();
                            //myConnect.renewSocket();
                            //myConnect = SocketConnect.get();

                        }
                    }).setCancelable(false);
            alert = mBuilder.create();
            alert.show();
            //System.out.println("dialog show");
            //alert.setMessage(myConnect.getUserlist());

            if (response == 1){
                //alertMessage = ;
                System.out.println("response = 1");
                alert.setMessage(myConnect.getUserlist());
                alertMessage = myConnect.getUserlist();
                //myConnect.setWaiting(true);
                System.out.println("THREAD IS ALIGE?"+myConnect.isAlive());
                myConnect.start();
                MyTimer.start();
                System.out.println("thread started");
            } else if (response == 2){
                alert.setMessage("Username duplicate");
            } else if (response == 3){
                alert.setMessage("Game is ongoing, failed to join the room");
            } else {
                //alertMessage = "Connection Failed";
                alert.setMessage("Connection Failed");
                System.out.println("response != 1");
            }


        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //do nothing
    }

    @Override
    protected void onStop() {
        //System.out.println("on stop is called!");
        //myConnect.setQuit(true);
        super.onStop();
    }

}
