package com.paringer.minesweeper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Zhenya on 27.05.2016.
 */
public class Element {
    boolean flagged = false;
    boolean mine = false;
    boolean triggered = false;
    boolean opened = false;
    int danger = 0;
    ImageView view;
    int x;
    int y;

    public Element(Context context, int x, int y) {
        view = new ImageView(context);
        this.x = x;
        this.y = y;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean isMarkedByFlag) {
        flagged = isMarkedByFlag;
    }

    public boolean hasMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public int getDanger() {
        return danger;
    }

    public void setDanger(int danger) {
        this.danger = danger;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    void setMineDrawable(Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    void setImageDrawable(Drawable background) {
        view.setBackground(background);
    }

    public void setImageDrawable(Drawable background, Drawable mine) {
        view.setBackground(background);
        view.setImageDrawable(mine);
    }

    public void clearMine(){
        flagged = false;
        mine = false;
        triggered = false;
        opened = false;
        danger = 0;
    }

    public void clear(){
        flagged = false;
        mine = false;
        triggered = false;
        opened = false;
        danger = 0;
        view.setImageDrawable(null);
        view.setBackground(null);
    }

    public void setViewSomething(int x, int y, int sizeX, int sizeY, int viewWidth, int viewHeight, GridView.LayoutParams lp, GridLayout gridLayout, GameBox gameBox){
        if(lp != null) view.setLayoutParams(lp);
        view.setTag(Integer.valueOf(x + y * sizeX));
        view.setClickable(true);
        view.setLongClickable(true);
        view.setOnClickListener(gameBox);
        view.setOnLongClickListener(gameBox);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(viewWidth, viewHeight));
        params.setGravity(Gravity.CENTER);
        view.setLayoutParams(params);
        gridLayout.addView(view, x + y * sizeX);
    }

    public void invalidate(){
        if(view!=null) view.invalidate();
    }

}
