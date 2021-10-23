package com.example.course3kotlinwisataaplikasi.retrofitconfig

import com.ds.foodreceiptapp.network.RetrofitSupportSsl
import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
//import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{
        fun getInstance(url:String):Retrofit{
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create() )
                .client(RetrofitSupportSsl.getSupportSSL().build())
                .build();
            return retrofit;
        }
        fun getInstanceReactvice(url:String):Retrofit{
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create() )
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
            return retrofit;
        }
        fun getInstanceOkhttpReactive(url:String):Retrofit{
//            val interceptor = HttpLoggingInterceptor()
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//            val okHttp = OkHttpClient().newBuilder().addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttp)
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
            return retrofit;
        }
    }


}