package ru.nondoanything.promoshares;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import ru.nondoanything.promoshares.pojo.Data;

public class PromoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_promo_click);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        TextView textViewPromoID = findViewById(R.id.textViewPromoID);
        TextView textViewTitlePromo = findViewById(R.id.textViewTitlePromo);
        TextView textViewTextPromo = findViewById(R.id.textViewTextPromo);
        ImageView dataImage = findViewById(R.id.DataImage);
        if (getIntent().hasExtra("data")) {
            Data data = Parcels.unwrap(getIntent().getParcelableExtra("data"));
            String id = Integer.toString(data.getId());
            String title = data.getTitle();
            String text = data.getText();
            String cover = data.getCover();
            textViewPromoID.setText(id);
            textViewTitlePromo.setText(title);
            textViewTextPromo.setText(text);
            Glide
                    .with(this)
                    .load(cover)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(dataImage);
        }
        else {
            Intent backToList = new Intent(this, MainActivity.class);
            startActivity(backToList);
        }
    }
}