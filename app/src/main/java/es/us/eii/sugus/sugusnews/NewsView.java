package es.us.eii.sugus.sugusnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * Created by guilledelacruz on 18/05/15.
 */
public class NewsView extends ActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsview);

        final TextView title = (TextView) findViewById(R.id.newsviewtitle);
        final TextView date = (TextView) findViewById(R.id.newsviewdate);
        final TextView pub = (TextView) findViewById(R.id.newsviewpub);
        final TextView desc = (TextView) findViewById(R.id.newsviewdescription);
        final TextView link = (TextView) findViewById(R.id.newsviewlink);

        Intent data = getIntent();

        title.setText(data.getExtras().get("title").toString());
        date.setText(data.getExtras().get("date").toString());
        pub.setText(data.getExtras().get("pub").toString());
        desc.setText(Html.fromHtml(data.getExtras().get("description").toString()));
        link.setText(data.getExtras().getString("link").toString());

    }

    public boolean onKeyDown(int keyCode, KeyEvent event){

        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }

        return false;
    }

}
