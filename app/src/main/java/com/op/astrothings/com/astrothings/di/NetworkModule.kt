package com.op.astrothings.com.astrothings.di


import com.google.gson.GsonBuilder
import com.op.astrothings.com.astrothings.network.ApiHelper
import com.op.astrothings.com.astrothings.network.ApiHelperImpl
import com.op.astrothings.com.astrothings.network.ApiService
import com.op.astrothings.com.astrothings.network.NetworkResponse
import com.op.astrothings.com.astrothings.util.AnalyticsHelper
import com.op.astrothings.com.astrothings.util.Constants.Companion.BASE_URL
import com.op.astrothings.com.astrothings.util.MoshiArrayListJsonAdapter
import com.op.astrothings.com.astrothings.util.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named(BASE_URL)
    fun provideBaseUrl(): String {
        return BASE_URL
    }

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
//    val gson = GsonBuilder()
//        .registerTypeAdapter(NetworkResponse::class.java, NetworkResponseAdapter())
//        .create()

//    @Singleton
//    @Provides
//    fun provideRetrofitInstance(
//        okHttpClient: OkHttpClient,
//        gsonConverterFactory: GsonConverterFactory
//    ): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .client(okHttpClient)
//            .addConverterFactory(gsonConverterFactory)
//            .build()
//    }
@Provides
@Singleton
fun provideRetrofit(
    okHttpClient: OkHttpClient,
    @Named(BASE_URL) baseUrl: String
): Retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(
        MoshiConverterFactory.create(
            Moshi.Builder()
                .add(MoshiArrayListJsonAdapter.FACTORY).addLast(KotlinJsonAdapterFactory()).build()
        )
    )
    .addCallAdapterFactory(NetworkResponseAdapterFactory())
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiHelperModule {
    @Binds
    @Singleton
    abstract fun bindApiHelper(apiHelperImpl: ApiHelperImpl): ApiHelper

}
