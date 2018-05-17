package kr.ac.sungshin.colleckingseoul.mypage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.ac.sungshin.colleckingseoul.model.response.rank.UserRank;

/**
 * Created by LG on 2018-05-17.
 */

public class MyPageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MyVisitItem> list = new ArrayList<>();

    public MyPageAdapter(Context context, ArrayList<MyVisitItem> list) {
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
