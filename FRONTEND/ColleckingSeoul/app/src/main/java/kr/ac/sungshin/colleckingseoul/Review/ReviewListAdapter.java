package kr.ac.sungshin.colleckingseoul.Review;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;

/**
 * Created by kwonhyeon-a on 2018. 4. 19..
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ReviewListItem> listDatas;
    View.OnClickListener mOnClickListener;

    public ReviewListAdapter(Context context, ArrayList<ReviewListItem> listDatas, View.OnClickListener mOnClickListener) {
        this.context = context;
        this.listDatas = listDatas;
        this.mOnClickListener = mOnClickListener;
    }

    public void setAdapter(ArrayList<ReviewListItem> listDatas) {
        this.listDatas = listDatas;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        view.setOnClickListener(mOnClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewListAdapter.ViewHolder holder, int position) {
        ReviewListItem item = listDatas.get(position);
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewContent.setText(item.getContent());
        Glide.with(context)
                .load(item.getUrl())
                .into(holder.imageviewImage);
    }

    @Override
    public int getItemCount() {
        return (listDatas != null) ? listDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageviewImage;
        private TextView textViewContent;
        private TextView textViewTitle;
        private LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageviewImage = (ImageView) itemView.findViewById(R.id.itemreview_imageview_image);
            textViewContent = (TextView) itemView.findViewById(R.id.itemreview_textview_content);
            textViewTitle = (TextView) itemView.findViewById(R.id.itemreview_textview_title);
            layout = (LinearLayout) itemView.findViewById(R.id.itemreview_layout_layout);
        }
    }
}