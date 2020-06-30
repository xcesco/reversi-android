package it.fmt.games.reversi.android.viewmodels;

import it.fmt.games.reversi.android.repositories.network.model.MatchEndMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStartMessage;
import it.fmt.games.reversi.android.repositories.network.model.MatchStatusMessage;

public interface MatchEventDispatcher {

    void postMatchStart(MatchStartMessage matchStartMessage);

    void postMatchMove(MatchStatusMessage matchStatusMessage);

    void postMatchEnd(MatchEndMessage matchEndMessage);
}
