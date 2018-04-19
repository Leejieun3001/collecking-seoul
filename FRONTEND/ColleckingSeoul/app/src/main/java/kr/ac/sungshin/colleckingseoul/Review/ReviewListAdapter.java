package kr.ac.sungshin.colleckingseoul.Review;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import kr.ac.sungshin.colleckingseoul.R;

/**
 * Created by kwonhyeon-a on 2018. 4. 19..
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ReviewItem> list;

    public ReviewListAdapter(Context context, ArrayList<ReviewItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewListAdapter.ViewHolder holder, int position) {
        ReviewItem item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
        Glide.with(context)
                .load(item.getUrl())
                .into(holder.image);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.putExtra("idx", 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemreview_textview_title)
        TextView title;
        @BindView(R.id.itemreview_textview_content)
        TextView content;
        @BindView(R.id.itemreview_imageview_image)
        ImageView image;
        @BindView(R.id.itemreview_layout_layout)
        LinearLayout layout;

        public ViewHolder(View v) {
            super(v);
        }
    }
}
