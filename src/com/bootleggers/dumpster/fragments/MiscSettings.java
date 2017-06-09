package com.bootleggers.dumpster.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;

import java.util.Arrays;
import java.util.HashSet;

import com.android.settings.Utils;
import com.android.settings.SettingsPreferenceFragment;

import net.margaritov.preference.colorpicker.ColorPickerPreference;


public class MiscSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    //Launch music player on headset connect - Wireless Compatibility
    private static final String HEADSET_CONNECT_PLAYER = "headset_connect_player";
    private ListPreference mLaunchPlayerHeadsetConnection;
    
    // Battery Low Color
    private static final String TAG = "StatusbarBatteryStyle";
    private static final String STATUS_BAR_BATTERY_SAVER_COLOR = "status_bar_battery_saver_color";
    private ColorPickerPreference mBatterySaverColor;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.bootleg_dumpster_misc);
        
        ContentResolver resolver = getActivity().getContentResolver();

        mLaunchPlayerHeadsetConnection = (ListPreference) findPreference(HEADSET_CONNECT_PLAYER);
        int mLaunchPlayerHeadsetConnectionValue = Settings.System.getIntForUser(resolver,
                Settings.System.HEADSET_CONNECT_PLAYER, 4, UserHandle.USER_CURRENT);
        mLaunchPlayerHeadsetConnection.setValue(Integer.toString(mLaunchPlayerHeadsetConnectionValue));
        mLaunchPlayerHeadsetConnection.setSummary(mLaunchPlayerHeadsetConnection.getEntry());
        mLaunchPlayerHeadsetConnection.setOnPreferenceChangeListener(this);
        
        //Battery Low Color
        int batterySaverColor = Settings.Secure.getInt(resolver,
                Settings.Secure.STATUS_BAR_BATTERY_SAVER_COLOR, 0xfff4511e);
        mBatterySaverColor = (ColorPickerPreference) findPreference("status_bar_battery_saver_color");
        mBatterySaverColor.setNewPreviewColor(batterySaverColor);
        mBatterySaverColor.setOnPreferenceChangeListener(this);

        enableStatusBarBatteryDependents();

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        // If we didn't handle it, let preferences handle it.
        return super.onPreferenceTreeClick(preference);
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DUMPSTER;
    }
    
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
    
        if (preference == mLaunchPlayerHeadsetConnection) {
            int mLaunchPlayerHeadsetConnectionValue = Integer.valueOf((String) newValue);
            int index = mLaunchPlayerHeadsetConnection.findIndexOfValue((String) newValue);
            mLaunchPlayerHeadsetConnection.setSummary(
                    mLaunchPlayerHeadsetConnection.getEntries()[index]);
            Settings.System.putIntForUser(resolver, Settings.System.HEADSET_CONNECT_PLAYER,
                    mLaunchPlayerHeadsetConnectionValue, UserHandle.USER_CURRENT);
            return true;
        }
        // Battery Low Color
        if (preference.equals(mBatterySaverColor)) {
            int color = ((Integer) newValue).intValue();
            Settings.Secure.putInt(resolver,
                    Settings.Secure.STATUS_BAR_BATTERY_SAVER_COLOR, color);
            return true;
        }
        return false;
    }
    
    private void enableStatusBarBatteryDependents() {
        mBatterySaverColor.setEnabled(true);
    }
}
