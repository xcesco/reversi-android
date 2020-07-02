package it.fmt.games.reversi.android.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.databinding.ActivityReversiBinding;

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
        break;
      case R.id.menu_played_matches:
        findNavController(this, R.id.navHostFragment).navigate(R.id.action_played_matches);
        break;
      default:
        break;
    }

    return super.onOptionsItemSelected(item);
  }

}