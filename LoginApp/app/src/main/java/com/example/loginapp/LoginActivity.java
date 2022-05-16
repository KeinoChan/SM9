package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    TextView txtView;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        txtView = (TextView)findViewById(R.id.showkey);
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
                        Socket client = null;
                        Socket client1 = null;
                        try{
                            client = new Socket("192.168.1.109", 6666); // connect to server
                            PrintWriter printwriter = new PrintWriter(client.getOutputStream(),true);

                            printwriter.write(user); // write the message to output stream

                            printwriter.flush();
                            printwriter.close();

                            // closing the connection
                            client.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try{
                            Thread.sleep(20000);

                            client1 = new Socket("192.168.1.109", 6666); // connect to server
                            System.out.println("Connected to Server");
                            BufferedReader in = new BufferedReader(new InputStreamReader(client1.getInputStream()));
                            String s1 = in.readLine();
                            String s = in.readLine();
                            String s2 = in.readLine();
                            System.out.println("the received Private Key s: "+s+s2);
                            txtView.setText(s+""+s2);

                            client1.close();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
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