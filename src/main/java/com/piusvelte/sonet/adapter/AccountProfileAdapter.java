package com.piusvelte.sonet.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.piusvelte.sonet.R;
import com.piusvelte.sonet.loader.AccountsProfilesLoader;
import com.piusvelte.sonet.util.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bemmanuel on 5/16/15.
 */
public class AccountProfileAdapter extends SimpleAdapter {

    private Context mContext;
    private Picasso mPicasso;

    public AccountProfileAdapter(Context context,
            List<? extends Map<String, ?>> data,
            int resource,
            String[] from,
            int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        mPicasso = Picasso.with(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.account_profile, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.profile = (ImageView) convertView.findViewById(R.id.profile);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder != null) {
            HashMap<String, String> account = (HashMap<String, String>) getItem(position);
            String url = account.get(AccountsProfilesLoader.PROFILE);

            if (!TextUtils.isEmpty(url)) {
                mPicasso.load(account.get(AccountsProfilesLoader.PROFILE))
                        .transform(new CircleTransformation())
                        .into(viewHolder.profile);
            } else {
                mPicasso.load(R.drawable.ic_account_box_grey600_48dp)
                        .transform(new CircleTransformation())
                        .into(viewHolder.profile);
            }

            mPicasso.load(Integer.valueOf(account.get(AccountsProfilesLoader.ICON)))
                    .into(viewHolder.icon);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView profile;
        ImageView icon;
    }
}