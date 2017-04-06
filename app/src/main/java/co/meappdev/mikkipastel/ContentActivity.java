package co.meappdev.mikkipastel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import co.meappdev.mikkipastel.fragment.ContentFragment;

/**
 * Created by acer on 4/30/2016.
 */
public class ContentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        if (savedInstanceState == null) {
            //1st created
            //place fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, ContentFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            //onSharedContent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onListItemClick() {
        finish();
        getSupportFragmentManager().beginTransaction()
                .remove(ContentFragment.newInstance())
                .commit();
    }

    /*private void onSharedContent() {
        // TODO Auto-generated method stub
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        String weburl = myWebView.getUrl();
        String webtitle = myWebView.getTitle();
        String shared_web = webtitle + " " + weburl;
        share.putExtra(Intent.EXTRA_TEXT, shared_web);
        startActivity(Intent.createChooser(share, "Share link"));
    }*/
}
