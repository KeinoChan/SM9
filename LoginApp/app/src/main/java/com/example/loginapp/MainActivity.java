package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    EditText iptEmail,vefifyCode,password,repassword;
    Button signup,signin,verify;
    DBHelper DB;
    int rdnCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iptEmail = (EditText) findViewById(R.id.email);
        vefifyCode = (EditText) findViewById(R.id.verifycode);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);
        verify = (Button) findViewById(R.id.verifybtn);
        DB = new DBHelper(this);
        rdnCode = ThreadLocalRandom.current().nextInt(100000, 1000000); //verification code.

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = iptEmail.getText().toString();
                System.out.println("乱码The input email:"+email);

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    Toast.makeText(MainActivity.this, "Please provide valid email", Toast.LENGTH_SHORT).show();
                else{
                        SendMail sendMail = new SendMail();
                    try {
                        sendMail.sendmsg(email,rdnCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("乱码Send email successfully");
                    Toast.makeText(MainActivity.this, "Email send successfully, please check your mailbox", Toast.LENGTH_SHORT).show();


                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = iptEmail.getText().toString();
                String iptCode = vefifyCode.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();


                if(user.equals("")||vefifyCode.equals("")||pass.equals("")||repass.equals(""))
                    Toast.makeText(MainActivity.this, "Please enter  all the fields", Toast.LENGTH_SHORT).show();
                else {
                    if(iptCode.equals(Integer.toString(rdnCode))){
                        if(pass.equals(repass)){
                            Boolean checkuser = DB.checkusername(user);
                            if(checkuser==false){
                                Boolean insert = DB.insertData(user,pass);
                                if(insert==true){
                                    Toast.makeText(MainActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                    startActivity(intent); //back to Login once register
                                }else{
                                    Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this, "user already exist, please sign in", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "password not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Verification Code not match, please try again", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);

                }
            });

    }



}