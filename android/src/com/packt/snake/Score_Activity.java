package com.packt.snake;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.widget.ShareDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.Map;

public class Score_Activity extends AppCompatActivity {
    private Button shareButton, restartButton, shareImageButton;
    private TextView scoreText;
    private MyAssetsManager myAm;
    private SocketConnect myConnect;
    private String score = "";
    private ImageView imageView;
    CallbackManager callbackManager;
    ShareDialog shareDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplication());
        setContentView(R.layout.activity_score_);

        myConnect = SocketConnect.get();
        myAm = myConnect.getMyAm();
        myConnect.quit = true;

        getSupportActionBar().hide();

        scoreText = findViewById(R.id.score_text);
        for (Map.Entry<String, int[]> entry : myAm.userdata.entrySet()) {
            score = score + entry.getKey() + "\t" + entry.getValue()[2] + "\n";
        }
        scoreText.setText(score);

        if (!myAm.adsOff) {
            Intent intent = new Intent(Score_Activity.this, AdvertiseActivity.class);
            startActivity(intent);
        }
        setupButtons();
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }


    private void setupButtons() {
        //Share Button (to Facebook, Email.. Etc)
        imageView = (ImageView) findViewById(R.id.imageView);
        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAm.incrementSkin();//for unlocking more skin
                myConnect.saveData();
                File file = capture(imageView);
                Uri imageUri = Uri.fromFile(file);
                if(Build.VERSION.SDK_INT>=24){
                    try{
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent, "Share Image"));

            }

        });
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        //Screenshot Button (Take screenshots and save it to local album)
        shareImageButton = findViewById(R.id.share_image_button);
        shareImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                save_screenshot(imageView);

            }
        });

        //restart game
        restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myAm.resetData();

                myAm.manager.clear();
                //myConnect.renewSocket();
                Intent intent = new Intent(Score_Activity.this, Welcome_Activity.class);
                startActivity(intent);
                Score_Activity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //do nothing
    }


    public File capture(View v) {
        ActivityCompat.requestPermissions(Score_Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        View view = v.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        File file = null;
        try {
            File exDir = Environment.getExternalStorageDirectory();
            String filename = "slither_" + System.currentTimeMillis() + ".png";
            File folder = new File(exDir, "Slither");
            if (!folder.exists()) {
                folder.mkdir();
            }
            file = new File(folder.getPath(), filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            file.setWritable(Boolean.TRUE);
            FileOutputStream out = new FileOutputStream(file);
            //FileOutputStream fileOutputStream=new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(this, "Capture Succeed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Capture Failed", Toast.LENGTH_SHORT).show();
        }

        return file;
    }


    public void save_screenshot(View v) {
        ActivityCompat.requestPermissions(Score_Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        View view = v.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        File file = null;
        try {
            File exDir = Environment.getExternalStorageDirectory();
            String filename = "slither_" + System.currentTimeMillis() + ".png";
            File folder = new File(exDir, "Slither");
            if (!folder.exists()) {
                folder.mkdir();
            }
            file = new File(folder.getPath(), filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            file.setWritable(Boolean.TRUE);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
            Toast.makeText(this, "Screenshot Saved to Phone", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Screenshot Failed to save on Phone", Toast.LENGTH_SHORT).show();
        }


    }
}




