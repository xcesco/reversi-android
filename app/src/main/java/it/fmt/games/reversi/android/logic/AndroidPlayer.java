package it.fmt.games.reversi.android.logic;

import java.util.List;

import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.cpu.RandomDecisionHandler;

public class AndroidPlayer extends RandomDecisionHandler {

    public static final int THINKING_TIME = 1200;

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