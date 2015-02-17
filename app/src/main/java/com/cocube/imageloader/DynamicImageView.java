package com.cocube.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DynamicImageView extends ImageView {


    private static final int FADE_IN_TIME_MS = 250;


    public DynamicImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {

        boolean normal = false;
        if (normal) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        } else {
            final Drawable d = this.getDrawable();

            if (d != null) {
                /**
                 * 2014-03-22
                 * Because of this part, noimage.png should have same ratio as the actual view size
                 * You may have to check the height/width before making noimage.png.
                 */
                // ceil not round - avoid thin vertical gaps along the left/right edges
                final int width = MeasureSpec.getSize(widthMeasureSpec);
                final int height = (int) Math.ceil(width * (float) d.getIntrinsicHeight() / d.getIntrinsicWidth());
                this.setMeasuredDimension(width, height);

            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

    }


    @Override
    public void setImageBitmap(Bitmap bm) {

        TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                new ColorDrawable(android.R.color.transparent),
                new BitmapDrawable(getContext().getResources(), bm)
        });

        setImageDrawable(td);
        td.startTransition(FADE_IN_TIME_MS);
    }
}