package com.paringer.minesweeper;

import android.app.DialogFragment;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.HashSet;

/**
 * Created by Zhenya on 28.05.2016.
 */
public class GameBox implements View.OnClickListener, View.OnLongClickListener {
    public static final int DEFAULT_FIELD_SIZE_W = 10;
    public static final int DEFAULT_FIELD_SIZE_H = 10;
    public static final int DEFAULT_FIELD_NUM_MINES = 20;
    Board board;
    SkinMinesweeper skin;
    int movesAmount = 0;
    int screenWidth = 0;
    int screenHeight = 0;
    private boolean mCheat = true;
    OnGameStateListener onGameStateListener;

    public GameBox(SkinMinesweeper skin) {
        this.skin = skin;
    }

    public void updateScreenSize(int width, int height){
        this.screenWidth = width;
        this.screenHeight = height;
        if(board!=null) board.updateCellSize(screenWidth, screenHeight);
    }

    public void newGame(Context context, GridLayout gridLayout){
        movesAmount = 0;
        if (board == null){
            board = new Board(context, DEFAULT_FIELD_SIZE_W, DEFAULT_FIELD_SIZE_H, DEFAULT_FIELD_NUM_MINES);
            board.updateCellSize(screenWidth, screenHeight);
        }else{
            board.clearMines();
            board.updateCellSize(screenWidth, screenHeight);
        }
        board.prepareViews(context, skin, gridLayout, this);
        gridLayout.setOnClickListener(this);
        gridLayout.setOnLongClickListener(this);
    }

    public void cheat(Context context, GridLayout gridLayout){
        board.cheat(mCheat);
        mCheat = ! mCheat;
        board.prepareViews(context, skin, gridLayout, this);
    }

    public void clear(){
        board.clear();
        board = null;
        skin.clear();
        skin = null;
        movesAmount = 0;
        mCheat = true;
        onGameStateListener = null;
    }

    public void setOnGameStateListener(OnGameStateListener onGameStateListener) {
        this.onGameStateListener = onGameStateListener;
    }

    public SkinMinesweeper getSkin() {
        return skin;
    }

    public void setSkin(SkinMinesweeper skin) {
        this.skin = skin;
    }

    public int getMovesAmount() {
        return movesAmount;
    }
    public int getMinesTotal() {
        return board.getMinesTotal();
    }

    public int getMinesMarked() {
        return board.getMinesMarked();
    }

    @Override
    public void onClick(View v) {
        if(! (v instanceof ImageView)) return;
        Integer c = (Integer) ((ImageView)v).getTag();
        if(c == null) return;
        int cc = c.intValue();
        int sizeX = board.getSizeX();
        int x = cc % sizeX;
        int y = cc / sizeX;
        if(!board.isOpened(x,y)){board.toggleFlag(x, y);}
        board.updateView(skin, x, y);
        if(onGameStateListener!=null) onGameStateListener.onCellMarkedByFlag(board.getMinesMarked(), board.getMinesTotal());
        v.invalidate();
        if(checkWinConditions()){
            if(onGameStateListener!=null) onGameStateListener.onGameWon();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(! (v instanceof ImageView)) return false;
        Integer c = (Integer) ((ImageView)v).getTag();
        if(c == null) return false;
        int cc = c.intValue();
        int sizeX = board.getSizeX();
        int x = cc % sizeX;
        int y = cc / sizeX;
        if(movesAmount == 0) {
            board.addMines(skin, x, y);
        }
        movesAmount += 1;
        boolean notBad = openRecursive(x, y);
        if(!notBad){
            //game over
            if(onGameStateListener!=null) onGameStateListener.onGameOver();
            if(onGameStateListener!=null) onGameStateListener.onCellMarkedByFlag(board.getMinesMarked(), board.getMinesTotal());
        }
        if(checkWinConditions()){
            //game won
            if(onGameStateListener!=null) onGameStateListener.onGameWon();
        }
        board.updateView(skin, x, y);
        return false;
    }

    public boolean checkWinConditions() {
        Log.d("mines", "found="+board.minesFound);
        Log.d("mines", "marked="+board.minesMarked);
        Log.d("mines", "unopened="+(board.sizeX*board.sizeY-board.openedFree));
        return board.checkWinConditions();
    }

    public boolean openRecursive(int x, int y){
        boolean result = board.openRecursive(x, y, true);
        for (Element el : board.set) {
            board.updateView(skin, el.getX(), el.getY());
        }
        return result;
    }
}
