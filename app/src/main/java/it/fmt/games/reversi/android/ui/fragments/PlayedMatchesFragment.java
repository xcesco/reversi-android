package it.fmt.games.reversi.android.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.databinding.FragmentPlayedMatchBinding;
import it.fmt.games.reversi.android.ui.adapters.PlayedMatchAdapter;
import it.fmt.games.reversi.android.ui.fragments.support.FragmentUtils;
import it.fmt.games.reversi.android.viewmodels.PlayedMatchesViewModel;

import static androidx.navigation.Navigation.findNavController;

public class PlayedMatchesFragment extends Fragment {

  private PlayedMatchesViewModel viewModel;
  private FragmentPlayedMatchBinding binding;

  @Override
  public View onCreateView(
          LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState
  ) {
    binding = FragmentPlayedMatchBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final PlayedMatchAdapter adapter = new PlayedMatchAdapter(getActivity(), null);
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    binding.mainRecyclerView.setLayoutManager(mLayoutManager);

    FragmentUtils.configureHomeButton(requireActivity(), true);
    setHasOptionsMenu(true);

    int resId = R.anim.layout_fall_down;
    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
    binding.mainRecyclerView.setLayoutAnimation(animation);
    binding.mainRecyclerView.setItemAnimator(new DefaultItemAnimator());
    binding.mainRecyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
    binding.mainRecyclerView.setAdapter(adapter);

    viewModel = new ViewModelProvider(this).get(PlayedMatchesViewModel.class);
    viewModel.getAll().observe(getViewLifecycleOwner(), items -> {
      if (items.size() == 0) {
        binding.mainNoItems.setVisibility(View.VISIBLE);
      } else {
        binding.mainNoItems.setVisibility(View.INVISIBLE);
      }
      adapter.updateValues(items);
    });
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_played_matches, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case R.id.menu_played_matches_clear:
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.dialog_delete_confirmation_title)
                .setMessage(R.string.dialog_delete_confirmation_message)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                  viewModel.deleteAll();
                  Toast.makeText(requireActivity(), R.string.dialog_delete_confirmed_message, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss()).show();

        return true;
      default:
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}