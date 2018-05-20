package kr.ac.sungshin.colleckingseoul.rank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import kr.ac.sungshin.colleckingseoul.model.response.rank.LandmarkRank;

/**
 * Created by gwonhyeon-a on 2018. 5. 20..
 */

public class LandmarkRankAdapter extends RecyclerView.Adapter<LandmarkRankAdapter.ViewHolder> {
    private Context context;
    private ArrayList<LandmarkRank> listDatas;
    View.OnClickListener mOnClickListener;

    public LandmarkRankAdapter(Context context, ArrayList<LandmarkRank> listDatas, View.OnClickListener mOnClickListener) {
        this.context = context;
        this.listDatas = listDatas;
        this.mOnClickListener = mOnClickListener;
    }

    public void setAdapter(ArrayList<LandmarkRank> listDatas) {
        this.listDatas = listDatas;
        notifyDataSetChanged();
    }

    @Override
    public LandmarkRankAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_landmark_rank, parent, false);
        view.setOnClickListener(mOnClickListener);
        return new LandmarkRankAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LandmarkRankAdapter.ViewHolder holder, int position) {
        LandmarkRank item = listDatas.get(position);
        holder.nameTextView.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return (listDatas != null) ? listDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.landmarkrank_textview_name)
        TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
