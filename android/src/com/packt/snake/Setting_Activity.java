package com.packt.snake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class Setting_Activity extends AppCompatActivity {
    private Button backbutton,submitButton;
    private EditText usernameText;
    private EditText ipText;
    private String username = "player";
    private RadioGroup skins;
    private RadioGroup controls;
    private RadioGroup aimode;
    private Switch adsswitch;
    private int skinno = 1;
    private int controlno = 1;
    private int aimodeno =1;
    private boolean adsOff = false;
    private SocketConnect myConnect;
    private MyAssetsManager myAm;
    private int numOfSkins = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);
        getSupportActionBar().hide();
        myConnect = SocketConnect.get();
        myAm = myConnect.getMyAm();

        adsswitch = findViewById(R.id.switch_noads);
        adsswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                adsOff = isChecked;
            }
        });


        backbutton = findViewById(R.id.back_botton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //myAm.updateSetting(skinno,controlno,adsOff);
                //myAm.myUsername = usernameText.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putInt("skin",skinno);
                bundle.putInt("control",controlno);
                bundle.putBoolean("adsoff",adsOff);
//                System.out.println("asdasdasdasdasdasd"+ipText.getText().toString());
//                myAm.ipAdress = ipText.getText().toString();
                bundle.putString("ipAddr",ipText.getText().toString());
                if (usernameText.getText().toString().equals(""))
                    bundle.putString("name","player");
                else
                    bundle.putString("name",usernameText.getText().toString());
//                bundle.putInt("numOfSkins",numOfSkins);
                Intent intent = new Intent(Setting_Activity.this,Welcome_Activity.class);
                intent.putExtras(bundle);

                startActivity(intent);
                Setting_Activity.this.finish();
            }
        });

        usernameText = findViewById(R.id.text_name);
        System.out.println("*******SETTING username = "+myAm.myUsername);
//        System.out.println("*******SETTING username2 = "+usernameText);
        usernameText.setText(myAm.myUsername);

        ipText = findViewById(R.id.text_ip);
        System.out.println("*******SETTING ipAddr = "+myAm.ipAdress);
        ipText.setText(myAm.ipAdress);


        skins = findViewById(R.id.skingroup);
        skins.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_Skin1:
                        skinno = 1;
                        break;
                    case R.id.radio_Skin2:
                        skinno = 2;
                        break;
                    case R.id.radio_Skin3:
                        skinno = 3;
                        break;
                    case R.id.radio_Skin4:
                        skinno = 4;
                        break;
                    case R.id.radio_Skin5:
                        skinno = 5;
                        break;
                    case R.id.radio_Skin6:
                        skinno = 6;
                        break;
                    default:
                        skinno = 1;
                        break;
                }
            }

        });

        for (int i = 0; i < 6; i++) {
            skins.getChildAt(i).setEnabled(false);
        }
        if(myAm.numberOfSkin > 6){
            myAm.numberOfSkin = 6;
        }
        for (int i = 0; i <myAm.numberOfSkin; i++) {
            skins.getChildAt(i).setEnabled(true);
        }

        controls = findViewById(R.id.controlgroup);
        controls.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_gravity:
                        controlno = 3;
                        break;
                    case R.id.radio_handle:
                        controlno = 2;
                        break;
                    case R.id.radio_touch:
                        controlno = 1;
                        break;
                    default:
                        controlno = 1;
                        break;
                }
            }

        });

        aimode = findViewById(R.id.aigroup);
        aimode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_simpleai:
                        aimodeno = 1;
                        break;
                    case R.id.radio_hardai:
                        aimodeno = 2;
                        break;
                    default:
                        controlno = 1;
                        break;
                }
            }

        });





    }


}
