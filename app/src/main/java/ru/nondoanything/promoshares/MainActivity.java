package ru.nondoanything.promoshares;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        recyclerViewData = findViewById(R.id.recyclerViewData);
        adapter = new DataAdapter();
        adapter.setPromodata(new ArrayList<Data>());
        recyclerViewData.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewData.setAdapter(adapter);
        adapter.setPromoListener(this::onPromoCLick);
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        apiService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataResponse>() {
                    @Override
                    public void accept(DataResponse dataResponse) throws Exception {
                        adapter.setPromodata(dataResponse.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "Ошибка получения данных", Toast.LENGTH_SHORT).show();
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
        /*if (data.getPhoto() != null && data.getPhoto().getUrlPhoto() != null) {
            intent.putExtra("photo", data.getPhoto().getUrlPhoto());
        }
        else {
            intent.putExtra("photo", );
        }*/
        startActivity(intent);
    }
}