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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pixys.settings.PixysExtraUtils;
import com.android.settings.R;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class PixysAboutFragment extends Fragment {

    private static final String PIXYS_DEVICE_URL = "https://pixysos.com/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixys_about, container, false);

        MaterialCardView coreTeamCard = view.findViewById(R.id.pixys_core_category_card);
        MaterialCardView devicesCard = view.findViewById(R.id.pixys_devices_category_card);
        RecyclerView coreTeamRecycler = view.findViewById(R.id.pixys_core_team_recycler);
        RecyclerView devicesRecycler = view.findViewById(R.id.pixys_devices_recycler);

        RelativeLayout contactGithub = view.findViewById(R.id.pixys_about_contact_github);
        RelativeLayout contactFacebook = view.findViewById(R.id.pixys_about_contact_facebook);
        RelativeLayout contactTelegram = view.findViewById(R.id.pixys_about_contact_telegram);
        RelativeLayout contactWebsite = view.findViewById(R.id.pixys_about_contact_website);
        ArrayList<PixysCategoryItem> deviceMaintainers = new ArrayList<>(parseDevicesJson(PixysExtraUtils.parseJsonFromFile(getContext(), "devices.json")));
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        contactGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/PixysOS"));
                getContext().startActivity(urlIntent);
            }
        });
        contactFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/PixysOS"));
                getContext().startActivity(urlIntent);
            }
        });
        contactTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/PixysOS"));
                getContext().startActivity(urlIntent);
            }
        });
        contactWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pixysos.com"));
                getContext().startActivity(urlIntent);
            }
        });
        coreTeamCard.setOnClickListener(new View.OnClickListener() {
            float rotationAngle = 0f;
            @Override
            public void onClick(View v) {
                RotateAnimation rotateAnimation;
                rotateAnimation = new RotateAnimation(rotationAngle, rotationAngle + 180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setInterpolator(new LinearInterpolator());
                rotateAnimation.setDuration(250);
                rotateAnimation.setFillAfter(true);
                ImageView targetExpandCollapse = view.findViewById(R.id.pixys_core_expand_icon);
                if (coreTeamRecycler.getVisibility() == View.GONE) {
                    coreTeamRecycler.setVisibility(View.VISIBLE);
                    targetExpandCollapse.startAnimation(rotateAnimation);
                    rotationAngle += 180;
                    rotationAngle %= 360;
                } else {
                    coreTeamRecycler.setVisibility(View.GONE);
                    targetExpandCollapse.startAnimation(rotateAnimation);
                    rotationAngle = 0f;
                }
            }
        });

        devicesCard.setOnClickListener(new View.OnClickListener() {
            float rotationAngle = 0f;
            @Override
            public void onClick(View v) {
                RotateAnimation rotateAnimation;
                rotateAnimation = new RotateAnimation(rotationAngle, rotationAngle + 180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setInterpolator(new LinearInterpolator());
                rotateAnimation.setDuration(250);
                rotateAnimation.setFillAfter(true);
                ImageView targetExpandCollapse = view.findViewById(R.id.pixys_devices_expand_icon);
                if (devicesRecycler.getVisibility() == View.GONE) {
                    devicesRecycler.setVisibility(View.VISIBLE);
                    targetExpandCollapse.startAnimation(rotateAnimation);
                    rotationAngle += 180;
                    rotationAngle %= 360;
                } else {
                    devicesRecycler.setVisibility(View.GONE);
                    targetExpandCollapse.startAnimation(rotateAnimation);
                    rotationAngle = 0f;
                }
            }
        });

        ArrayList<PixysCategoryItem> coreTeam = getCoreTeam(getContext());
        PixysCategoryItemAdapter coreTeamAdapter = new PixysCategoryItemAdapter(coreTeam, getContext());
        coreTeamRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        coreTeamRecycler.setAdapter(coreTeamAdapter);
        coreTeamRecycler.setHasFixedSize(true);

        PixysCategoryItemAdapter devicesAdapter = new PixysCategoryItemAdapter(deviceMaintainers, getContext());
        devicesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        devicesRecycler.setAdapter(devicesAdapter);

        if (isConnected) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONArray devicesJson = PixysExtraUtils.parseDevicesJson(getContext());
                    deviceMaintainers.clear();
                    deviceMaintainers.addAll(parseDevicesJson(devicesJson));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PixysCategoryItemAdapter devicesUpdatedAdapter = new PixysCategoryItemAdapter(deviceMaintainers, getContext());
                            devicesRecycler.swapAdapter(devicesUpdatedAdapter, false);
                        }
                    });
                }
            }).start();
        }

        return view;
    }

    private ArrayList<PixysAboutItem> getDeviceMaintainers(ArrayList<JSONObject> devices) {
        ArrayList<PixysAboutItem> maintainers = new ArrayList<>();
        try {
            for (JSONObject device : devices) {
                maintainers.add(new PixysAboutItem(device.getString("name") + " (" + device.getString("codename") + ")",
                        device.getString("maintainers_name"), PIXYS_DEVICE_URL + device.getString("codename")));
            }
            maintainers.sort(new Comparator<PixysAboutItem>() {
                @Override
                public int compare(PixysAboutItem o1, PixysAboutItem o2) {
                    return o1.getTitle().compareToIgnoreCase(o2.getTitle());
                }
            });
        } catch (Exception e) {
            Log.e("PixysSettings", "getDeviceMaintainers: Failed to get device maintainers", e);
        }
        return maintainers;
    }

    private ArrayList<PixysCategoryItem> getCoreTeam(Context context) {
        JSONArray team = PixysExtraUtils.parseJsonFromAssets("core_team.json", context);
        ArrayList<PixysCategoryItem> categories = new ArrayList<>();
        try {
            for (int i = 0; i < team.length(); i++) {
                JSONObject category = team.getJSONObject(i);
                categories.add(new PixysCategoryItem(category.getString("category"), getMembers(category.getJSONArray("members"))));
            }
        } catch (Exception e) {
            Log.e("PixysSettings", "getCoreTeam: Failed to get Core team categories", e);
        }
        return categories;
    }

    private ArrayList<PixysAboutItem> getMembers(JSONArray arr) {
        ArrayList<PixysAboutItem> members = new ArrayList<>();
        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject member = arr.getJSONObject(i);
                members.add(new PixysAboutItem(member.getString("name"), member.getString("role"), member.getString("url")));
            }
        } catch (Exception e) {
            Log.e("PixysSettings", "getMembers: Failed to get Core team Members", e);
        }
        return members;
    }

    private ArrayList<PixysCategoryItem> parseDevicesJson(JSONArray devicesJson) {
        ArrayList<PixysCategoryItem> deviceMaintainers = new ArrayList<>();
        try {
            TreeMap<String, ArrayList<JSONObject>> devices = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
            for (int i = 0; i < devicesJson.length(); i++) {
                JSONObject device = devicesJson.getJSONObject(i);
                String brand = device.getString("brand");
                ArrayList<JSONObject> arr;
                if (devices.containsKey(brand)) {
                    arr = devices.get(brand);
                } else {
                    arr = new ArrayList<>();
                }
                arr.add(device);
                devices.put(brand, arr);
            }
            for (Map.Entry<String, ArrayList<JSONObject>> entry : devices.entrySet()) {
                String brand = entry.getKey();
                ArrayList<PixysAboutItem> maintainers = getDeviceMaintainers(entry.getValue());
                deviceMaintainers.add(new PixysCategoryItem(brand, maintainers));
            }
        } catch (Exception e) {
            Log.e("TAG", "parseDevicesJson: Failed to parse Devices JSON", e);
        }
        return deviceMaintainers;
    }

}

