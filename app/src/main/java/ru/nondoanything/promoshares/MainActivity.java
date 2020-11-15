package ru.nondoanything.promoshares;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
        button1 = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
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
        Toast.makeText(getApplicationContext(),"Позиция: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, onPromoClickActivity.class);
        intent.putExtra("id", data.getId());
        intent.putExtra("title", data.getTitle());
        intent.putExtra("text", data.getText());
        intent.putExtra("cover", data.getCover());
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
        if(response.getMeta().getCurrentPage() == response.getMeta().getLastPage()) {
            //Disable next button
                button2.setEnabled(false);
        } else {
            //Enable next button
                button2.setEnabled(true);
        }
        if (response.getMeta().getCurrentPage() == 0) {
            //Disable prev button
                button1.setEnabled(false);
        } else {
            //Enable prev button
                button1.setEnabled(true);
        }
    }
}