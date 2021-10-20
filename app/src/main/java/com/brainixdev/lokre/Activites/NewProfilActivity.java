package com.brainixdev.lokre.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.brainixdev.lokre.R;
import com.brainixdev.lokre.Utils.RecherchePays;
import com.brainixdev.lokre.Utils.Utilisateur;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class NewProfilActivity extends AppCompatActivity {

    private com.google.android.material.textfield.TextInputEditText et_pseudo, et_nom_prenom, et_num2;
    private Spinner sp;
    private Utilisateur user;
    private FirebaseDatabase root;
    private DatabaseReference ref;
    private StorageReference storeRef;
    private ImageView iv_user;
    private Uri profilPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profil);

        SharedPreferences shp = getBaseContext().getSharedPreferences("app_use",MODE_PRIVATE);
        final SharedPreferences.Editor edit = shp.edit();

        final String numero = getIntent().getStringExtra("numero");
        final String pays = getIntent().getStringExtra("pays");

        et_pseudo = findViewById(R.id.et_pseudo);
        et_nom_prenom = findViewById(R.id.et_nom_prenom);
        et_num2 = findViewById(R.id.et_2nd_numero);

        if(shp.getString("userData","") == null)
            fillFields(shp.getString("userData",""));
        else{
            assert numero != null;
            et_pseudo.setText(getString(R.string.default_pseudo,numero.substring(1)));
            user = new Utilisateur(numero, pays);
            Gson gson = new Gson();
            edit.putString("userData",gson.toJson(user));
            edit.apply();
        }
        sp = findViewById(R.id.sp_ville);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(NewProfilActivity.this, R.layout.support_simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        RecherchePays.recupererVille(NewProfilActivity.this, new RecherchePays.mCall() {
            @Override
            public void onCallBack(RecherchePays.ListePays val) {

            }

            @Override
            public void onLoad(RecherchePays.ListePays val) {

            }

            @Override
            public void onVilleLoad(ArrayList<String> ville) {
                adapter.addAll(ville);
                adapter.notifyDataSetChanged();
            }
        },pays);

        com.google.android.material.button.MaterialButton bt_continuer = findViewById(R.id.bt_continuer_profil);
        iv_user = findViewById(R.id.user_profile_image);
        ImageView iv_cam = findViewById(R.id.user_cam_image);
        Glide.with(this).load(R.drawable.ic_user_profile).into(iv_user);
        Glide.with(this).load(R.drawable.ic_cam).into(iv_cam);

        bt_continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.requireNonNull(et_nom_prenom.getText()).toString().isEmpty() || !Objects.requireNonNull(et_nom_prenom.getText()).toString().contains(" "))
                    Toast.makeText(NewProfilActivity.this,"Veuillez entrer vos noms et prénom(s)",Toast.LENGTH_SHORT).show();
                else {
                    root = FirebaseDatabase.getInstance();
                    ref = root.getReference("Users");
                    storeRef = FirebaseStorage.getInstance().getReference().child("users_profils_pics/" + FirebaseAuth.getInstance().getUid() + "profil_pic");
                    if(profilPic != null) {
                        UploadTask upload = storeRef.putFile(profilPic);


                        Task<Uri> getUrl = upload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful())
                                    throw Objects.requireNonNull(task.getException());

                                return storeRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri profilPicUrl = task.getResult();
                                    user = new Utilisateur(et_nom_prenom.getText().toString().substring(0, et_nom_prenom.getText().toString().indexOf(" ")),
                                            et_nom_prenom.getText().toString().substring(et_nom_prenom.getText().toString().indexOf(" ") + 1),
                                            Objects.requireNonNull(et_pseudo.getText()).toString(), Objects.requireNonNull(profilPicUrl).toString(),
                                            sp.getSelectedItem().toString(),
                                            pays, numero,
                                            Objects.requireNonNull(et_num2.getText()).toString());
                                } else {
                                    user = new Utilisateur(et_nom_prenom.getText().toString().substring(0, et_nom_prenom.getText().toString().indexOf(" ")),
                                            et_nom_prenom.getText().toString().substring(et_nom_prenom.getText().toString().indexOf(" ") + 1),
                                            Objects.requireNonNull(et_pseudo.getText()).toString(), null,
                                            sp.getSelectedItem().toString(),
                                            pays, numero,
                                            Objects.requireNonNull(et_num2.getText()).toString());
                                }
                                ref.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("Identity").setValue(user);
                                finish();
                            }
                        });
                    }else {
                        user = new Utilisateur(et_nom_prenom.getText().toString().substring(0, et_nom_prenom.getText().toString().indexOf(" ")),
                                et_nom_prenom.getText().toString().substring(et_nom_prenom.getText().toString().indexOf(" ") + 1),
                                Objects.requireNonNull(et_pseudo.getText()).toString(), null,
                                sp.getSelectedItem().toString(),
                                pays, numero,
                                Objects.requireNonNull(et_num2.getText()).toString());
                        ref.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("Identity").setValue(user);
                        finish();
                    }

                    edit.putBoolean(getString(R.string.profil_key),true);
                    Gson gson = new Gson();
                    edit.putString("userData",gson.toJson(user));
                    edit.apply();
                    startActivity(new Intent(NewProfilActivity.this,CGUActivity.class));
                }
            }
        });

        iv_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfilPic();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200)
            if(resultCode == RESULT_OK){
                assert data != null;
                profilPic = data.getData();
                Glide.with(this).load(profilPic).apply(RequestOptions.circleCropTransform()).into(iv_user);
            }
    }

    @SuppressLint("SetTextI18n")
    void fillFields(String json){
        Gson gson = new Gson();
        user = gson.fromJson(json,Utilisateur.class);
        et_pseudo.setText(getString(R.string.default_pseudo,user.getUser_first_number().substring(1)));
        et_nom_prenom.setText(user.getUser_last_name() + " " + user.getUser_first_name());
        et_num2.setText(user.getUser_second_number());
    }

    @AfterPermissionGranted(100)
    void setProfilPic(){
        if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(it,200);
        }
        else
            EasyPermissions.requestPermissions(this,"Accès aux fichiers",100,Manifest.permission.READ_EXTERNAL_STORAGE);
            //Toast.makeText(this,"Vous devez accorder l'accès à vos fichiers",Toast.LENGTH_SHORT).show();
    }
}