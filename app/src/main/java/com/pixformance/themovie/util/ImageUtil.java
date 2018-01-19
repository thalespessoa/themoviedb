package com.pixformance.themovie.util;

import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.pixformance.themovie.data.NetworkApi;
import com.squareup.picasso.Picasso;

/**
 * Created by thalespessoa on 1/19/18.
 */

public class ImageUtil {

    static public void loadImage(final ImageView imageView, final String path) {
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                int width = imageView.getWidth();
                if(width < 92) {
                    width = 92;
                } else if(width < 185) {
                    width = 185;
                } else if(width < 500) {
                    width = 500;
                } else {
                    width = 780;
                }
                Picasso.with(imageView.getContext())
                        .load(String.format(NetworkApi.IMAGE_PATH, width, path))
                        .fit().centerCrop()
                        .into(imageView);

                return true;
            }
        });
    }
}
