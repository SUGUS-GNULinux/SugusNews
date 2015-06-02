package es.us.eii.sugus.sugusnews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.us.eii.sugus.sugusnews.preferences.PreferencesManager;
import es.us.eii.sugus.sugusnews.R;
import es.us.eii.sugus.sugusnews.adapters.AdapterMember;
import es.us.eii.sugus.sugusnews.adapters.AdapterNew;
import es.us.eii.sugus.sugusnews.models.Member;
import es.us.eii.sugus.sugusnews.models.New;
import es.us.eii.sugus.sugusnews.updater.UpdateFinder;
import es.us.eii.sugus.sugusnews.utils.MemberParserJSoup;
import es.us.eii.sugus.sugusnews.utils.NewsParserDOM;


public class MainActivity extends AppCompatActivity {

    private List<New> nt = new ArrayList<>();
    private List<Member> ms = new ArrayList<>();
    private AdapterNew nots;
    private AdapterMember mems;
    private Context context;

    private PreferencesManager preferencesManager;

    private String appVersion;

    private final String appVersionUrl = "https://github.com/SUGUS-GNULinux/SugusNews/blob/master/README.md";
    //private final String changelogUrl = "https://raw.githubusercontent.com/SUGUS-GNULinux/SugusNews/master/CHANGELOG";
    private final String changelogUrl = "https://raw.githubusercontent.com/SUGUS-GNULinux/SugusNews/dev/CHANGELOG"; //for testing

    @InjectView(R.id.lv_news_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayoutNews;
    @InjectView(R.id.lv_members_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayoutMembers;
    @InjectView(R.id.tabHost)
    TabHost mTabHost;
    @InjectView(R.id.listviewmembers)
    ListView mLvMembers;
    @InjectView(R.id.listviewnews)
    ListView mLvNews;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setToolbar();



        New[] no = {new New()};
        Member[] me = {new Member()};
        nots = new AdapterNew(getApplicationContext(), no);
        mems = new AdapterMember(getApplicationContext(), me);

        preferencesManager = new PreferencesManager(this);

        context=this;



        mLvMembers.setAdapter(mems);
        mLvNews.setAdapter(nots);


        Resources res = getResources();


        mTabHost.setup();

        TabHost.TabSpec spec=mTabHost.newTabSpec("News");
        spec.setContent(R.id.tab1);

        spec.setIndicator("News", res.getDrawable(android.R.drawable.ic_menu_agenda));
        mTabHost.addTab(spec);

        spec=mTabHost.newTabSpec("Alguien en Sugus?");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Alguien en Sugus?", res.getDrawable(android.R.drawable.ic_menu_myplaces));
        mTabHost.addTab(spec);

        mTabHost.setCurrentTab(0);


        mLvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView mTvTitle = (TextView) view.findViewById(R.id.newstitulo);


                if (!mTvTitle.getText().toString().equals("No hay noticias disponibles")) {

                    New n = (New) parent.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, NewsView.class);
                    intent.putExtra("title", n.getTitulo());
                    intent.putExtra("link", n.getLink());
                    intent.putExtra("date", " " + n.getFecha());
                    intent.putExtra("pub", " " + n.getPublicador());
                    intent.putExtra("description", n.getDescripcion());

                    startActivity(intent);
                }
            }
        });



        mSwipeRefreshLayoutNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    CargarRssTask tareaNews = new CargarRssTask();
                    tareaNews.execute();
                } else {
                    Toast.makeText(context, "No hay conexión a internet", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayoutNews.setRefreshing(false);
                }
            }
        });

        mSwipeRefreshLayoutMembers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    CargarHtmlTask tareaMembers = new CargarHtmlTask();
                    tareaMembers.execute();
                } else {
                    Toast.makeText(context, "No hay conexión a internet", Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayoutMembers.setRefreshing(false);
                }
            }
        });





        if(isNetworkAvailable()){
            CargarRssTask tareaNews = new CargarRssTask();
            tareaNews.execute();

            CargarHtmlTask tareaMembers = new CargarHtmlTask();
            tareaMembers.execute();

            searchUpdates(appVersionUrl);

          /*  if(preferencesManager.getVersion()==null || !preferencesManager.getVersion().equals(appVersion)){   //improve in the future checking the changelog correspond with the actual version
                String changelog = getChangelog();

                if(changelog!=null){
                    new MaterialDialog.Builder(this)
                            .title("¿Qué hay de nuevo?")
                            .content(changelog)
                            .positiveText("Aceptar")
                            .show();

                    preferencesManager.setVersion(appVersion);
                }

            }*/

        }else{
            Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_LONG).show();
        }




    }


    private void setToolbar(){
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);

    }

    private String getChangelog(){

        String res = null;

        try {
            res = Ion.with(this).load(changelogUrl).asString().get().toString();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Ocurrió un error al obtener los cambios", Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            Toast.makeText(this, "Ocurrió un error al obtener los cambios", Toast.LENGTH_LONG).show();
        }

        return res;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void searchUpdates(String url) {
        try {
            this.appVersion = getPackageManager().getPackageInfo("es.us.eii.sugus.sugusnews",0).versionName.trim();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new UpdateFinder(this,appVersion,url).execute();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_acerca) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_test) {
            String changelog = getChangelog();
            new MaterialDialog.Builder(this)
                    .title("¿Qué hay de nuevo?")
                    .content(changelog)
                    .positiveText("Aceptar")
                    .show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private class CargarHtmlTask extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            Boolean result = false;
            MemberParserJSoup parse = new MemberParserJSoup();

            ms = parse.parse();
            if (ms != null) {
                mems = new AdapterMember(getApplicationContext(), AdapterMember.getAllMembers(ms));
                result = true;
            }else {
                ms = new ArrayList<Member>();
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                mLvMembers.setAdapter(mems);
            }
            mSwipeRefreshLayoutMembers.setRefreshing(false);
        }
    }

    private class CargarRssTask extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            Boolean result = false;
            NewsParserDOM parse = new NewsParserDOM();

            nt = parse.parse();
            if (nt != null) {
                nots = new AdapterNew(getApplicationContext(), AdapterNew.getAllNews(nt));
                result = true;
            }else{
                nt = new ArrayList<New>();
            }

            return result;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                mLvNews.setAdapter(nots);
            }
            mSwipeRefreshLayoutNews.setRefreshing(false);
        }
    }
}
