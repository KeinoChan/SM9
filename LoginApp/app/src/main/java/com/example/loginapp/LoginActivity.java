package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginActivity extends AppCompatActivity {

    EditText username,password;
    Button btnlogin;
    TextView txtView,introView;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        txtView = (TextView)findViewById(R.id.showkey);
        introView = (TextView)findViewById(R.id.intro);

        DB = new DBHelper(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if(checkuserpass==true){
                        Toast.makeText(LoginActivity.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                        //startActivity(intent);

                        System.out.println("登录按钮This sentence is printed when the button is clicked");

                        try{
                            Socket client = new Socket("192.168.1.109", 6623); // connect to server
                            System.out.println("连接成功");
                            PrintWriter out = new PrintWriter(client.getOutputStream(),true);

                            out.println(user);

                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String s1 = in.readLine();
                            String s2 = in.readLine();
                            System.out.println("the received Private Key s: "+s1+s2);
                            introView.setText("Your SM9 Private key is as below:");
                            txtView.setText(s1+" "+s2);

                            client.close();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }else{
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}