package com.example.rickandmortyapi.data.api

import com.example.rickandmortyapi.data.model.ResponseAPI
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Dagger Hilt Will Inject this API Interface to other class that depends on it
interface CharactersApi {

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }

    @GET("character/")
    suspend fun getCharacters(@Query("page") page : String) : Response<ResponseAPI>
}