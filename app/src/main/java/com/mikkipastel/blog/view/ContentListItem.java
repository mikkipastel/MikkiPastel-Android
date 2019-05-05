package com.mikkipastel.blog.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.mikkipastel.blog.R;

/**
 * Created by acer on 4/10/2016.
 */
public class ContentListItem extends BaseCustomViewGroup {

    TextView contentTag, contentTopic, contentDate;
    ImageView contentCover;

    int width;

    public ContentListItem(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public ContentListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public ContentListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public ContentListItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.layout_showcontent, this);
    }

    private void initInstances() {
        // findViewById here
        contentCover = (ImageView)findViewById(R.id.cover_pic);
        contentTag = (TextView)findViewById(R.id.tag);
        contentTopic = (TextView)findViewById(R.id.topic_name);
        contentDate = (TextView)findViewById(R.id.publish_date);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec); //width in px
        //int height = width * 2 / 3;
        //int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        //child view
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //self
        //setMeasuredDimension(width, height);
    }*/

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        width = contentCover.getWidth();
        contentCover.setMaxHeight(width * 2 / 3);
    }

    public void SetContentTopic(String name){
        contentTopic.setText(name);
    }

    public void SetContentTag(String name){
        contentTag.setText(name);
    }

    public void SetContentDate(String name){
        contentDate.setText(name);
    }

    public void setContentCoverUrl(String url){
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.loading) //show drawable between loading pic
                .error(R.drawable.image_cover) //error for load pic
                .centerCrop()
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(contentCover);
    }

}
