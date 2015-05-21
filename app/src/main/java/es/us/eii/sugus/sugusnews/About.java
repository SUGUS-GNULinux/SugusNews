package es.us.eii.sugus.sugusnews;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * Created by guilledelacruz on 19/05/15.
 */
public class About extends Activity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final TextView aboutapp = (TextView) findViewById(R.id.aboutapp);
        aboutapp.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){

        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }

        return false;
    }
}
