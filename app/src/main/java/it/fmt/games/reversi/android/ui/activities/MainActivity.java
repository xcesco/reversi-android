package it.fmt.games.reversi.android.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import it.fmt.games.reversi.android.R;

public class MainActivity extends AppCompatActivity {

    @OnClick({R.id.btnCPUVsHuman, R.id.btnHumanVsCPU, R.id.btnHumanVsHuman, R.id.btnCPUVsCPU})
    public void onOpenView(View view) {
        Intent intent=GameActivity.createIntent(this, view.getTag().toString());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
