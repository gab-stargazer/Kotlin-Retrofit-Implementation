package com.example.rickandmortyapi.di

import com.example.rickandmortyapi.data.api.CharactersApi
import com.example.rickandmortyapi.data.repository.CharactersRepository
import com.example.rickandmortyapi.data.repository.CharactersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    //   Providing MoshiConverter For Converting JSON into Kotlin Data Class
    @Provides
    @Singleton
    fun moshiBuilder() : MoshiConverterFactory = MoshiConverterFactory.create()

    //   Providing Retrofit Builder
    @Provides
    @Singleton
    fun provideRetrofit(moshi : MoshiConverterFactory) : Retrofit = Retrofit.Builder()
        .baseUrl(CharactersApi.BASE_URL)
        .addConverterFactory(moshi)
        .build()

    //   Creating API Interface
    @Provides
    @Singleton
    fun provideCharactersApi(retrofit : Retrofit) : CharactersApi =
        retrofit.create(CharactersApi::class.java)

    //  Providing Repository Class with API
    @Provides
    @Singleton
    fun provideCharactersRepository(
        charactersApi : CharactersApi
    ) : CharactersRepository =
        CharactersRepositoryImpl(charactersApi)
}