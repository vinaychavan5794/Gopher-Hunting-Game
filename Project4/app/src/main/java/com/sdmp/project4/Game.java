package com.sdmp.project4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Game extends AppCompatActivity {

    Button startGame;
    TextView currentMode;
    TextView displayWinner;
    TextView movesmade1;
    TextView movesmade2;
    GridView gridview;
    MyCustomAdapter mycustomadapter;
    public WorkerThread player1;
    public WorkerThread2 player2;
    public int mode;
    public static final int gopherHole=45;
    protected final int GUESSED = 1;
    protected final int MAKE_A_MOVE = 2;
    protected final int UPDATE_UI = 3;
    protected int currentTurn=1;
    protected final int MAKE_GUESS = 4;
    Map<Integer,Integer> disasterTrack=new HashMap<Integer, Integer>();
    Map<Integer, Integer> clMiss = new HashMap<Integer, Integer>();
    Map<Integer, Integer> nMiss = new HashMap<Integer, Integer>();
    ArrayList<Integer> player1SaveInstance=new ArrayList<Integer>();
    ArrayList<Integer> player2SaveInstance=new ArrayList<Integer>();


    private ArrayList<Integer> images = new ArrayList<Integer>(
            Arrays.asList(R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.gopher,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,
                    R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,
                    R.drawable.hole,R.drawable.hole));
    private ArrayList<Integer> backupImages = new ArrayList<Integer>(
            Arrays.asList(R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.gopher,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole,
                    R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,
                    R.drawable.hole,R.drawable.hole, R.drawable.hole,R.drawable.hole,R.drawable.hole,R.drawable.hole, R.drawable.hole,
                    R.drawable.hole,R.drawable.hole));

    Integer [] nearMissPositions={44,46,45,35,34,36,55,54,56};
    Integer [] closeMissPosition={47,43,25,65,67,23,63,27};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.landscape);
        }else{
            setContentView(R.layout.activity_game);
        }
        gridview = findViewById(R.id.gridview);
        mycustomadapter=new MyCustomAdapter(this, images);
        gridview.setAdapter(mycustomadapter);
        startGame=(Button)findViewById(R.id.start);
        currentMode=(TextView)findViewById(R.id.currentMode);
        displayWinner=findViewById(R.id.displayWinner);
        movesmade1=findViewById(R.id.movesmade1);
        movesmade2=findViewById(R.id.movesmade2);
        movesmade1.setMovementMethod(new ScrollingMovementMethod());
        movesmade2.setMovementMethod(new ScrollingMovementMethod());
        movesmade1.setText("");
        movesmade2.setText("");
        player1 = new WorkerThread("Player1", uiHandler);
        player2 = new WorkerThread2("Player2", uiHandler);
        player1.start();
        player2.start();
        fillMap();
        Intent intent=getIntent();
        mode=intent.getIntExtra("MODE",0);
        setMode(mode);
        if(savedInstanceState!=null){
            player1SaveInstance=savedInstanceState.getIntegerArrayList("PLAYER1MOVES");
            player2SaveInstance=savedInstanceState.getIntegerArrayList("PLAYER2MOVES");
            for(Integer in:player1SaveInstance){
                images.set(in,R.drawable.orange);
                mycustomadapter.notifyDataSetChanged();
            }
            for(Integer in:player2SaveInstance){
                images.set(in,R.drawable.yellow);
                mycustomadapter.notifyDataSetChanged();
            }

            currentTurn=savedInstanceState.getInt("TURN");
            movesmade1.setText(savedInstanceState.getString("PLAYER1MOVESMADE"));
            movesmade2.setText(savedInstanceState.getString("PLAYER2MOVESMADE"));
            currentMode.setText(savedInstanceState.getString("MODESELECTED"));
            displayWinner.setText(savedInstanceState.getString("WINNER"));
            if(savedInstanceState.getInt("ON")==1) {
                startGame.setEnabled(false);
                Message msg = uiHandler.obtainMessage(MAKE_A_MOVE);
                uiHandler.sendMessage(msg);
            }
        }

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startGame();
            }
        });

    }
    protected void onDestroy() {
        super.onDestroy();
        player1.quit();
        player2.quit();
        player1.interrupt();
        player2.interrupt();
    }

    public void fillMap(){
        for(int i=0;i<nearMissPositions.length;i++){
            nMiss.put(nearMissPositions[i],i);
        }
        for(int j=0;j<closeMissPosition.length;j++){
            clMiss.put(closeMissPosition[j],j);
        }
    }


    public void startGame(){
        if(displayWinner.getText().length()>0){
            displayWinner.setText(null);
            movesmade1.setText(null);
            movesmade2.setText(null);
            player1SaveInstance.clear();
            player2SaveInstance.clear();
            Collections.copy(images,backupImages);
            mycustomadapter.notifyDataSetChanged();
        }
        firstMove();
        Message msg = uiHandler.obtainMessage(MAKE_A_MOVE);
        uiHandler.sendMessage(msg);
    }

    public void setMode(int Mode) {
        if (Mode==0) {
            currentMode.setText("GUESS MODE SELECTED");
        } else {
            currentMode.setText("CONTINUOUS MODE SELECTED");
        }
    }

    private void firstMove() {
        startGame.setEnabled(false);
        player1.addRunnable(new Runnable() {
            @Override
            public void run() {
                Random random=new Random();
                int guess = random.nextInt(10)*10;
                Message msg = uiHandler.obtainMessage(GUESSED);
                msg.arg2 = guess;
                uiHandler.sendMessage(msg);
            }
        });

        player2.addRunnable(new Runnable() {
            @Override
            public void run() {
                Random random=new Random();
                int guess = random.nextInt(10)*8;
                Message msg = uiHandler.obtainMessage(GUESSED);
                msg.arg2 = guess;
                uiHandler.sendMessage(msg);
            }
        });
    }

    protected Handler uiHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            Message newMSG;
            int guess;
            int what = msg.what;
            int feed=0;
            int feed1;
            switch (what) {
                case GUESSED:
                    guess = msg.arg2;
                    if(currentTurn%2==1) {
                        player1SaveInstance.add(guess);
                        images.set(guess, R.drawable.orange);
                    }
                    else {
                        player2SaveInstance.add(guess);
                        images.set(guess,R.drawable.yellow);
                    }
                    mycustomadapter.notifyDataSetChanged();

                    if(gopherHole == guess) {
                        startGame.setEnabled(true);
                        if(currentTurn%2 == 1) {
                            movesmade1.append("SUCCESS\n");
                            displayWinner.setText("Player 1 Wins");
                        }
                        else {
                            movesmade2.append("SUCCESS\n");
                            displayWinner.setText("Player 2 Wins");
                        }
                    }
                    nextPlayer();
                    break;
                case MAKE_A_MOVE:
                    guess=msg.arg2;
                    feed1=msg.arg1;
                    if(currentTurn == 1) {
                        if(mode==0){
                            if(disasterTrack.containsKey(guess)){
                                player1.addMessageFeedbackGuess(MAKE_GUESS,2);
                            }
                            else if(feed1==11){
                                player1.addMessageFeedbackGuess(MAKE_GUESS,1);
                            }
                            else if( feed1==10){
                                player1.addMessageFeedbackGuess(MAKE_GUESS,0);
                            }
                            else if(gopherHole==guess){

                            }
                            else{
                                player1.addMessageFeedbackGuess(MAKE_GUESS,4);
                            }
                        }
                        else{
                            if(disasterTrack.containsKey(guess)){
                                player1.addMessageFeedbackContinuous(MAKE_GUESS,2);
                            }
                            else if(feed1==11){
                                player1.addMessageFeedbackContinuous(MAKE_GUESS,1);
                            }
                            else if( feed1==10){
                                player1.addMessageFeedbackContinuous(MAKE_GUESS,0);
                            }

                            else if(gopherHole==guess){

                            }
                            else{
                                player1.addMessageFeedbackContinuous(MAKE_GUESS,4);
                            }
                        }

                    } else{
                        if(mode==0){
                            if( feed1==20){
                                player2.addMessageFeedbackGuess(MAKE_GUESS,0);
                            }
                            else if(feed1==21){
                                player2.addMessageFeedbackGuess(MAKE_GUESS,1);
                            }
                            else if(disasterTrack.containsKey(guess)){
                                player2.addMessageFeedbackGuess(MAKE_GUESS,2);
                            }

                            else if(gopherHole==guess){

                            }
                            else{
                                player2.addMessageFeedbackGuess(MAKE_GUESS,4);
                            }
                        }
                        else{
                            if( feed1==20){
                                player2.addMessageFeedbackContinuous(MAKE_GUESS,0);
                            }
                            else if(feed1==21){
                                player2.addMessageFeedbackContinuous(MAKE_GUESS,1);
                            }
                            else if(disasterTrack.containsKey(guess)){
                                player2.addMessageFeedbackContinuous(MAKE_GUESS,2);
                            }
                            else if(gopherHole==guess){

                            }
                            else{
                                player2.addMessageFeedbackContinuous(MAKE_GUESS,4);
                            }
                        }

                    }
                    break;
                case UPDATE_UI:
                    guess = msg.arg2;

                    if(currentTurn==1) {
                        player1SaveInstance.add(guess);
                        if(disasterTrack.containsKey(guess)){
                            movesmade1.append("DISASTER\n");
                            feed=12;
                        }

                        else if(clMiss.containsKey(guess) ){
                            if(!disasterTrack.containsKey(guess)){
                                disasterTrack.put(guess,0);
                            }
                            movesmade1.append("CLOSE MISS\n");
                            feed=10;
                        }
                        else if(nMiss.containsKey(guess)){
                            if(!disasterTrack.containsKey(guess)){
                                disasterTrack.put(guess,0);
                            }
                            movesmade1.append("NEAR MISS\n");
                            feed=11;
                        }

                        else if(gopherHole==guess){
                            movesmade1.append("SUCCESS\n");
                            feed=13;
                        }
                        else{
                            if(!disasterTrack.containsKey(guess)){
                                disasterTrack.put(guess,0);
                            }
                            movesmade1.append("COMPLETE MISS\n");
                            feed=14;
                        }
                        images.set(guess, R.drawable.orange);
                    }
                    else {
                        player2SaveInstance.add(guess);
                        if( clMiss.containsKey(guess)){
                            if(!disasterTrack.containsKey(guess)){
                                disasterTrack.put(guess,0);
                            }
                            movesmade2.append("CLOSE MISS\n");
                            feed=20;

                        }
                        else if(nMiss.containsKey(guess)){
                            if(!disasterTrack.containsKey(guess)){
                                disasterTrack.put(guess,0);
                            }
                            movesmade2.append("NEAR MISS\n");
                            feed=21;

                        }

                        else if(gopherHole==guess){
                            movesmade2.append("SUCCESS\n");
                            feed=23;

                        }
                        else if(disasterTrack.containsKey(guess)){
                            movesmade2.append("DISASTER\n");
                            feed=22;

                        }
                        else{
                            if(!disasterTrack.containsKey(guess)){
                                disasterTrack.put(guess,0);
                            }
                            movesmade2.append("COMPLETE MISS\n");
                            feed=24;

                        }
                        images.set(guess,R.drawable.yellow);
                    }
                    mycustomadapter.notifyDataSetChanged();
                    if(gopherHole == guess) {
                        startGame.setEnabled(true);
                        if(currentTurn == 1) {
                            movesmade1.append("SUCCESS\n");
                            displayWinner.setText("Player 1 Wins");
                        }
                        else {
                            movesmade2.append("SUCCESS\n");
                            displayWinner.setText("Player 2 Wins");
                        }
                    } else {
                        newMSG = uiHandler.obtainMessage(MAKE_A_MOVE);
                        newMSG.arg1=feed;
                        uiHandler.sendMessage(newMSG);
                    }
                    break;
            }
        }
    };

    public void nextPlayer(){
        if(currentTurn==1)
            currentTurn=2;
        else
            currentTurn=1;
    }

    public class WorkerThread extends HandlerThread {

        private WorkerHandler workerHandler;
        private Handler uiHandler;
        private Random random;
        LinkedList<Integer> nearMiss = new LinkedList<>(Arrays.asList(nearMissPositions));

        public WorkerThread(String name, Handler uiHandler) {
            super(name);
            this.random = new Random();
            this.uiHandler = uiHandler;
        }

        public void addMessageFeedbackGuess(int message,int feedback){
            if(workerHandler != null) {
                Message msg = workerHandler.obtainMessage(message);
                msg.arg1=feedback;
                workerHandler.sendMessageDelayed(msg, 900);
            }
        }
        public void addMessageFeedbackContinuous(int message,int feedback){
            if(workerHandler != null) {
                Message msg = workerHandler.obtainMessage(message);
                msg.arg1=feedback;
                workerHandler.sendMessageDelayed(msg, 380);
            }
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            workerHandler = new WorkerHandler(getLooper());
        }

        public void addRunnable(Runnable runnable) {
            if(workerHandler != null) {
                workerHandler.post(runnable);
            }
        }

        private class WorkerHandler extends Handler {

            public WorkerHandler(Looper looper) {
                super(looper);
            }
            @Override
            public void	handleMessage(Message msg) {
                super.handleMessage(msg);
                int what = msg.what;
                int tens;
                int units;
                int feedbk=msg.arg1;
                switch (what) {
                    case MAKE_GUESS:
                        nextPlayer();
                        if(feedbk==0){
                            Message uiMESSAGE = uiHandler.obtainMessage(UPDATE_UI);
                            tens=ThreadLocalRandom.current().nextInt(2,7);
                            units=ThreadLocalRandom.current().nextInt(3,8);
                            uiMESSAGE.arg2 = tens*10+units;
                            uiHandler.sendMessage(uiMESSAGE);

                        }
                        else if(feedbk==1){
                            Message uiMESSAGE = uiHandler.obtainMessage(UPDATE_UI);
                            if(!nearMiss.isEmpty()) {
                                Collections.shuffle(nearMiss);
                                uiMESSAGE.arg2 = nearMiss.removeLast();
                                uiHandler.sendMessage(uiMESSAGE);
                            }
                        }
                        else if(feedbk==2){
                            Message uiMESSAGE = uiHandler.obtainMessage(UPDATE_UI);
                            uiMESSAGE.arg2 = heuristic();
                            uiHandler.sendMessage(uiMESSAGE);
                        }
                        else if(feedbk==3){
                            break;
                        }
                        else{
                            Message uiMESSAGE = uiHandler.obtainMessage(UPDATE_UI);
                            uiMESSAGE.arg2 = heuristic();
                            uiHandler.sendMessage(uiMESSAGE);
                        }
                        break;

                }
            }

            private int heuristic() {
                int guess = random.nextInt(100);
                return guess;
            }
        }
    }

    public class WorkerThread2 extends HandlerThread {

        private WorkerHandler2 workerHandler2;
        private Handler uiHandler;
        LinkedList<Integer> nearMiss1 = new LinkedList<>(Arrays.asList(nearMissPositions));

        public WorkerThread2(String name, Handler uiHandler) {
            super(name);
            this.uiHandler = uiHandler;
        }

        public void addMessageFeedbackGuess(int message,int feedback){
            if(workerHandler2 != null) {
                Message msg = workerHandler2.obtainMessage(message);
                msg.arg1=feedback;
                workerHandler2.sendMessageDelayed(msg, 900);
            }
        }
        public void addMessageFeedbackContinuous(int message,int feedback){
            if(workerHandler2 != null) {
                Message msg = workerHandler2.obtainMessage(message);
                msg.arg1=feedback;
                workerHandler2.sendMessageDelayed(msg, 380);
            }
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            workerHandler2 = new WorkerHandler2(getLooper());
        }

        public void addRunnable(Runnable runnable) {
            if(workerHandler2 != null) {
                workerHandler2.post(runnable);
            }
        }

        private class WorkerHandler2 extends Handler {

            public WorkerHandler2(Looper looper) {
                super(looper);
            }
            @Override
            public void	handleMessage(Message msg) {
                super.handleMessage(msg);
                int what = msg.what;
                int feedbk=msg.arg1;
                int tens;
                int units;
                switch (what) {
                    case MAKE_GUESS:
                        nextPlayer();
                        if(feedbk==0){
                            Message uiMESSAGE = uiHandler.obtainMessage(UPDATE_UI);
                            tens=ThreadLocalRandom.current().nextInt(2,7);
                            units=ThreadLocalRandom.current().nextInt(3,8);
                            uiMESSAGE.arg2 = tens*10+units;
                            uiHandler.sendMessage(uiMESSAGE);
                        }
                        else if(feedbk==1){
                            Message uiMESSAGE = uiHandler.obtainMessage(UPDATE_UI);
                            if(!nearMiss1.isEmpty()) {
                                Collections.shuffle(nearMiss1);
                                uiMESSAGE.arg2 = nearMiss1.removeLast();
                                uiHandler.sendMessage(uiMESSAGE);
                            }
                        }
                        else if(feedbk==2){
                            Message uiMESSAGE = uiHandler.obtainMessage(UPDATE_UI);
                            uiMESSAGE.arg2 = heuristic1();
                            uiHandler.sendMessage(uiMESSAGE);
                        }
                        else if(feedbk==3){
                            break;
                        }
                        else{
                            Message uiMESSAGE = uiHandler.obtainMessage(UPDATE_UI);
                            uiMESSAGE.arg2 = heuristic1();
                            uiHandler.sendMessage(uiMESSAGE);
                        }
                        break;

                }
            }
            private int heuristic1() {
                int tens,units;
                tens=ThreadLocalRandom.current().nextInt(0,10);
                units=ThreadLocalRandom.current().nextInt(0,10);
                return tens*10+units;
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState) ;
        outState.putIntegerArrayList("PLAYER1MOVES",player1SaveInstance);
        outState.putIntegerArrayList("PLAYER2MOVES",player2SaveInstance);
        outState.putInt("TURN",currentTurn);
        outState.putString("PLAYER1MOVESMADE",movesmade1.getText().toString());
        outState.putString("PLAYER2MOVESMADE",movesmade2.getText().toString());
        outState.putString("MODESELECTED",currentMode.getText().toString());
        outState.putString("WINNER",displayWinner.getText().toString());
        if(!startGame.isEnabled()){
            outState.putInt("ON",1);
        }


    }

}
