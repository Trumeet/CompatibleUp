package kh.android.xposed.comlatibleup;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import kh.android.updatecheckerlib.UpdateChecker;


public class MainActivity extends AppCompatActivity {
    Context mContext;
    SwitchCompat swStart;
    public static SharedPreferences prefs;
    boolean isEnable=true;
    private MenuItem mMenuItemUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        swStart= (SwitchCompat) findViewById(R.id.switchStart);
        //prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isEnable = prefs.getBoolean("ENABLE",false);
        swStart.setChecked(isEnable);
        swStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putBoolean("ENABLE",b);
                prefsEditor.apply();
            }
        });
    }
    private void checkUpdate(){
        new UpdateChecker(UpdateChecker.Market.MARKET_COOLAPK, getPackageName())
                .checkAsync(new UpdateChecker.OnCheckListener() {
                    @Override
                    public void onStartCheck() {
                        mMenuItemUpdate.setEnabled(false);
                        Toast.makeText(mContext, R.string.btn_InCheckUpdate, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void done(UpdateChecker.UpdateInfo info, Exception e) {
                        mMenuItemUpdate.setEnabled(true);
                        if (e == null) {
                            new AlertDialog.Builder(mContext)
                                    .setTitle(R.string.btn_CheckUpdate)
                                    .setMessage(getString(R.string.text_update
                                            , info.getVersionName(), info.getChangeLog().replace(" ", "\n")))
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.ok, null).show();
                        } else {
                            new AlertDialog.Builder(mContext)
                                    .setTitle(R.string.btn_CheckUpdate)
                                    .setMessage(R.string.btn_Update_Err)
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.ok, null).show();
                        }
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenuItemUpdate = menu.findItem(R.id.action_update);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_Hide:
                int mode = prefs.getBoolean("hide_launcher_icon", false) ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
                boolean now=prefs.getBoolean("hide_launcher_icon", false);
                mContext.getPackageManager().setComponentEnabledSetting(new ComponentName(mContext, MainActivity.class.getPackage().getName()+".Launcher"), mode, PackageManager.DONT_KILL_APP);
                if(!now){
                    Toast.makeText(mContext,getString(R.string.toast_ShowIcon),Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext,getString(R.string.toast_HideIcon),Toast.LENGTH_LONG).show();
                }
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putBoolean("hide_launcher_icon",!now);
                prefsEditor.apply();
                break;
            case R.id.action_update :
                checkUpdate();
                break;
            case R.id.action_code :
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/liangyuteng0927/CompatibleUp")));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.action_developer :
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://coolapk.com/u/543424")));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}