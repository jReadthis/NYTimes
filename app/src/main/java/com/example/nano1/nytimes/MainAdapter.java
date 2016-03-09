package com.example.nano1.nytimes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nano1 on 2/10/2016.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<Article.Result> results;
    private LayoutInflater inflater;
    private Context context;

    public MainAdapter(Context context,List<Article.Result> results){
        this.results = results;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void delete(int position){
        results.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.main_single_item, parent,false);
        MainViewHolder holder = new MainViewHolder(v);
        return holder ;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        final Article.Result article = results.get(position);

        holder.titleTextView.setText(article.getTitle());
        holder.abstractTextView.setText(article.getAbstractX());
        holder.options.setImageResource(R.drawable.ic_menu_send);

        if (article.getMultimedia().size() != 0) {
            Picasso.with(context)
                    .load(article.getMultimedia().get(1).getUrl())
                    .resize(200, 200)
                    .centerCrop()
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("article", results.get(position) );
                    context.startActivity(intent);
            }
        });
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticlesFragment.customTab(article.getUrl(),context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != results ? results.size() : 0);
    }

    public void swapList(List<Article.Result> results){
        if (this.results != null){
            this.results.clear();
            this.results.addAll(results);
        }else {
            this.results = results;
        }
        notifyDataSetChanged();
    }

    // Implement ArticleViewHolderClicks
    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView abstractTextView;
        public ImageView imageView;
        public ImageView options;

        public MainViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.single_row_title);
            abstractTextView = (TextView) itemView.findViewById(R.id.single_row_abstract);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            options = (ImageView) itemView.findViewById(R.id.imageView2);
        }

    }
}
