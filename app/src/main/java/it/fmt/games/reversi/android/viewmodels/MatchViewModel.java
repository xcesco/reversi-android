package it.fmt.games.reversi.android.viewmodels;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import it.fmt.games.reversi.android.ReversiApplication;
import it.fmt.games.reversi.android.repositories.persistence.MatchRepository;

public class MatchViewModel extends ViewModel {

    public MatchViewModel() {
        ReversiApplication.getInjector().inject(this);
    }

    @Inject
    MatchRepository matchRepository;
}
