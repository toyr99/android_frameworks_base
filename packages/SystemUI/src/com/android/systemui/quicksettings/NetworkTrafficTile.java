/*
 * Copyright (C) 2013 The Mahdi-Rom Project
 * Modifications Copyright (C) 2014 The NamelessRom Project
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

package com.android.systemui.quicksettings;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import com.android.systemui.R;
import com.android.systemui.statusbar.phone.QuickSettingsContainerView;
import com.android.systemui.statusbar.phone.QuickSettingsController;

public class NetworkTrafficTile extends QuickSettingsTile {

    private int mTraffic;
    private boolean mTrafficOff;
    private boolean mTrafficUp;
    private boolean mTrafficDown;
    private boolean mTrafficBoth;

    public NetworkTrafficTile(Context context, QuickSettingsController qsc) {
        super(context, qsc);

        mOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTraffic == 0) {
                    mTraffic = 3;
                } else {
                    mTraffic -= 1;
                }
                saveChanges();
            }
        };
        qsc.registerObservedContent(Settings.System.getUriFor(Settings.System.NETWORK_TRAFFIC_STATE), this);
    }

    private void saveChanges() {
        Settings.System.putIntForUser(mContext.getContentResolver(),
                Settings.System.NETWORK_TRAFFIC_STATE, mTraffic,
                UserHandle.USER_CURRENT);
    }

    @Override
    void onPostCreate() {
        updateTile();
        super.onPostCreate();
    }

    @Override
    public void updateResources() {
        updateTile();
        super.updateResources();
    }

    private synchronized void updateTile() {
        mTrafficOff = Settings.System.getIntForUser(mContext.getContentResolver(),
                Settings.System.NETWORK_TRAFFIC_STATE, 0, UserHandle.USER_CURRENT) == 0;
        mTrafficUp = Settings.System.getIntForUser(mContext.getContentResolver(),
                Settings.System.NETWORK_TRAFFIC_STATE, 0, UserHandle.USER_CURRENT) == 1;
        mTrafficDown = Settings.System.getIntForUser(mContext.getContentResolver(),
                Settings.System.NETWORK_TRAFFIC_STATE, 0, UserHandle.USER_CURRENT) == 2;
        mTrafficBoth = Settings.System.getIntForUser(mContext.getContentResolver(),
                Settings.System.NETWORK_TRAFFIC_STATE, 0, UserHandle.USER_CURRENT) == 3;

        if (mTrafficOff) {
            mDrawable = R.drawable.ic_qs_network_traffic_off;
                mLabel = mContext.getString(R.string.quick_settings_network_traffic_off);
        } else if (mTrafficUp) {
            mDrawable = R.drawable.ic_qs_network_traffic_up;
                mLabel = mContext.getString(R.string.quick_settings_network_traffic_up);
        } else if (mTrafficDown) {
            mDrawable = R.drawable.ic_qs_network_traffic_down;
                mLabel = mContext.getString(R.string.quick_settings_network_traffic_down);
        } else if (mTrafficBoth) {
            mDrawable = R.drawable.ic_qs_network_traffic_on;
                mLabel = mContext.getString(R.string.quick_settings_network_traffic_on);
        }
    }

    @Override
    public void onChangeUri(ContentResolver resolver, Uri uri) {
        updateResources();
    }

}
