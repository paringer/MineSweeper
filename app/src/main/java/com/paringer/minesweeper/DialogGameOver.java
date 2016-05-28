package com.paringer.minesweeper;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class DialogGameOver extends DialogFragment implements OnClickListener {

  final String LOG_TAG = "myLogs";

  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    getDialog().setTitle(R.string.message_title_game_over);
    View v = inflater.inflate(R.layout.dialog_game_over, null);
    v.findViewById(R.id.btnYes).setOnClickListener(this);
    v.findViewById(R.id.btnNo).setOnClickListener(this);
    return v;
  }

  public void onClick(View v) {
    Log.d(LOG_TAG, "Dialog GameOver: " + ((Button) v).getText());
    if(v.getId()==R.id.btnYes){
      getActivity().recreate();
    }
    dismiss();
  }

  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    Log.d(LOG_TAG, "Dialog GameOver: onDismiss");
  }

  public void onCancel(DialogInterface dialog) {
    super.onCancel(dialog);
    Log.d(LOG_TAG, "Dialog GameOver: onCancel");
  }
}