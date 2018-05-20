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
import kr.ac.sungshin.colleckingseoul.model.response.rank.UserRank;

/**
 * Created by kwonhyeon-a on 2018. 5. 16..
 */

public class UserRankAdapter extends RecyclerView.Adapter<UserRankAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserRank> listDatas;

    public UserRankAdapter(Context context, ArrayList<UserRank> listDatas) {
        this.context = context;
        this.listDatas = listDatas;
    }

    public void setAdapter(ArrayList<UserRank> listDatas) {
        this.listDatas = listDatas;
        notifyDataSetChanged();
    }

    @Override
    public UserRankAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_rank, parent, false);
        return new UserRankAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserRankAdapter.ViewHolder holder, int position) {
        UserRank item = listDatas.get(position);
        holder.idTextView.setText(item.getId());
        holder.nicknameTextView.setText(item.getNickname());
        Glide.with(context)
                .load(item.getUrl())
                .into(holder.profileImageView);
    }

    @Override
    public int getItemCount() {
        return (listDatas != null) ? listDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userrank_imageview_profile) ImageView profileImageView;
        @BindView(R.id.userrank_textview_nickname) TextView nicknameTextView;
        @BindView(R.id.userrank_textview_id) TextView idTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
