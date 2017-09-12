package com.bootleggers.dumpster.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.internal.logging.nano.MetricsProto;

import com.bootleggers.dumpster.preferences.SystemSettingSwitchPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import net.margaritov.preference.colorpicker.ColorPickerPreference;



public class NotificationSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener  {
    
     ///Battery Lights   
     private ColorPickerPreference mLowColor;
     private ColorPickerPreference mMediumColor;
     private ColorPickerPreference mFullColor;
     private ColorPickerPreference mReallyFullColor;
     private SystemSettingSwitchPreference mLowBatteryBlinking;
     private PreferenceCategory mColorCategory;
     private PreferenceCategory mLedsCategory;
     private Preference mChargingLeds;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //Load the XML to show the interface of this section
        addPreferencesFromResource(R.xml.bootleg_dumpster_notifications);
        
        final ContentResolver resolver = getActivity().getContentResolver();
        PreferenceScreen prefSet = getPreferenceScreen();
        
        //Battery Lights code
        mLedsCategory = (PreferenceCategory) findPreference("light_category");
        mChargingLeds = (Preference) findPreference("battery_charging_light");
        if (mChargingLeds != null
                && !getResources().getBoolean(
                        com.android.internal.R.bool.config_intrusiveBatteryLed)) {
            mLedsCategory.removePreference(mChargingLeds);
        }
          if (mChargingLeds == null) {
            prefSet.removePreference(mLedsCategory);
        }
        mColorCategory = (PreferenceCategory) findPreference("battery_light_cat");

        mLowBatteryBlinking = (SystemSettingSwitchPreference)prefSet.findPreference("battery_light_low_blinking");
        if (getResources().getBoolean(
                        com.android.internal.R.bool.config_ledCanPulse)) {
            mLowBatteryBlinking.setChecked(Settings.System.getIntForUser(getContentResolver(),
                            Settings.System.BATTERY_LIGHT_LOW_BLINKING, 0, UserHandle.USER_CURRENT) == 1);
            mLowBatteryBlinking.setOnPreferenceChangeListener(this);
        } else {
            prefSet.removePreference(mLowBatteryBlinking);
        }

        if (getResources().getBoolean(com.android.internal.R.bool.config_multiColorBatteryLed)) {
            int color = Settings.System.getIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_LOW_COLOR, 0xFFFF0000,
                            UserHandle.USER_CURRENT);
            mLowColor = (ColorPickerPreference) findPreference("battery_light_low_color");
            mLowColor.setAlphaSliderEnabled(true);
            mLowColor.setNewPreviewColor(color);
            mLowColor.setOnPreferenceChangeListener(this);

            color = Settings.System.getIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_MEDIUM_COLOR, 0xFFFFFF00,
                            UserHandle.USER_CURRENT);
            mMediumColor = (ColorPickerPreference) findPreference("battery_light_medium_color");
            mMediumColor.setAlphaSliderEnabled(true);
            mMediumColor.setNewPreviewColor(color);
            mMediumColor.setOnPreferenceChangeListener(this);

            color = Settings.System.getIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_FULL_COLOR, 0xFF00FF00,
                            UserHandle.USER_CURRENT);
            mFullColor = (ColorPickerPreference) findPreference("battery_light_full_color");
            mFullColor.setAlphaSliderEnabled(true);
            mFullColor.setNewPreviewColor(color);
            mFullColor.setOnPreferenceChangeListener(this);

            color = Settings.System.getIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_REALLYFULL_COLOR, 0xFF00FF00,
                            UserHandle.USER_CURRENT);
            mReallyFullColor = (ColorPickerPreference) findPreference("battery_light_reallyfull_color");
            mReallyFullColor.setAlphaSliderEnabled(true);
            mReallyFullColor.setNewPreviewColor(color);
            mReallyFullColor.setOnPreferenceChangeListener(this);
        } else {
            prefSet.removePreference(mColorCategory);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DUMPSTER;
    }
    
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        
        //Battery Lights
        if (preference.equals(mLowColor)) {
            int color = ((Integer) newValue).intValue();
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_LOW_COLOR, color,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference.equals(mMediumColor)) {
            int color = ((Integer) newValue).intValue();
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_MEDIUM_COLOR, color,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference.equals(mFullColor)) {
            int color = ((Integer) newValue).intValue();
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_FULL_COLOR, color,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference.equals(mReallyFullColor)) {
            int color = ((Integer) newValue).intValue();
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.BATTERY_LIGHT_REALLYFULL_COLOR, color,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mLowBatteryBlinking) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.BATTERY_LIGHT_LOW_BLINKING, value ? 1 : 0,
                    UserHandle.USER_CURRENT);
            mLowBatteryBlinking.setChecked(value);
            return true;
        }
        return false;
    }
}
