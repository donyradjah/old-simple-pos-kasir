package com.possederhana.kasir.helper;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("produk/ganti-status")
    Call<ResponseBody> gantiStatus(@Field("id") int id,
                                   @Field("status") String status);

}
