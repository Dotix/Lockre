package com.brainixdev.lokre.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.com.sapereaude.maskedEditText.MaskedEditText;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brainixdev.lokre.R;
import com.brainixdev.lokre.Utils.TacheAsync;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class CodeVerificationActivity extends AppCompatActivity implements TacheAsync.Listeners, TextWatcher {

    private String numero, mVerificationId, pays;

    private MaskedEditText code;
    private ProgressBar progress;
    com.google.android.material.button.MaterialButton bt;
    private CountDownTimer cd;
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private PhoneAuthProvider.ForceResendingToken mToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean("verificationProgress",false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("verificationProgress",mVerificationInProgress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        mAuth = FirebaseAuth.getInstance();

        final TextView tv_num, tv_change, tv_resend, tv_count;

        tv_num = findViewById(R.id.tv_bad_num);
        tv_change = findViewById(R.id.tv_change_num);
        tv_resend = findViewById(R.id.tv_resend_code);
        tv_resend.setEnabled(false);
        tv_count = findViewById(R.id.countdown);
        numero = getIntent().getStringExtra("numero");
        pays = getIntent().getStringExtra("pays");
        code = findViewById(R.id.et_code);
        code.addTextChangedListener(this);
        bt = findViewById(R.id.bt_valider);
        progress = findViewById(R.id.progress_code);

        cd = new CountDownTimer(120*1000,1000) {
            @Override
            public void onTick(long l) {
                tv_count.setText(String.format("%02d:%02d",(l/60000)%60,(l/1000)%60));
            }

            @Override
            public void onFinish() {
                tv_resend.setEnabled(true);
            }
        };

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                mVerificationInProgress = false;
                mAuth.signInWithCredential(phoneAuthCredential);
                onPostExecute("VALIDE");
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                mVerificationInProgress = false;
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mToken = forceResendingToken;
                mVerificationId = s;
                mVerificationInProgress = true;
            }

        };

        tv_num.setText(getString(R.string.bad_num,numero));
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CodeVerificationActivity.this,NumberVerificationActivity.class));
                finish();
            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cd.start();
                // Renvoi du code avec serveur sms
                    //executer(numero,"renvoi",null);
                resendCode(numero,mToken);
                onPostExecute("OK");
                tv_resend.setEnabled(false);
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(code.getRawText().isEmpty())
                    Toast.makeText(CodeVerificationActivity.this,"Veuillez entrez un code",Toast.LENGTH_SHORT).show();
                else if(code.getRawText().length()<6)
                    Toast.makeText(CodeVerificationActivity.this,"Le code doit faire six (06) chiffres",Toast.LENGTH_SHORT).show();
                else {
                    // Authentification avec serveur sms
                        //executer(numero,"validation",code.getRawText());
                    // Authentification firebase
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code.getRawText());
                    mAuth.signInWithCredential(credential).addOnCompleteListener(CodeVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent it = new Intent(CodeVerificationActivity.this,NewProfilActivity.class);
                                it.putExtra("numero",numero);
                                it.putExtra("pays",pays);
                                startActivity(it);
                                finish();
                            }else
                                Toast.makeText(CodeVerificationActivity.this,"Code invalide",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!mVerificationInProgress){
            sendCode(numero);
            cd.start();
        }
    }

    private void executer(String numero, String type, String code){
        new TacheAsync(this).execute(numero,type,code);
    }

    @Override
    public void onPreExecute() {
        progress.setVisibility(View.VISIBLE);
        Toast.makeText(CodeVerificationActivity.this,"Connexion ...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doInBackground() {
    }

    @Override
    public void onPostExecute(String result) {
        progress.setVisibility(View.GONE);
        if(result.compareTo("OK") == 0 )
            Toast.makeText(CodeVerificationActivity.this,"Le code vous a été renvoyé !",Toast.LENGTH_SHORT).show();
        else if(result.compareTo("VALIDE") == 0 )
            startActivity(new Intent(this,NewProfilActivity.class).putExtra("numero",numero).putExtra("pays",pays));
        else
            Toast.makeText(CodeVerificationActivity.this,"Une erreur s'est produite : \n"
                    + result + "\nVeuillez réessayer !",Toast.LENGTH_SHORT).show();
    }

    private void sendCode(String number){

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(number)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setCallbacks(mCallBacks)
                .setActivity(this)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendCode(String number,PhoneAuthProvider.ForceResendingToken token){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(number)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setCallbacks(mCallBacks)
                .setActivity(this)
                .setForceResendingToken(token)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.length() == 6 )
            bt.performClick();
    }
}