class PixysAboutItem {

    private String title;
    private String description;
    private String url;

    PixysAboutItem(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}

class PixysCategoryItem {

    private String subheader;
    private ArrayList<PixysAboutItem> items;

    PixysCategoryItem(String subheader, @NonNull ArrayList<PixysAboutItem> items) {
        this.subheader = subheader;
        this.items = items;
    }

    public String getSubheader() {
        return subheader;
    }

    public ArrayList<PixysAboutItem> getItems() {
        return items;
    }
}

class PixysCategoryItemAdapter extends RecyclerView.Adapter<PixysCategoryItemAdapter.PixysCategoryItemViewHolder> {

    private ArrayList<PixysCategoryItem> categories;
    private Context mContext;

    PixysCategoryItemAdapter(ArrayList<PixysCategoryItem> categories, Context mContext) {
        this.categories = categories;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PixysCategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PixysCategoryItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_pixys_about_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PixysCategoryItemViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class PixysCategoryItemViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView aboutItemRecycler;
        private TextView subheader;

        public PixysCategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);

            aboutItemRecycler = itemView.findViewById(R.id.pixys_about_category_recycler_view);
            subheader = itemView.findViewById(R.id.pixys_about_category_subheader);

            aboutItemRecycler.setLayoutManager(new LinearLayoutManager(mContext));
            aboutItemRecycler.setHasFixedSize(true);
        }

        void bindView(int position) {
            PixysCategoryItem category = categories.get(position);

            aboutItemRecycler.setAdapter(new PixysAboutItemAdapter(category.getItems(), mContext));
            subheader.setText(category.getSubheader());

            if (category.getSubheader().isEmpty()) {
                subheader.setVisibility(View.GONE);
            }
        }

    }

}

class PixysAboutItemAdapter extends RecyclerView.Adapter<PixysAboutItemAdapter.AboutItemViewHolder> {

    private ArrayList<PixysAboutItem> items;
    private Context mContext;

    PixysAboutItemAdapter(ArrayList<PixysAboutItem> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AboutItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AboutItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_pixys_about_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AboutItemViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class AboutItemViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout aboutItem;
        private TextView header;
        private TextView description;

        public AboutItemViewHolder(@NonNull View itemView) {
            super(itemView);
            aboutItem = itemView.findViewById(R.id.pixys_about_item);
            header = itemView.findViewById(R.id.header);
            description = itemView.findViewById(R.id.description);
        }

        void bindView(int position) {
            PixysAboutItem item = items.get(position);
            header.setText(item.getTitle());
            description.setText(item.getDescription());
            if (item.getTitle().isEmpty()) {
                header.setVisibility(View.GONE);
            }
            if (item.getDescription().isEmpty()) {
                description.setVisibility(View.GONE);
            }
            aboutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                    mContext.startActivity(urlIntent);
                }
            });
        }

    }

}