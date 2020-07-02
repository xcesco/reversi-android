package it.fmt.games.reversi.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.databinding.FragmentLauncherBinding;
import it.fmt.games.reversi.android.ui.fragments.support.FragmentUtils;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LauncherFragment extends Fragment {
  private FragmentLauncherBinding binding;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = FragmentLauncherBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();

    binding.btnHumanVsNetwork.setOnClickListener(btn -> findNavController(this).navigate(R.id.action_settings));
    binding.btnCPUVsHuman.setOnClickListener(btn -> findNavController(this).navigate(R.id.action_settings));
    binding.btnHumanVsCPU.setOnClickListener(btn -> findNavController(this).navigate(R.id.action_settings));
    binding.btnHumanVsHuman.setOnClickListener(btn -> findNavController(this).navigate(R.id.action_settings));
    binding.btnCPUVsCPU.setOnClickListener(btn -> findNavController(this).navigate(R.id.action_settings));

    FragmentUtils.configureHomeButton(requireActivity(), false);

    setHasOptionsMenu(true);

    return view;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_main, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}