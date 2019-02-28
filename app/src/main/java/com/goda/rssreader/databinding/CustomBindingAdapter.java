package com.goda.rssreader.databinding;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.goda.rssreader.R;
import com.squareup.picasso.Picasso;

public class CustomBindingAdapter {
    @BindingAdapter("image")
    public static void setImage(ImageView view, String url) {
        if (view != null && !TextUtils.isEmpty(url)) {
            Picasso.with(view.getContext()).load(url)
                    .placeholder(R.drawable.no_image).error(R.drawable.no_image)
                    .into(view);
        }
    }

    @BindingAdapter("typeFace")
    public static void setTypeFace(View view, String type) {
        if (!TextUtils.isEmpty(type) && view instanceof TextView) {
            ((TextView) view).setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), type));
        }
    }
}
