package es.us.eii.sugus.sugusnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by guilledelacruz on 10/05/15.
 */
public class AdapterMember extends ArrayAdapter<Member>{

    private Member[] members;

    public AdapterMember(Context context, Member[] m) {
        super(context, R.layout.adapter_member, m);
        members = m;
    }

    public View getView(int position, View convertView, ViewGroup group){

        View v = convertView;
        ViewHolder vh;

        if (v == null){

            LayoutInflater lay = LayoutInflater.from(getContext());
            v = lay.inflate(R.layout.adapter_member, null);
            vh = new ViewHolder();

            vh.name = (TextView) v.findViewById(R.id.membername);

            v.setTag(vh);

        } else {
            vh = (ViewHolder) v.getTag();
        }

        vh.name.setText(members[position].getName());

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

    public static Member[] getAllMembers(List<Member> mems){
        Member[] m = new Member[1];

        if (mems.isEmpty() || mems == null){
            m[0] = new Member();
        }else{
            m = new Member[mems.size()];
            for(int i = 0; i < mems.size(); i++){
                m[i] = mems.get(i);
            }
        }

        return m;
    }
}
