package ru.nondoanything.promoshares.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nondoanything.promoshares.pojo.DataResponse;

public interface ApiService {
    @GET("promotions")
    Observable <DataResponse> getData(@Query("page") int currentPage);
}
