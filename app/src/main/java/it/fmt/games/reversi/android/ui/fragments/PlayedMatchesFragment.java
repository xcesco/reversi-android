package it.fmt.games.reversi.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
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
    binding.mainRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
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
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

//  @Override
//  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//    menu.clear();
//    super.onCreateOptionsMenu(menu, inflater);
//  }
}