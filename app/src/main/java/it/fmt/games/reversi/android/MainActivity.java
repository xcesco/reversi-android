package it.fmt.games.reversi.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

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
}
