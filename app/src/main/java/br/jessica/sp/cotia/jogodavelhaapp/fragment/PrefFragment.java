package br.jessica.sp.cotia.jogodavelhaapp.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import br.jessica.sp.cotia.jogodavelhaapp.R;

public class PrefFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
}