package com.example.interactivebutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText nameInput = (EditText) findViewById(R.id.input_text);
        Button button = (Button) findViewById(R.id.start_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                System.out.println("This sentence is printed when the button is clicked");

                name = nameInput.getText().toString();

                Socket client = null;

                try{

                    client = new Socket("192.168.1.109", 6666); // connect to server
                    PrintWriter printwriter = new PrintWriter(client.getOutputStream(),true);
                    printwriter.write(name); // write the message to output stream

                    printwriter.flush();
                    printwriter.close();

                    // closing the connection
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}