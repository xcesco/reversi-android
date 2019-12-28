package it.fmt.games.reversi.android.logic;

import java.util.List;

import it.fmt.games.reversi.android.BuildConfig;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.cpu.RandomDecisionHandler;

public class AndroidDecisionHandler extends RandomDecisionHandler {

    public static final long THINKING_TIME = BuildConfig.THINKING_TIME;

    @Override
        public Coordinates compute(List<Coordinates> availableMoves) {
            try {
                Thread.sleep(THINKING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return super.compute(availableMoves);
        }
    }