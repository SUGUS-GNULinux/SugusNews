package es.us.eii.sugus.sugusnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.us.eii.sugus.sugusnews.models.New;
import es.us.eii.sugus.sugusnews.R;

/**
 * Created by guilledelacruz on 10/05/15.
 */
public class AdapterNew extends ArrayAdapter<New> {

    private New[] noticias;

    public AdapterNew(Context context, New[] n) {
        super(context, R.layout.adapter_news, n);
        noticias = n;
    }

    public View getView(int position, View convertView, ViewGroup group){

        View v = convertView;
        ViewHolder vh;

        if (v == null){

            LayoutInflater lay = LayoutInflater.from(getContext());
            v = lay.inflate(R.layout.adapter_news, null);
            vh = new ViewHolder();

            vh.titulo = (TextView) v.findViewById(R.id.newstitulo);
            vh.date = (TextView) v.findViewById(R.id.newsdate);
            vh.pub = (TextView) v.findViewById(R.id.newspub);

            v.setTag(vh);

        } else {
            vh = (ViewHolder) v.getTag();
        }

        vh.titulo.setText(noticias[position].getTitulo());
        vh.date.setText(noticias[position].getFecha());
        vh.pub.setText(noticias[position].getPublicador());

        return v;
    }

    static class ViewHolder {
        TextView titulo;
        TextView date;
        TextView pub;
    }

    public static New[] getAllNews(List<New> news){
        New[] n = new New[1];

        if (news.isEmpty() || news == null){
            n[0] = new New();
        }else{
            n = new New[news.size()];
            for(int i = 0; i < news.size(); i++){
                n[i] = news.get(i);
            }
        }

        return n;
    }
}
