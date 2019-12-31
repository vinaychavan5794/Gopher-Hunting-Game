package com.sdmp.project4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    Button startGame;
    Button guessbtn;
    Button continuousbtn;
    TextView setMode;
    public  int mode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startGame = (Button)findViewById(R.id.startGanebtn);
        guessbtn = (Button)findViewById(R.id.guessbtn);
        continuousbtn = (Button)findViewById(R.id.continuousbtn);
        setMode=(TextView)findViewById(R.id.modeSelected);
        startGame.setEnabled(false);
        if(savedInstanceState!=null){
            setMode.setText(savedInstanceState.getString("MODE"));
            if(savedInstanceState.getInt("ON")==1){
                startGame.setEnabled(true);
            }
        }
        guessbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginGuess();
            }
        });
        continuousbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginContinuous();
            }
        });
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginGame();
            }
        });
    }
    public void beginGuess(){
    setMode.setText("GUESS MODE SELECTED");
    mode=0;
        if(!startGame.isEnabled()){
            startGame.setEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("MODE",setMode.getText().toString());
        if(startGame.isEnabled()){
            outState.putInt("ON",1);
        }

    }

    public void beginContinuous(){
        setMode.setText("CONTINUOUS MODE SELECTED");
        mode=1;
        if(!startGame.isEnabled()){
            startGame.setEnabled(true);
        }
    }
    public void beginGame(){
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("MODE",mode);
        startActivity(intent);
    }
}
