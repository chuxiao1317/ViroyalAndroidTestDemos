package com.chuxiao.preferencefragmenttest.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import com.chuxiao.preferencefragmenttest.R;

public class SettingsFragment extends PreferenceFragment//preferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        // 注销
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_visyalizer);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        // 为每一个列表偏好设置都设置摘要
        for (int i = 0; i < count; i++) {
            Preference preference = prefScreen.getPreference(i);
            if (!(preference instanceof CheckBoxPreference)) {
                // getString方法用在CheckBoxPreference上时会出错，
                // 因为CheckBoxPreference的key值都是Boolean值，
                // 故此处排除CheckBoxPreference
                String value = sharedPreferences.getString(preference.getKey(), "");
                setListPreferenceSummary(preference, value);
            }
        }

        Preference preference = findPreference(getString(R.string.pref_size_key));
        preference.setOnPreferenceChangeListener(this);
    }

    /**
     * 为列表偏好设置设置摘要
     */
    private void setListPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                // 设置摘要
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(value);
        }
    }

    /**
     * 偏好设置发生改变时回调
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference != null && !(preference instanceof CheckBoxPreference)) {
            String value = sharedPreferences.getString(preference.getKey(), "");
            setListPreferenceSummary(preference, value);
        }
    }

    /**
     * 偏好设置的值保存之前触发
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast errorToast = Toast.makeText(getActivity(), "请输入一个0.1到2之间的数", Toast.LENGTH_SHORT);

        String sizeKey = getString(R.string.pref_size_key);
        if (preference.getKey().equals(sizeKey)) {
            String stringSize = ((String) newValue).trim();
            if (stringSize.equals("")) {
                stringSize = "1";
            }
            try {
                float size = Float.parseFloat(stringSize);
                if (size > 2 || size < 0.1) {
                    errorToast.show();
                    return false;
                }
            } catch (NumberFormatException e) {
                // 防止用户异常输入时出错
                errorToast.show();
                return false;
            }
        }
        return true;
    }
}
