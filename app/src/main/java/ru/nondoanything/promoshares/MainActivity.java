package ru.nondoanything.promoshares;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.parceler.Parcels;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.nondoanything.promoshares.adapters.DataAdapter;
import ru.nondoanything.promoshares.api.ApiFactory;
import ru.nondoanything.promoshares.api.ApiService;
import ru.nondoanything.promoshares.pojo.Data;
import ru.nondoanything.promoshares.pojo.DataResponse;

public class MainActivity extends AppCompatActivity implements DataAdapter.OnPromoListener {

    private RecyclerView recyclerViewData;
    private DataAdapter adapter;
    private DataResponse response;
    private Button button1, button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        recyclerViewData = findViewById(R.id.recyclerViewData);
        button1 = findViewById(R.id.button_prev);
        button2 = findViewById(R.id.button_next);
        adapter = new DataAdapter();
        adapter.setPromodata(new ArrayList<Data>());
        recyclerViewData.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewData.setAdapter(adapter);
        adapter.setPromoListener(this);
        updateList(1);
        button1.setOnClickListener(v -> {
            if(response.getMeta().getCurrentPage() != 0) {
                updateList(response.getMeta().getCurrentPage() - 1);
            }
        });
        button2.setOnClickListener(v -> {
            if(response.getMeta().getCurrentPage() != response.getMeta().getLastPage()) {
                updateList(response.getMeta().getCurrentPage() + 1);
            }
        });

    }

    @Override
    public void onPromoCLick(int position) {
        Data data = adapter.getPromodata().get(position);
        Parcelable parcelData = Parcels.wrap(data);
        Toast.makeText(getApplicationContext(),"Позиция: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PromoActivity.class);
        intent.putExtra("data", parcelData);
        startActivity(intent);
    }

    private void updateList(int currentPage) {
        button1.setEnabled(false);
        button2.setEnabled(false);
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        apiService.getData(currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataResponse -> {
                    response = dataResponse;
                    adapter.setPromodata(dataResponse.getData());
                    updatesButtonStatus();
                }, throwable -> {
                    Toast.makeText(MainActivity.this, "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                    updatesButtonStatus();
                });
    }

    private void updatesButtonStatus() {
        //Disable button_next
        //Enable button_next
        button2.setEnabled(response.getMeta().getCurrentPage() != response.getMeta().getLastPage());
        //Disable button_prev
        //Enable button_prev
        button1.setEnabled(response.getMeta().getCurrentPage() != 0);
    }

    @Override
    public void onBackPressed() {
        if(response.getMeta().getCurrentPage() != 0) {
            updateList(response.getMeta().getCurrentPage() - 1);
    } else {
            moveTaskToBack(true);
            finish();
        }
    }
}