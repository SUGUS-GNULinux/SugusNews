package es.us.eii.sugus.sugusnews.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.us.eii.sugus.sugusnews.R;

/**
 * Created by guilledelacruz on 19/05/15.
 */
public class About extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.aboutapp)
    TextView mTvAboutApp;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);
        setToolbar();

        mTvAboutApp.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setToolbar(){
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);

    }
}
