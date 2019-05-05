package com.mikkipastel.blog.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.mikkipastel.blog.DAO.Item;
import com.mikkipastel.blog.DAO.MikkiBlog;
import com.mikkipastel.blog.R;
import com.mikkipastel.blog.manager.ContentListManager;
import com.mikkipastel.blog.view.ContentListItem;

/**
 * Created by acer on 4/9/2016.
 */
public class listAdapter extends BaseAdapter{

    MikkiBlog dao;

    int lastPosition = -1;

    public void setDao(MikkiBlog dao) {
        this.dao = dao;
    }

    public MikkiBlog getDao() {
        return dao;
    }

    @Override
    public int getCount() {
        if (ContentListManager.getInstance().getDao() == null)
            return 0;
        if (ContentListManager.getInstance().getDao().getItems() == null)
            return 0;
        return ContentListManager.getInstance().getDao().getItems().size();
        /*if (dao == null)
            return 0;
        if (dao.getItems() == null)
            return 0;
        return dao.getItems().size();*/
    }

    @Override
    public Object getItem(int position) {
        return ContentListManager.getInstance().getDao().getItems().get(position);
        //return dao.getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContentListItem blogitem;
        if (convertView != null)
            blogitem = (ContentListItem)convertView;
        else
            blogitem = new ContentListItem(parent.getContext());

        Item dao = (Item) getItem(position);
        blogitem.SetContentTopic(dao.getTitle());
        blogitem.SetContentTag(dao.getLabels().toString());
        blogitem.SetContentDate("Published : " + dao.getPublished());
        blogitem.setContentCoverUrl(dao.getImages().get(0).getUrl());

        if (position > lastPosition) {
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(),
                    R.anim.up_from_bottom);
            blogitem.startAnimation(anim);
            lastPosition = position;
        }


        return blogitem;
    }
}
