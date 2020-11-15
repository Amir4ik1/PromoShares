package ru.nondoanything.promoshares;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.nondoanything.promoshares.adapters.DataAdapter;
import ru.nondoanything.promoshares.pojo.Data;

public class onPromoClickActivity extends AppCompatActivity {

    private TextView textViewPromoID;
    private TextView textViewTitlePromo;
    private TextView textViewTextPromo;
    private ImageView DataImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_promo_click);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        textViewPromoID = findViewById(R.id.textViewPromoID);
        textViewTitlePromo = findViewById(R.id.textViewTitlePromo);
        textViewTextPromo = findViewById(R.id.textViewTextPromo);
        DataImage = findViewById(R.id.DataImage);
        Intent intent = getIntent();
        if (intent.hasExtra("id") && intent.hasExtra("title") && intent.hasExtra("text") && intent.hasExtra("cover")) {
            String id = Integer.toString(intent.getIntExtra("id", -1));
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            String cover = intent.getStringExtra("cover");
            textViewPromoID.setText(id);
            textViewTitlePromo.setText(title);
            textViewTextPromo.setText(text);
            Glide
                    .with(this)
                    .load(cover)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(DataImage);
        }
        else {
            Intent backToList = new Intent(this, MainActivity.class);
            startActivity(backToList);
        }
    }
}