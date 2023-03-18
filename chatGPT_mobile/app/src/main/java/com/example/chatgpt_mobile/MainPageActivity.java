package com.example.chatgpt_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainPageActivity extends AppCompatActivity {
    Button useGPTButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        useGPTButton = findViewById(R.id.useGPT);
        onClick();
    }
    private void onClick(){
        useGPTButton.setOnClickListener(e -> {
            Toast.makeText(this, "Routed to the chat page.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, BotActivity.class);
            startActivity(intent);
        });
    }
}