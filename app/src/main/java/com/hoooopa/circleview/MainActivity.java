package com.hoooopa.circleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleView = (CircleView) findViewById(R.id.circleView);
        circleView.setOnCirleViewClickListenter(new CircleView.onCirleViewClickListenter() {
            @Override
            public void onClick() {
                Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        circleView.stop();
    }
}
