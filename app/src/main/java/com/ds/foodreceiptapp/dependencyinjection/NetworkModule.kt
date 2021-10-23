package com.ds.foodreceiptapp.dependencyinjection

import com.ds.foodreceiptapp.network.FoodRecipesApi
import com.ds.foodreceiptapp.network.RetrofitSupportSsl
import com.ds.foodreceiptapp.util.ParameterSetting
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    fun provideHttpClient():OkHttpClient{
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory():GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ):Retrofit{
        return Retrofit.Builder()
            .baseUrl(ParameterSetting.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build();
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit):FoodRecipesApi{
        return retrofit.create(FoodRecipesApi::class.java)
    }

}