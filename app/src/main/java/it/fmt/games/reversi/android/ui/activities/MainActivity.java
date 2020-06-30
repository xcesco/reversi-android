package it.fmt.games.reversi.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.jetbrains.annotations.NotNull;

import it.fmt.games.reversi.android.R;
import it.fmt.games.reversi.android.databinding.ActivityMainBinding;
import it.fmt.games.reversi.android.ui.support.GameType;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

  private static final int RC_SIGN_IN = 99;

  public void onOpenView(View view) {
    Intent intent = GameActivity.createIntent(this, GameType.valueOf(view.getTag().toString()));
    startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    @NotNull ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    setContentView(view);

    binding.btnCPUVsHuman.setOnClickListener(this::onOpenView);
    binding.btnHumanVsCPU.setOnClickListener(this::onOpenView);
    binding.btnHumanVsHuman.setOnClickListener(this::onOpenView);
    binding.btnCPUVsCPU.setOnClickListener(this::onOpenView);
    binding.btnHumanVsNetwork.setOnClickListener(this::onOpenView);
  }

  public static Intent createIntent(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    return intent;
  }

  @Override
  protected void onResume() {
    super.onResume();
    signInSilently();
  }

  private void startSignInIntent() {
    GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
            GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
    Intent intent = signInClient.getSignInIntent();
    startActivityForResult(intent, RC_SIGN_IN);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess()) {
        // The signed in account is stored in the result.
        GoogleSignInAccount signedInAccount = result.getSignInAccount();
        Timber.e("benvenuto " + signedInAccount.getDisplayName());
//        new AlertDialog.Builder(this).setMessage(message)
//                .setNeutralButton(android.R.string.ok, null).show();
      } else {
        Timber.e(result.getStatus().getStatusMessage());
        String message = result.getStatus().getStatusMessage();
        if (message == null || message.isEmpty()) {
          message = getString(R.string.signin_other_error);
        }
        Timber.e("" + result.getStatus());
//        new AlertDialog.Builder(this).setMessage(message)
//                .setNeutralButton(android.R.string.ok, null).show();
      }
    }
  }


  private void signInSilently() {
    GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    if (GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
      // Already signed in.
      // The signed in account is stored in the 'account' variable.
      GoogleSignInAccount signedInAccount = account;
      Timber.e("benvenuto dd " + signedInAccount.getEmail());
    } else {
      // Haven't been signed-in before. Try the silent sign-in first.
      GoogleSignInClient signInClient = GoogleSignIn.getClient(this, signInOptions);
      signInClient
              .silentSignIn()
              .addOnCompleteListener(
                      this,
                      task -> {
                        Timber.i("onComplete");
                        if (task.isSuccessful()) {
                          // The signed in account is stored in the task's result.
                          GoogleSignInAccount signedInAccount = task.getResult();
                          Timber.i("signed!");
                        } else {
                          // Player will need to sign-in explicitly using via UI.
                          // See [sign-in best practices](http://developers.google.com/games/services/checklist) for guidance on how and when to implement Interactive Sign-in,
                          // and [Performing Interactive Sign-in](http://developers.google.com/games/services/android/signin#performing_interactive_sign-in) for details on how to implement
                          // Interactive Sign-in.
                          Timber.i("iteractive signed!");
                          startSignInIntent();
                        }
                      });
    }
  }

}
