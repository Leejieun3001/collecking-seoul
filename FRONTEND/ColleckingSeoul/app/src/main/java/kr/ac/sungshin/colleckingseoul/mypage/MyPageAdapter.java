package kr.ac.sungshin.colleckingseoul.mypage;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.Review.BoardItem;
import kr.ac.sungshin.colleckingseoul.model.response.rank.UserRank;
import kr.ac.sungshin.colleckingseoul.sqLite.Landmark;

/**
 * Created by LG on 2018-05-17.
 */

public class MyPageAdapter extends RecyclerView.Adapter<MyPageAdapter.ViewHolder> {
    private final String TAG = "MyPageAdapter";
    private Context context;
    private ArrayList<Landmark> list;
    View.OnClickListener mOnClickListener;

    public MyPageAdapter(Context context, ArrayList<Landmark> list, View.OnClickListener mOnClickListener) {
        this.context = context;
        this.list = list;
        this.mOnClickListener = mOnClickListener;
    }

    public void setAdapter(ArrayList<Landmark> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    @Override
    public MyPageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_landmark, parent, false);
        view.setOnClickListener(mOnClickListener);
        return new MyPageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyPageAdapter.ViewHolder holder, int position) {
        Landmark item = list.get(position);
        holder.textViewMyLandmarkName.setText(item.getName());
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getIdx();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMyLandmarkName;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewMyLandmarkName = (TextView) itemView.findViewById(R.id.my_landmark_textview_name);
        }
    }

}
