package com.example.nano1.nytimes;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ImageView imageView = (ImageView) findViewById(R.id.detailImageView);
        TextView textView = (TextView) findViewById(R.id.detailTextView);
        Article.Result selectedArticle = getIntent().getParcelableExtra("article");

        textView.setText(selectedArticle.getTitle());
        if (selectedArticle.getMultimedia().size() != 0) {

            String url = selectedArticle.getMultimedia().get(4).getUrl();

            Picasso.with(getBaseContext())
                    .load(url)
                    .into(imageView);
        }
    }


}
