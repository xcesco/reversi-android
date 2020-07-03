package it.fmt.games.reversi.android.viewmodels.model;

import java.util.List;

import it.fmt.games.reversi.DecisionHandler;
import it.fmt.games.reversi.android.BuildConfig;
import it.fmt.games.reversi.android.ui.support.CpuType;
import it.fmt.games.reversi.model.Coordinates;
import it.fmt.games.reversi.model.cpu.RandomDecisionHandler;
import it.fmt.games.reversi.model.cpu.SimpleDecisionHandler;

public class AndroidDecisionHandler implements DecisionHandler {
  public static final long THINKING_TIME = BuildConfig.THINKING_TIME;
  private DecisionHandler delegate;

  public AndroidDecisionHandler(CpuType cpuType) {
    if (CpuType.RANDOM == cpuType) {
      delegate = new RandomDecisionHandler();
    } else {
      delegate = new SimpleDecisionHandler();
    }
  }

  @Override
  public Coordinates compute(List<Coordinates> availableMoves) {
    try {
      Thread.sleep(THINKING_TIME);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return delegate.compute(availableMoves);
  }
}