package com.example.rickandmortyapi.repository

import com.example.rickandmortyapi.data.api.CharactersApi
import com.example.rickandmortyapi.data.model.ResponseAPI
import retrofit2.Response
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val charactersApi : CharactersApi
) : CharactersRepository {

    override suspend fun getCharacters(page : String) : Response<ResponseAPI> =
        charactersApi.getCharacters(page)
}