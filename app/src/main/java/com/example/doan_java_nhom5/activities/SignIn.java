package com.example.doan_java_nhom5.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan_java_nhom5.R;
import com.example.doan_java_nhom5.databinding.ActivitySignInBinding;
import com.example.doan_java_nhom5.utilities.Constants;
import com.example.doan_java_nhom5.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getboolean(Constants.KEY_IS_SIGN_IN)){
            Intent intent= new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners(){
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUp.class)));
//        binding.buttonSignIn.setOnClickListener(v -> addDataToFireStore());

        binding.buttonSignIn.setOnClickListener(v -> {
            if(isValidSignIn())
            {
                signIn();

            }
        });
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.processBar.setVisibility(View.VISIBLE);

        } else {
            binding.processBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }
    private void signIn()
    {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL,binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful() && task.getResult() !=null &&task.getResult().getDocuments().size() > 0)
                   {
                       DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                       preferenceManager.putBoolean(Constants.KEY_IS_SIGN_IN,true);
                       preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.getId());
                       preferenceManager.putString(Constants.KEY_NAME,documentSnapshot.getString(Constants.KEY_NAME));
                       preferenceManager.putString(Constants.KEY_IMAGE,documentSnapshot.getString(Constants.KEY_IMAGE));
                      Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      startActivity(intent);
                   }else
                   {
                       loading(false);
                       showToast("Đăng nhậpa không thành công");
                   }
                });
    }
    private void showToast(String mesage){
        Toast.makeText(getApplicationContext(),mesage,Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignIn()
    {
        if(binding.inputEmail.getText().toString().trim().isEmpty())
        {
            showToast("Vui long nhập Email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Nhập đúng giá trị Email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("vui lòng nhập mật khẩu");
            return false;
        }else {
            return true;
        }
    }

//    private void addDataToFireStore(){
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        HashMap<String, Object> data = new HashMap<>();
//        data.put("First_name","Thiên");
//        data.put("Last_name","Đạo");
//        database.collection("users").add(data).addOnSuccessListener(documentReference -> {
//            Toast.makeText(getApplicationContext(),"Data Inserted",Toast.LENGTH_SHORT).show();
//        })
//                .addOnFailureListener(exception ->{
//                    Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_SHORT).show();
//                });
//
//    }



}