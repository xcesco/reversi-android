package it.fmt.games.reversi.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.preference.PreferenceFragmentCompat;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.ui.fragments.support.FragmentUtils;

public class SettingsFragment extends PreferenceFragmentCompat {
  @Override
  public View onCreateView(
          LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState
  ) {
    View view = super.onCreateView(inflater, container, savedInstanceState);

    FragmentUtils.configureHomeButton(requireActivity(), true);
    setHasOptionsMenu(true);

    return view;
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.root_preferences, rootKey);
  }
}