package it.fmt.games.reversi.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.databinding.FragmentMatchBinding;
import it.fmt.games.reversi.android.ui.fragments.support.FragmentUtils;
import it.fmt.games.reversi.android.ui.support.GameActivityHelper;
import it.fmt.games.reversi.android.ui.support.GameType;
import it.fmt.games.reversi.android.viewmodels.LocalMatchViewModel;
import it.fmt.games.reversi.android.viewmodels.NetworkMatchViewModel;

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

//  @Override
//  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//    menu.clear();
//    super.onCreateOptionsMenu(menu, inflater);
//  }
}