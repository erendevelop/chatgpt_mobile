package com.example.chatgpt_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BotActivity extends AppCompatActivity {
    LinearLayout botMessageBackground;
    RecyclerView recyclerView;
    EditText messageEditText;
    Button sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    TextView typingTextView;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot);
        define();
        onClick();
    }
    public void define(){
        recyclerView = findViewById(R.id.recyclerView);
        sendButton = findViewById(R.id.sendButton);
        messageEditText = findViewById(R.id.messageEditText);
        typingTextView = findViewById(R.id.typingText);
        botMessageBackground = findViewById(R.id.botMessageBackground);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        linearManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearManager);
    }
    public void onClick(){
        sendButton.setOnClickListener(e -> {
            String question = messageEditText.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME);
            callAPI(question);
        });
    }
    public void addToChat(String message, String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // adding message to messageList
                messageList.add(new Message(message, sentBy));

                // informs the recyclerview for changes
                messageAdapter.notifyDataSetChanged();

                // scrolls to the position to the newest message sent
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());

                // deleting the message sent
                messageEditText.setText("");

                // setting the typing text invisible
                typingTextView.setText("");

                // setting the button text as normal
                sendButton.setText("Send");

                // setting the edittext as typeable again
                messageEditText.setEnabled(true);

                onClick();
            }
        });
    }
    public void addResponse(String response){
        addToChat(response, Message.SENT_BY_BOT);
    }
    void callAPI(String sentence){
        // ui changes
        typingTextView.setText("Typing...");
        sendButton.setText("Wait...");
        sendButton.setOnClickListener(e -> {});
        messageEditText.setEnabled(false);
        // okHttp
        JSONObject jsonBody = new JSONObject();
        try{
            // this makes a JSONObject as {"model" : "text-davinci-003" ... "temperature : "0"}
            jsonBody.put("model", "text-davinci-003");
            jsonBody.put("prompt", sentence);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request req = new Request.Builder().url("https://api.openai.com/v1/completions").header("Authorization", "Bearer token").post(body).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text").trim();
                        addResponse(result);
                    } catch (JSONException e) {
                         e.printStackTrace();
                    }
                } else{
                    addResponse("Failed to load response due to"+response.body().toString());
                }
            }
        });
    }
}