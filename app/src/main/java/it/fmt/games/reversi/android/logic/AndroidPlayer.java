package it.fmt.games.reversi.android.logic;

import java.util.List;

import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.RandomDecisionHandler;

public class AndroidPlayer extends RandomDecisionHandler {
        @Override
        public Coordinates compute(List<Coordinates> availableMoves) {
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return super.compute(availableMoves);
        }
    }