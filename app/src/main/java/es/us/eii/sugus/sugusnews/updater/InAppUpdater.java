package es.us.eii.sugus.sugusnews.updater;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.future.ResponseFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

import es.us.eii.sugus.sugusnews.R;


public class InAppUpdater extends ActionBarActivity {

    private TextView mTvVersion;
    private Button mBtnUpdate;
    private final String ApkUrl = "https://raw.githubusercontent.com/SUGUS-GNULinux/SugusNews/master/binaries/app-release.apk";
    private String mLatestAppVersion;
    private Context context;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inappupdater);

        this.mTvVersion = (TextView) findViewById(R.id.tvVersion);
        this.mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        this.mProgressBar =(ProgressBar) findViewById(R.id.progressBar);
        this.context = this;

        Intent mIntent = getIntent();
        this.mLatestAppVersion = mIntent.getStringExtra("LatestAppVersion");

        mTvVersion.setText("Versión disponible: " + this.mLatestAppVersion);

        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ion.with(context)
                        .load(ApkUrl)
                        .progressBar(mProgressBar)
                        .write(new File(Environment.getExternalStorageDirectory().toString() + "/download/sugus-" + mLatestAppVersion + ".apk"))
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File file) {
                                if(e==null){
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(context, "Fallo al descargar", Toast.LENGTH_SHORT).show();
                                    mBtnUpdate.setText("Reintentar");
                                    mProgressBar.setProgress(0);
                                }

                            }
                        });
            }
        });


    }


}
