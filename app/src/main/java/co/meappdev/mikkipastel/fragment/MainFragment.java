package co.meappdev.mikkipastel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;

import co.meappdev.mikkipastel.DAO.Item;
import co.meappdev.mikkipastel.DAO.MikkiBlog;
import co.meappdev.mikkipastel.R;
import co.meappdev.mikkipastel.adapter.listAdapter;
import co.meappdev.mikkipastel.manager.ContentListManager;
import co.meappdev.mikkipastel.manager.Contextor;
import co.meappdev.mikkipastel.manager.HttpManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by acer on 4/30/2016.
 */
public class MainFragment extends Fragment {

    ListView myListView;
    listAdapter adapter;
	
	SwipeRefreshLayout swipeRefreshLayout;

    String banner_ad_unit_id = "ca-app-pub-0901553226036625~7699909716";

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //init(savedInstanceState);
        initInstances(rootView);
        return rootView;
    }

    /*private void init(Bundle savedInstanceState) {
        //internal storage : file
        //File dir = getContext().getFilesDir(); //file
        File dir = getContext().getCacheDir(); //cache
        Log.d("Storage", String.valueOf(dir));
        File file = new File(dir, "testfile.log");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write("hello".getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void initInstances(View rootView){
        myListView = (ListView)rootView.findViewById(R.id.mycontent_list);
        adapter = new listAdapter();
        myListView.setAdapter(adapter);

        myListView.setOnItemClickListener(listViewItemClickListener);
		
		swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        //swipeRefreshLayout.setColorSchemeColors(android.R.color.black,
        //       android.R.color.holo_red_dark);

        myListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            }
        });

        reloadData();

        //add admob
        MobileAds.initialize(getContext(), banner_ad_unit_id);
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void reloadData() {
        Call<MikkiBlog> call = HttpManager.getInstance().getService().loadContentList();
        call.enqueue(new Callback<MikkiBlog>() {
            @Override
            public void onResponse(Call<MikkiBlog> call, Response<MikkiBlog> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()){
                    MikkiBlog collection = response.body();
                    ContentListManager.getInstance().setDao(collection);
                    //adapter.setDao(collection);
                    adapter.notifyDataSetChanged();
                    /*Toast.makeText(getContext(),
                            collection.getItems().get(0).getTitle(),
                            Toast.LENGTH_SHORT)
                            .show();*/
                } else {
                    try { //404 not found
                        Toast.makeText(Contextor.getInstance().getContext(),
                                response.errorBody().string(),
                                Toast.LENGTH_SHORT)
                                .show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MikkiBlog> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(Contextor.getInstance().getContext(),
                        t.toString(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    final AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position < ContentListManager.getCount()){
                Item content = ContentListManager.getInstance().getDao().getItems().get(position);
                //Item content = adapter.getDao().getItems().get(position);
                ContentFragment.FragmentListener listener = (ContentFragment.FragmentListener) getActivity();
                listener.onListItemClick(content, position);
            }
        }
    };
}
