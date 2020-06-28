package it.fmt.games.reversi.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import it.fmt.games.reversi.android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;

  public void onOpenView(View view) {
    Intent intent = GameActivity.createIntent(this, view.getTag().toString());
    startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    setContentView(view);

    binding.btnCPUVsHuman.setOnClickListener(this::onOpenView);
    binding.btnHumanVsCPU.setOnClickListener(this::onOpenView);
    binding.btnHumanVsHuman.setOnClickListener(this::onOpenView);
    binding.btnCPUVsCPU.setOnClickListener(this::onOpenView);
  }

  public static Intent createIntent(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    return intent;
  }
}
