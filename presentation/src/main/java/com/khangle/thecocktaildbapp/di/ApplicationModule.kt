package com.khangle.thecocktaildbapp.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.khangle.data.repository.GoQuoteRepositoryImp
import com.khangle.data.repository.TheCocktailDBRepositoryImp
import com.khangle.data.webservice.GoQuoteApi
import com.khangle.data.webservice.GoQuoteBaseApi
import com.khangle.data.webservice.TheCockTailDBApi
import com.khangle.data.webservice.TheCockTailDBBaseApi
import com.khangle.data.webservice.interceptors.NetworkConnectionInterceptor
import com.khangle.domain.repository.GoQuoteRepository
import com.khangle.domain.repository.TheCockTailDBRepository

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationProvideModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().registerTypeAdapter(
            com.khangle.domain.model.Drink::class.java,
            com.khangle.domain.model.DrinkTypeAdapter()
        ).create()
    }


    @Provides
    @Singleton
    fun provideClient(@ApplicationContext context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val networkInterceptor = NetworkConnectionInterceptor(context)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30,TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    @Singleton
    fun provideWebservice(gson: Gson, okHttpClient: OkHttpClient): TheCockTailDBBaseApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TheCockTailDBApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQuoteWebservice(okHttpClient: OkHttpClient): GoQuoteBaseApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://goquotes-api.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoQuoteApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationBindModule {
    @Binds
    abstract fun bindTheCocktailDBRepository(theCocktailDBRepositoryImp: TheCocktailDBRepositoryImp): TheCockTailDBRepository

    @Binds
    abstract fun bindGoQuoteRepository(quoteRepository: GoQuoteRepositoryImp): GoQuoteRepository
}