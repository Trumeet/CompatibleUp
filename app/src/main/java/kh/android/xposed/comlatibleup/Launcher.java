package kh.android.xposed.comlatibleup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by liangyuteng on 16-8-11.
 */
public class Launcher extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(Launcher.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
