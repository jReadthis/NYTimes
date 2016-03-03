package com.example.nano1.nytimes;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
    Context context;

    public MainAdapter(Context context,List<Article.Result> results){
        this.results = results;
        this.context = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder( final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_single_item, parent,false);
        return new MainViewHolder(v, new MainViewHolder.ArticleViewHolderClicks() {

            @Override
            public void onItemClick(View caller) {

                Article.Result article = (Article.Result) caller.getTag();

                int viewId = caller.getId();
                if (viewId == R.id.imageView){
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("article", article );
                    context.startActivity(intent);
                }
                else {
                    String url = article.getUrl();
                    ArticlesFragment.customTab(url, context);

                }
            }
        });
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {
        final Article.Result article = results.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.abstractTextView.setText(article.getAbstractX());

        if (article.getMultimedia().size() != 0) {
            Picasso.with(context)
                    .load(article.getMultimedia().get(1).getUrl())
                    .resize(200,200)
                    .centerCrop()
                    .into(holder.imageView);
        }else {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }
        holder.itemView.setTag(article);
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
    public static class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleTextView;
        public TextView abstractTextView;
        public ImageView imageView;
        ArticleViewHolderClicks mListener;

        public MainViewHolder(View itemView, ArticleViewHolderClicks listener) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.single_row_title);
            abstractTextView = (TextView) itemView.findViewById(R.id.single_row_abstract);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setOnClickListener(this);
            mListener = listener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v);
        }

        // Interface to define the onclick events of the recycler view
        public interface ArticleViewHolderClicks{
            void onItemClick(View caller);
        }
    }


}
