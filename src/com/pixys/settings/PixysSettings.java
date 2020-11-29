/*
 * Copyright (C) 2020 Pixys OS
 * used for PixysOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pixys.settings;

import com.android.internal.logging.nano.MetricsProto;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.pixys.settings.fragments.PixysExploreFragment;

public class PixysSettings extends SettingsPreferenceFragment implements PixysPreferenceScreenChangeListener, View.OnKeyListener {

    static long frag_id = 0;
    
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pixys_settings_layout, container, false);
        getActivity().setTitle("Explore");

        fragmentManager = getChildFragmentManager();

        fragmentManager.beginTransaction().add(R.id.content, new PixysExploreFragment(), String.valueOf(frag_id)).commit();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.PIXYS;
    }

    @Override
    public void onPixysPreferenceScreenChange(Fragment fragment) {
        frag_id++;
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.pixys_settings_exit_anim, R.anim.pixys_settings_enter_anim, R.anim.pixys_settings_exit_anim, R.anim.pixys_settings_enter_anim).replace(R.id.content, fragment, String.valueOf(frag_id)).addToBackStack(String.valueOf(frag_id)).commit();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    if(fragmentManager.getBackStackEntryCount() == 1)
                        getActivity().setTitle("Explore");
                    fragmentManager.popBackStack();
                    return true;
                }
            }
        }
        return false;
    }

}
