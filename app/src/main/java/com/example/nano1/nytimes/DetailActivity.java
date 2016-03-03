package com.example.nano1.nytimes;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ImageView imageView = (ImageView) findViewById(R.id.detailImageView);
        Article selectedArticle = getIntent().getParcelableExtra("article");
        //String url = selectedArticle.

//        Picasso.with(getBaseContext())
//                .load(selectedArticle.getMultimedia().get(4).getUrl())
//                .into(imageView);
    }
}
