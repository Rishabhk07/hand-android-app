package me.rishabhkhanna.the_hand;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Hand extends AppCompatActivity {
    public static final Integer REQ_CODE  = 1234;
    public static final String TAG = "THE HAND activity";
    Socket socket = null;
    EditText etText;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand);
        Log.d(TAG, "onCreate: HAND ACTIVITY");
        etText = (EditText) findViewById(R.id.etText);
        btn = (Button) findViewById(R.id.btn);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        try {
            socket = IO.socket("http://192.168.43.164:9999");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
        Log.d(TAG, "onCreate: " + socket.connected());
        startActivityForResult(intent,REQ_CODE);
        Log.d(TAG, "onCreate: hand activity called");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = etText.getText().toString();
                socket.emit("msg",new Gson().toJson(result));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE && resultCode == RESULT_OK){
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
            );
            String spokenText = results.get(0);
            Log.d(TAG, "onActivityResult: " + spokenText.charAt(0));
            ChatApi.getSocket();
            Model model = new Model(spokenText.charAt(0));
            socket.emit("message",new Gson().toJson(model));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
