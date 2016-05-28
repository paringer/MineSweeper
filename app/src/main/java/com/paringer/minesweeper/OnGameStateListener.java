package com.paringer.minesweeper;

/**
 * Created by Zhenya on 28.05.2016.
 */
public interface OnGameStateListener {
    void onGameWon();
    void onGameOver();
    void onCellMarkedByFlag(int marked, int total);
}
