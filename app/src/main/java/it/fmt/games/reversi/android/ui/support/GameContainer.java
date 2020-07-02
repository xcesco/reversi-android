package it.fmt.games.reversi.android.ui.support;

import android.graphics.drawable.Drawable;
import android.view.View;

import it.fmt.games.reversi.android.repositories.network.model.PlayerType;
import it.fmt.games.reversi.android.ui.views.AppGridLayout;

public interface GameContainer extends View.OnClickListener {
  AppGridLayout getAppGridLayout();

  void setWhitePieceDrawable(Drawable drawable);

  void setBlackPieceDrawable(Drawable drawable);

  void setRotationAngle(float v);

  void setPlayer1Title(String text1);

  void setPlayer2Title(String text2);

  void setPlayer1Score(String s);

  void setPlayer2Score(String s);

  PlayerType getPlayer1Type();

  PlayerType getPlayer2Type();

  Drawable getBlackPieceDrawable();

  Drawable getWhitePieceDrawable();
}
