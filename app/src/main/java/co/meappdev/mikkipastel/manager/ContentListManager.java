package co.meappdev.mikkipastel.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import co.meappdev.mikkipastel.DAO.Item;
import co.meappdev.mikkipastel.DAO.MikkiBlog;

/**
 * Created by acer on 4/10/2016.
 */
public class ContentListManager {

    private static ContentListManager instance;

    public static  ContentListManager getInstance() {
        if (instance == null)
            instance = new ContentListManager();
        return instance;
    }

    private Context mContext;
    private MikkiBlog dao;

    private ContentListManager() {
        mContext = Contextor.getInstance().getContext();
        //load data from persistent storage : mContext should be not null
        //loadCache();
    }

    public MikkiBlog getDao() {
        return dao;
    }

    public void setDao(MikkiBlog dao) {
        this.dao = dao;
        //save to persistent storage
        //saveCache();
    }

    public int getMaximumId() {
        if (dao == null)
            return 0;
        if (dao.getItems() == null)
            return 0;
        if (dao.getItems().size() == 0)
            return 0;
        int maxId = Integer.parseInt(dao.getItems().get(0).getId());
        for (int i = 1; i < dao.getItems().size(); i++) {
            maxId = Math.max(maxId, Integer.parseInt(dao.getItems().get(0).getId()));
        }
        return maxId;
    }

    public void insertDaoAtTopPosition(MikkiBlog newDao) {
        if (dao == null)
            dao = new MikkiBlog();
        if (dao.getItems() == null)
            dao.setItems(new ArrayList<Item>());
        dao.getItems().addAll(0, newDao.getItems());
        //save to persistent storage
        saveCache();
    }

    public void appendDaoAtBottomPosition(MikkiBlog newDao) {
        if (dao == null)
            dao = new MikkiBlog();
        if (dao.getItems() == null)
            dao.setItems(new ArrayList<Item>());
        dao.getItems().addAll(0, newDao.getItems());
        //save to persistent storage
        saveCache();
    }

    public static int getCount(){
        if (ContentListManager.getInstance().getDao() == null)
            return 0;
        if (ContentListManager.getInstance().getDao().getItems() == null)
            return 0;
        return ContentListManager.getInstance().getDao().getItems().size();
    }

    /*public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("dao", dao);
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        dao = savedInstanceState.getParcelable("dao");
    }*/

    private void saveCache() {
        MikkiBlog cacheDao = new MikkiBlog();
        if (dao != null && dao.getItems() != null)
            //cacheDao.setItems(dao.getItems().subList(0, Math.min(50, dao.getItems().size())));
            cacheDao.setItems(dao.getItems().subList(0, 50));
        String json = new Gson().toJson(cacheDao);

        SharedPreferences prefs = mContext.getSharedPreferences("photo",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("json", json);
        editor.apply();
    }

    private void loadCache() {
        SharedPreferences prefs = mContext.getSharedPreferences("photo",
                Context.MODE_PRIVATE);
        String json = prefs.getString("json", null);
        if (json == null)
            return;
        dao = new Gson().fromJson(json, MikkiBlog.class);
    }
}
