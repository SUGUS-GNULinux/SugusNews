package es.us.eii.sugus.sugusnews.updater;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.koushikdutta.ion.Ion;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

/**
 * Created by alejandrohall on 22/05/15.
 */
public class UpdateFinder extends AsyncTask {

    private String oldAppVersion;
    private String LatestAppVersion;
    private String appVersionUrl;
    private Context context;

    private Boolean update;

    public UpdateFinder(Context context, String oldAppVersion, String appVersionUrl){
        this.context = context;
        this.oldAppVersion = oldAppVersion;
        this.appVersionUrl = appVersionUrl;
    }


    @Override
    protected void onPostExecute(Object o) {
        if(update){
            Intent mIntent = new Intent(context, InAppUpdater.class);
            mIntent.putExtra("LatestAppVersion", this.LatestAppVersion);
            context.startActivity(mIntent);
        }
    }



    @Override
    protected Boolean doInBackground(Object[] objects) {

        this.LatestAppVersion = getLatestAppVersion(appVersionUrl);

        this.update = compareVersion(oldAppVersion, LatestAppVersion);


        return true;
    }




    public String getLatestAppVersion(String url){

        String aux2=null;

        String mRawHtmlAppVersion = getHtml(url); //improve in the next version with the raw text url.

        String mRawHtmlAppVersionSplitted[] =  mRawHtmlAppVersion.split(">");

        for(int i=0; i<mRawHtmlAppVersionSplitted.length;i++){
            if(mRawHtmlAppVersionSplitted[i].startsWith("Version:")){
                String aux[] = mRawHtmlAppVersionSplitted[i].split(":");
                aux2 = aux[1].trim().substring(0,5).toString();
                break;

            }
        }


        return aux2;
    }


    public String getHtml(String url){

        String result = null;
        try {
            result = Ion.with(context).load(url).asString().get().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;

    }


    private Boolean compareVersion(String oldVersion, String newVersion) {
        Boolean res = false;

        Integer latest = Integer.valueOf(newVersion.replace(".",""));
        Integer old = Integer.valueOf(oldVersion.replace(".", ""));

        if(latest>old){
            res = true;
        }

        return res;
    }



}
