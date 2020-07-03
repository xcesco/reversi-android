package it.fmt.games.reversi.android.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.databinding.ActivityReversiBinding;
import it.fmt.games.reversi.android.ui.fragments.LauncherFragmentDirections;
import it.fmt.games.reversi.android.ui.support.GameType;

import static androidx.navigation.Navigation.findNavController;

public class ReversiActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    @NotNull ActivityReversiBinding binding = ActivityReversiBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    setContentView(view);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  public boolean onSupportNavigateUp() {
    return findNavController(this, R.id.navHostFragment).navigateUp();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case R.id.menu_settings:
        findNavController(this, R.id.navHostFragment).navigate(R.id.action_settings);
        return true;
      case R.id.menu_played_matches:
        findNavController(this, R.id.navHostFragment).navigate(R.id.action_played_matches);
        return true;
      case R.id.menu_demo:
        gotoDemo();
        return true;
      case R.id.menu_check:
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.server_url)));
        startActivity(browserIntent);
        return true;
      default:
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  private void gotoDemo() {
    GameType gameType = GameType.CPU_VS_CPU;
    LauncherFragmentDirections.ActionMatch action = LauncherFragmentDirections.actionMatch()
            .setGameType(gameType);
    findNavController(this, R.id.navHostFragment).navigate(action);
  }

}