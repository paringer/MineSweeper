package com.paringer.minesweeper;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Zhenya on 27.05.2016.
 */
public class SkinMinesweeper {

    private Drawable m0red;
    private Drawable m0;
    private Drawable m1;
    private Drawable m2;
    private Drawable m3;
    private Drawable m4;
    private Drawable m5;
    private Drawable m6;
    private Drawable m7;
    private Drawable m8;
    private Drawable mine;
    private Drawable flag;
    private Drawable cover;

    public SkinMinesweeper(Context context) {
        m0red = context.getResources().getDrawable(R.drawable.minesweeper_0_red);
        m0 = context.getResources().getDrawable(R.drawable.minesweeper_0);
        m1 = context.getResources().getDrawable(R.drawable.minesweeper_1);
        m2 = context.getResources().getDrawable(R.drawable.minesweeper_2);
        m3 = context.getResources().getDrawable(R.drawable.minesweeper_3);
        m4 = context.getResources().getDrawable(R.drawable.minesweeper_4);
        m5 = context.getResources().getDrawable(R.drawable.minesweeper_5);
        m6 = context.getResources().getDrawable(R.drawable.minesweeper_6);
        m7 = context.getResources().getDrawable(R.drawable.minesweeper_7);
        m8 = context.getResources().getDrawable(R.drawable.minesweeper_8);
        mine = context.getResources().getDrawable(R.drawable.minesweeper_mine22);
        flag = context.getResources().getDrawable(R.drawable.minesweeper_flag);
        cover = context.getResources().getDrawable(R.drawable.minesweeper_unopened_square);
    }

    public void clear(){
        m0red = null;
        m0 = null;
        m1 = null;
        m2 = null;
        m3 = null;
        m4 = null;
        m5 = null;
        m6 = null;
        m7 = null;
        m8 = null;
        mine = null;
        flag = null;
        cover = null;
    }

    Drawable getBackgroundDrawable(Element el) {
        if (el == null) return null;
        if (!el.isOpened())
            if (el.isFlagged()) return flag;
            else return cover;
        if (el.isOpened() && el.hasMine() && el.isTriggered()) return m0red;
        if (el.isOpened() && el.hasMine()) return m0;
        if (el.isOpened() && !el.hasMine()){
            switch (el.getDanger()) {
                case 0: return m0;
                case 1: return m1;
                case 2: return m2;
                case 3: return m3;
                case 4: return m4;
                case 5: return m5;
                case 6: return m6;
                case 7: return m7;
                case 8: return m8;
                default: return m0;
            }
        }
        return null;
    }

    Drawable getMineDrawable(Element el) {
        if (!el.isOpened()) {
            if (el.isFlagged())
                return null;
            else
                return null;
        } else {
            if (el.hasMine())
                return mine;
            else
                return null;
        }
    }

}
