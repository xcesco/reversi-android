package it.fmt.games.reversi.android.ui.fragments.support;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public abstract class FragmentUtils {
  private FragmentUtils() {

  }

  public static void configureHomeButton(Activity activity, boolean enabled) {
    if (activity instanceof AppCompatActivity) {
      ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
      supportActionBar.setDisplayShowHomeEnabled(enabled);
      supportActionBar.setDisplayHomeAsUpEnabled(enabled);
    }


  }
}
