package com.example.malusong.toggleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ToggleView toggleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleView = (ToggleView)findViewById(R.id.tv_toggle);
        toggleView.setOnSwitchStateUpdateListener(new ToggleView.OnSwitchStateUpdateListener() {
            @Override
            public void onStateUpdate(boolean state) {
                if(state){
                    Toast.makeText(MainActivity.this,"开",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this,"关",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
