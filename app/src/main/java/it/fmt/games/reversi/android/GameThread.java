package it.fmt.games.reversi.android;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import it.fmt.games.reversi.GameRenderer;
import it.fmt.games.reversi.Reversi;
import it.fmt.games.reversi.UserInputReader;
import it.fmt.games.reversi.model.GameSnapshot;

class GameThread extends Thread {
    private final GameActivity main;
    private final Reversi reversi;
    private Handler handler;
     private  GameActivity activity;

     public GameThread(GameActivity mainClass, UserInputReader inputReader, Reversi reversi) {
         this.main=mainClass;
         this.reversi=reversi;
     }

     @Override
     public void run(){
         Looper.prepare();

         handler = new Handler() {
             public void handleMessage(Message msg) {
                // inputReader.
             }
         };
         GameSnapshot result = reversi.play();

         Looper.loop();
     }
 }