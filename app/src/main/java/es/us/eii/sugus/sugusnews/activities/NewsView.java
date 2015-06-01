package es.us.eii.sugus.sugusnews.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.us.eii.sugus.sugusnews.R;

/**
 * Created by guilledelacruz on 18/05/15.
 */
public class NewsView extends AppCompatActivity {

    @InjectView(R.id.newsviewtitle)
    TextView mTvTitle;
    @InjectView(R.id.newsviewdate)
    TextView mTvDate;
    @InjectView(R.id.newsviewpub)
    TextView mTvPub;
    @InjectView(R.id.newsviewdescription)
    TextView mTvDescription;
    @InjectView(R.id.newsviewlink)
    TextView mTvLink;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsview);
        ButterKnife.inject(this);
        setToolbar();

        Intent data = getIntent();

        mTvTitle.setText(data.getExtras().get("title").toString());
        mTvDate.setText(data.getExtras().get("date").toString());
        mTvPub.setText(data.getExtras().get("pub").toString());
        mTvDescription.setText(Html.fromHtml(data.getExtras().get("description").toString()));
        mTvLink.setText(data.getExtras().getString("link").toString());

    }

    private void setToolbar(){
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);

    }

}
