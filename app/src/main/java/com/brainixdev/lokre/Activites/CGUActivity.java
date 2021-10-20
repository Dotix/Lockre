package com.brainixdev.lokre.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.brainixdev.lokre.R;

public class CGUActivity extends AppCompatActivity {

    private CheckBox cb_agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_g_u);

        SharedPreferences sp = getBaseContext().getSharedPreferences("app_use",MODE_PRIVATE);
        final SharedPreferences.Editor edit = sp.edit();
        cb_agree = findViewById(R.id.cb_agree);
        Button bt_agree = findViewById(R.id.bt_accepter_cgu);

        bt_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb_agree.isChecked()) {
                    edit.putBoolean(getString(R.string.cgu_key),true);
                    edit.apply();
                    startActivity(new Intent(CGUActivity.this, SliderActivity.class));
                    finish();
                }
                else
                    Toast.makeText(CGUActivity.this,"Vous devez acceptez les conditions d'utilisation pour continuer",Toast.LENGTH_SHORT).show();
            }
        });
    }
}