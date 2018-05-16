package kr.ac.sungshin.colleckingseoul.rank;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.ac.sungshin.colleckingseoul.model.response.rank.UserRank;

/**
 * Created by gwonhyeon-a on 2018. 5. 16..
 */

public class UserRankAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<UserRank> list = new ArrayList<>();

    public UserRankAdapter(Context context, ArrayList<UserRank> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
