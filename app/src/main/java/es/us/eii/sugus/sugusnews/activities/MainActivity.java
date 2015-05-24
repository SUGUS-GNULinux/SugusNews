package es.us.eii.sugus.sugusnews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.us.eii.sugus.sugusnews.R;
import es.us.eii.sugus.sugusnews.adapters.AdapterMember;
import es.us.eii.sugus.sugusnews.adapters.AdapterNew;
import es.us.eii.sugus.sugusnews.models.Member;
import es.us.eii.sugus.sugusnews.models.New;
import es.us.eii.sugus.sugusnews.updater.InAppUpdater;
import es.us.eii.sugus.sugusnews.updater.UpdateFinder;
import es.us.eii.sugus.sugusnews.utils.MemberParserJSoup;
import es.us.eii.sugus.sugusnews.utils.NewsParserDOM;


public class MainActivity extends ActionBarActivity {

    private List<New> nt = new ArrayList<New>();
    private List<Member> ms = new ArrayList<Member>();
    private AdapterNew nots;
    private AdapterMember mems;

    private String appVersion;

    private String appVersionUrl = "https://github.com/SUGUS-GNULinux/SugusNews/blob/master/README.md";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        New[] no = {new New()};
        Member[] me = {new Member()};
        nots = new AdapterNew(getApplicationContext(), no);
        mems = new AdapterMember(getApplicationContext(), me);
        final ListView m = (ListView) findViewById(R.id.listviewmembers);
        final ListView n = (ListView) findViewById(R.id.listviewnews);

        m.setAdapter(mems);
        n.setAdapter(nots);



        Resources res = getResources();

        TabHost tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("News");
        spec.setContent(R.id.tab1);

        spec.setIndicator("News", res.getDrawable(android.R.drawable.ic_menu_agenda));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("Alguien en Sugus?");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Alguien en Sugus?", res.getDrawable(android.R.drawable.ic_menu_myplaces));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);


        n.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView title = (TextView) view.findViewById(R.id.newstitulo);

                if (!title.getText().toString().equals("No hay noticias disponibles")) {

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

        if(isNetworkAvailable()){
            CargarHtmlTask tareaMembers = new CargarHtmlTask();
            tareaMembers.execute();

            CargarRssTask tareaNews = new CargarRssTask();
            tareaNews.execute();

            searchUpdates(appVersionUrl);
        }else{
            Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_LONG).show();
        }




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
        if (id == R.id.reload) {
            if(isNetworkAvailable()){
                CargarHtmlTask tareaMembers = new CargarHtmlTask();
                tareaMembers.execute();

                CargarRssTask tareaNews = new CargarRssTask();
                tareaNews.execute();

            }else{
                Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_LONG).show();
            }
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
                final ListView viewmembers = (ListView) findViewById(R.id.listviewmembers);
                viewmembers.setAdapter(mems);
            }
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
                final ListView viewnews = (ListView) findViewById(R.id.listviewnews);
                viewnews.setAdapter(nots);
            }
        }
    }
}
