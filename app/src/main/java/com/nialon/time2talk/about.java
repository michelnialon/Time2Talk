package com.nialon.time2talk;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class about extends AppCompatActivity
{
    TextView textTitre;
    TextView textAuth;
    TextView textDate;

    public void onCreate(Bundle savedInstanceState)
    {
        Button closeButton;

        // Pour enlever la barre de titre :
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        textTitre = findViewById(R.id.textTitre);
        textAuth = findViewById(R.id.textAuth);
        textDate =  findViewById(R.id.textDate);
        textTitre.setText(getString(R.string.app_name));
        textAuth.setText("Michel Nialon");
        textDate.setText("02/02/2020");

        /* Format title */
        /*
        TextView title = (TextView) findViewById(android.R.id.title);
        title.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        title.setTextColor(Color.YELLOW);
        title.setTextSize(20);
        */

        String versionName = "";
        PackageInfo packageInfo;
        try
        {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = "v " + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        TextView tv = findViewById(R.id.textVer);
        tv.setText(versionName);

        //Bouton OK, pas utilisé
        closeButton = this.findViewById(R.id.buttonOK);
        closeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

}
