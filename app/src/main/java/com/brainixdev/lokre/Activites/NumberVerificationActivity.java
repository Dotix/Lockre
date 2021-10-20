package com.brainixdev.lokre.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import br.com.sapereaude.maskedEditText.MaskedEditText;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.brainixdev.lokre.R;
import com.brainixdev.lokre.Utils.AdaptateurDrapeau;
import com.brainixdev.lokre.Utils.RecherchePays;
import com.brainixdev.lokre.Utils.TacheAsync;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.Objects;

public class NumberVerificationActivity extends AppCompatActivity implements TacheAsync.Listeners {

    private MaskedEditText numero;
    private String region, numeroF, pays;
    private EditText indicatif;
    private PhoneNumberUtil phoneUtil;
    private Phonenumber.PhoneNumber numero_format;
    private AdaptateurDrapeau adaptateur;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);

        progress = findViewById(R.id.progress_spin);
        Spinner choix_pays = findViewById(R.id.sp_indicatif);
        numero = findViewById(R.id.et_numero);
        com.google.android.material.button.MaterialButton bt_verif = findViewById(R.id.bt_verification);
        indicatif = findViewById(R.id.et_indicatif);
        indicatif.setEnabled(false);
        phoneUtil = PhoneNumberUtil.getInstance();

        numero.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.splash_text), PorterDuff.Mode.SRC_ATOP);
        indicatif.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.splash_text), PorterDuff.Mode.SRC_ATOP);
        choix_pays.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.splash_text), PorterDuff.Mode.SRC_ATOP);

        adaptateur = new AdaptateurDrapeau(NumberVerificationActivity.this);
        choix_pays.setAdapter(adaptateur);

       RecherchePays.recupererPays(NumberVerificationActivity.this, new RecherchePays.mCall() {
           @Override
           public void onCallBack(RecherchePays.ListePays val) {
               adaptateur.clear();
               adaptateur.addAll(val);
               adaptateur.notifyDataSetChanged();
           }

           @Override
           public void onLoad(RecherchePays.ListePays val) {
               adaptateur.addAll(val);
           }

           @Override
           public void onVilleLoad(ArrayList<String> ville) {

           }
       });

        choix_pays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    region = Objects.requireNonNull(adaptateur.getItem(i)).getIso();
                    pays = Objects.requireNonNull(adaptateur.getItem(i)).getNom();

                    int num_indicatif = phoneUtil.getExampleNumber(region).getCountryCode();
                    long exemple = phoneUtil.getExampleNumber(region).getNationalNumber();

                    numero_format = phoneUtil.parse(Long.toString(exemple),region);

                    String fomater = phoneUtil.format(numero_format, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

                    String hint = getString(R.string.mask_telephone,
                            fomater.substring(fomater.indexOf(" ")+1));
                    String mask = hint.replaceAll("[0-9]","#");

                    numero.setHint(hint.replaceAll(" ",""));
                    numero.setMask(mask);
                    indicatif.setText(getString(R.string.mask_ind,Integer.toString(num_indicatif)));

                } catch (NumberParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bt_verif.setOnClickListener(view -> verifyNumber());
    }

    private void executer(String numero_){
        // Authentification avec serveur sms
            //new TacheAsync(this).execute(numero,"requete");
        // Authentification firebase
            new TacheAsync(this).execute(numero_,"firebase");
    }

    @Override
    public void onPreExecute() {
        progress.setVisibility(View.VISIBLE);
        Toast.makeText(NumberVerificationActivity.this,"Connexion ...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doInBackground() {
    }

    @Override
    public void onPostExecute(String result) {
        progress.setVisibility(View.GONE);
        if(result.compareTo("OK") == 0 ) {
            Toast.makeText(NumberVerificationActivity.this, "Vous revevrez le code", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(NumberVerificationActivity.this, CodeVerificationActivity.class);
            it.putExtra("numero",numeroF);
            it.putExtra("pays",pays);
            startActivity(it);
            finish();
        }
        else
            Toast.makeText(NumberVerificationActivity.this,"Une erreur s'est produite : \n"
                    + result + "\nVeuillez réessayer !",Toast.LENGTH_SHORT).show();
    }

    public void verifyNumber(){
        try {
            numero_format = phoneUtil.parse(numero.getRawText(),region);
            if(phoneUtil.isValidNumber(numero_format) || phoneUtil.isPossibleNumber(numero_format))
            {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm != null && Objects.requireNonNull(cm.getActiveNetworkInfo()).isConnectedOrConnecting()) {
                    numeroF = indicatif.getText().toString().substring(1,indicatif.getText().toString().length()-1).concat(numero.getRawText());
                    executer(numeroF);
                } else if (cm != null && !cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
                    Toast.makeText(getApplicationContext(), R.string.connect_internet, Toast.LENGTH_LONG).show();
                }
            }
            else
                Toast.makeText(NumberVerificationActivity.this,R.string.wrong_phone_format,Toast.LENGTH_SHORT).show();
        } catch (NumberParseException e) {
            Toast.makeText(NumberVerificationActivity.this,R.string.invite_phone,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}