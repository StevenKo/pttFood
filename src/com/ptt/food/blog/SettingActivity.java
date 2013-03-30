package com.ptt.food.blog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.ptt.food.blog.api.Setting;

public class SettingActivity extends SherlockFragmentActivity {

    // private SharedPreferences prefs;
 
    private SeekBar      mSeekBar;
    private SeekBar      mSeekBar2;
    private int textTitleSize;
    private int textContentSize;
    private TextView textViewTitle;
    private TextView textViewContent;

    private AlertDialog.Builder finishDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        textTitleSize = Setting.getSetting(Setting.keyTextTitleSize, SettingActivity.this);
        textContentSize = Setting.getSetting(Setting.keyTextContentSize, SettingActivity.this);
        
        
        
        setViews();

        final ActionBar ab = getSupportActionBar();
        ab.setTitle(getResources().getString(R.string.my_read_setting));
        ab.setDisplayHomeAsUpEnabled(true);

    }

    private void setViews() {
        // TODO Auto-generated method stub
        mSeekBar = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBar2 =  (SeekBar) findViewById(R.id.seekBar2);
        textViewTitle = (TextView) findViewById(R.id.text_title_preview);
        textViewContent = (TextView) findViewById(R.id.text_content_preview);
        
        textViewTitle.setTextSize(textTitleSize);
        textViewContent.setTextSize(textContentSize);
        
        mSeekBar.setProgress(textTitleSize);
        mSeekBar2.setProgress(textContentSize);
        
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
            	textViewTitle.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });
        
        mSeekBar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
            	textViewContent.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });

        setFinishDialog();

    }

    
 

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
        case android.R.id.home:
            // finish();
            finishDialog.show();
            break;
        }
        return true;
    }



    private void setFinishDialog() {
        finishDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.setting_living))
                .setMessage(getResources().getString(R.string.setting_message))
                .setPositiveButton(getResources().getString(R.string.setting_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Setting.saveSetting(Setting.keyTextTitleSize, mSeekBar.getProgress(), SettingActivity.this);
                        Setting.saveSetting(Setting.keyTextContentSize, mSeekBar2.getProgress(), SettingActivity.this);
                        finish();

                    }
                }).setNeutralButton(getResources().getString(R.string.setting_neutral), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setNegativeButton(getResources().getString(R.string.setting_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finishDialog.show();
    }

}