package com.paringer.minesweeper;

import android.content.Context;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import java.util.HashSet;

/**
 * Created by Zhenya on 27.05.2016.
 * Board class maintains game board information and elements
 */
public class Board {
    int sizeY;
    int sizeX;
    int viewWidth;
    int viewHeight;
    int minesTotal = 0;
    int minesMarked = 0;
    int minesFound = 0;
    int openedFree = 0;
    Element[][] elements;
    HashSet<Element> set = new HashSet<>();

    public Board(Context context, int sizeY, int sizeX, int numMines) {//, SkinMinesweeper skin
        this.sizeY = sizeY;
        this.sizeX = sizeX;
        this.minesTotal = numMines;
        elements = new Element[sizeX][sizeY];
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                elements[x][y] = new Element(context, x, y);
            }
        }

    }

    public void clear() {
        minesFound = 0;
        minesMarked = 0;
        openedFree = 0;
        set.clear();
        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
                elements[x][y].clear();
    }

    public void prepareViews(Context context, SkinMinesweeper skin, GridLayout gridLayout, GameBox gameBox){
        gridLayout.removeAllViewsInLayout();
        gridLayout.setColumnCount(sizeX);
        gridLayout.setRowCount(sizeY);
//        gridLayout.setScrollContainer(false);
        gridLayout.setUseDefaultMargins(false);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setColumnOrderPreserved(true);
        gridLayout.setRowOrderPreserved(true);
        gridLayout.setClipChildren(true);

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                elements[x][y].setViewSomething(x, y, sizeX, sizeY, viewWidth, viewHeight, null, gridLayout, gameBox);
                updateView(skin, x, y);
            }
        }
    }

    public void updateView(SkinMinesweeper skin, int x, int y) {
        Element element = elements[x][y];
        element.setImageDrawable(skin.getBackgroundDrawable(element), skin.getMineDrawable(element));
    }

    /**
     * @field minesTotal is mines total number
     * @param skin       is current skin
     * @param notX       is x of start point, mines free cell, where first clicked
     * @param notY       is y of start point, mines free cell, where first clicked
     */
    public void addMines(SkinMinesweeper skin, int notX, int notY) {
        clearMines();
        for (int i = 0; i < minesTotal; i++) {
            int x = (int) Math.floor(Math.random() * sizeX);
            int y = (int) Math.floor(Math.random() * sizeY);
            while (elements[x][y].hasMine() || x == notX && y == notY) {
                x = (int) Math.floor(Math.random() * sizeX);
                y = (int) Math.floor(Math.random() * sizeY);
            }
            elements[x][y].setMine(true);
        }
        computeDangerAllCells();
    }

    public void clearMines() {
        minesFound = 0;
        minesMarked = 0;
        openedFree = 0;
        set.clear();
        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
                elements[x][y].clearMine();
    }

    public void computeDangerNearby(int x, int y) {
        int danger = 0;
        int xmin = x - 1;
        int xmax = x + 2;
        int ymin = y - 1;
        int ymax = y + 2;
        for (int x1 = xmin; x1 < xmax; x1++) {
            for (int y1 = ymin; y1 < ymax; y1++) {
                if (x != x1 || y != y1){
                    if (isInsideBoardOfCells(x1, y1) && hasMine(x1, y1)){
                        ++danger;
                    }
                }
            }
        }
        if (elements[x][y].hasMine()) {
            elements[x][y].setDanger(danger);
        } else {
            elements[x][y].setDanger(danger);
        }
    }

    public void computeDangerAllCells() {
        for (int xx = 0; xx < sizeX; xx++) {
            for (int yy = 0; yy < sizeY; yy++) {
                computeDangerNearby(xx, yy);
            }
        }
    }

    public boolean isInsideBoardOfCells(int x, int y) {
        return ((x < sizeX) && (x >= 0) && (y < sizeY) && (y >= 0));
    }

    public boolean hasMine(int x, int y){
        if(! isInsideBoardOfCells(x,y)) return false;
        return elements[x][y].hasMine();
    }

    public boolean isTriggered(int x, int y){
        if(! isInsideBoardOfCells(x,y)) return false;
        return elements[x][y].isTriggered();
    }

    public boolean openRecursive(int x, int y, boolean isManualClickAndMustBeOpened){
        Element element = elements[x][y];
        if(isManualClickAndMustBeOpened){set.clear();}
        if(set.contains(element)) return true;
        set.add(element);

        if(element.isFlagged()&&element.hasMine()){
            return true;
        }
        if(element.hasMine() && ! element.isFlagged()){
            element.setTriggered(true);
            if(! element.isOpened()){
                minesMarked++;
                minesFound++;
            }
            element.setOpened(true);

            return false;
        }

        boolean result = true;
        if( ! element.hasMine() && ! element.isFlagged()){
            //calculate all flagged unopened nearby if count(flagged)>=danger try to open all not flagged
            set.add(element);
            if(!element.isOpened()) openedFree++;
            element.setOpened(true);
            int allFlagged = calculateAllFlaggedNearby(x, y);
            if(allFlagged>=element.getDanger()){
                int xmin = x - 1;
                int xmax = x + 2;
                int ymin = y - 1;
                int ymax = y + 2;
                for (int x1 = xmin; x1 < xmax; x1++) {
                    for (int y1 = ymin; y1 < ymax; y1++) {
                        if (x1 != x || y1 != y){
                            if(! isInsideBoardOfCells(x1,y1)) continue;
                            if(! set.contains(elements[x1][y1])){
                                result &= openRecursive(x1, y1, false);
                            }
                        }
                    }
                }

            }
        }
        return result;
    }

    public int calculateAllFlaggedNearby(int x, int y){
        int result = 0;
        int xmin = x - 1;
        int xmax = x + 2;
        int ymin = y - 1;
        int ymax = y + 2;
        for (int x1 = xmin; x1 < xmax; x1++) {
            for (int y1 = ymin; y1 < ymax; y1++) {
                if (x1 != x || y1 != y){
                    if(isInsideBoardOfCells(x1,y1)){
                        if(elements[x1][y1].isFlagged()&&!elements[x1][y1].isOpened() || elements[x1][y1].hasMine() && elements[x1][y1].isOpened()){
                            result += 1;
                        }
                    }
                }
            }
        }
        return result;
    }

    public void toggleFlag(int x, int y){
        if(!isInsideBoardOfCells(x,y)){return;}
        Element element = elements[x][y];
        if(!element.isOpened()){
            element.setFlagged(!element.isFlagged());
            if(element.isFlagged()){minesMarked++;}else{minesMarked--;}
            if(element.hasMine()){if(element.isFlagged()){minesFound++;}else{minesFound--;}}
        }
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getMinesTotal() {
        return minesTotal;
    }

    public int getMinesMarked() {
        return minesMarked;
    }

    public void updateCellSize(int screenWidth, int screenHeight) {
        int x = screenWidth / sizeX;
        int y = screenHeight / sizeY;
        int t = Math.min(x, y);
        viewWidth = t;
        viewHeight = t;
    }

    public boolean isOpened(int x, int y){
        if(!isInsideBoardOfCells(x,y)){return true;}
        Element element = elements[x][y];
        return element.isOpened();
    }

    public boolean checkWinConditions() {
        return minesMarked == minesFound && minesFound == minesTotal && openedFree == sizeX*sizeY - minesTotal;
    }

    public void cheat(boolean what) {
        if(!what) openedFree = 0; else openedFree = sizeX*sizeY - minesTotal;
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                elements[x][y].setOpened(what);
            }
        }

    }
}
