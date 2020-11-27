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

package com.pixys.settings.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pixys.settings.PixysPreferenceScreenChangeListener;
import com.pixys.settings.PixysSettings;
import com.android.settings.R;

public class PixysExploreFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixys_explore, container, false);

        RelativeLayout statusbar_settings = view.findViewById(R.id.pixys_statusbar);
        RelativeLayout quick_settings = view.findViewById(R.id.pixys_quicksettings);
        RelativeLayout notification_settings = view.findViewById(R.id.pixys_notification);
        RelativeLayout buttons_settings = view.findViewById(R.id.pixys_buttons);
        RelativeLayout lockscreen_settings = view.findViewById(R.id.pixys_lockscreen);
        RelativeLayout gesture_settings = view.findViewById(R.id.pixys_gestures);
        RelativeLayout misc_settings = view.findViewById(R.id.pixys_miscellaneous);

        statusbar_settings.setOnClickListener(this);
        quick_settings.setOnClickListener(this);
        notification_settings.setOnClickListener(this);
        buttons_settings.setOnClickListener(this);
        lockscreen_settings.setOnClickListener(this);
        gesture_settings.setOnClickListener(this);
        misc_settings.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        PixysPreferenceScreenChangeListener onPreferenceClick = (PixysPreferenceScreenChangeListener) getParentFragment();
        int id = v.getId();
        if (id == R.id.pixys_statusbar) {
            onPreferenceClick.onPixysPreferenceScreenChange(new SettingsFragment());
        } else if (id == R.id.pixys_quicksettings) {
        } else if (id == R.id.pixys_notification) {
        } else if (id == R.id.pixys_buttons) {
        } else if (id == R.id.pixys_lockscreen) {
        } else if (id == R.id.pixys_gestures) {
        } else if (id == R.id.pixys_miscellaneous) {
        }
    }
    
}