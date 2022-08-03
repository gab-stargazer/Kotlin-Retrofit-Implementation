package com.example.rickandmortyapi.data.repository

import com.example.rickandmortyapi.data.api.CharactersApi
import com.example.rickandmortyapi.data.local.CharacterCache
import com.example.rickandmortyapi.data.model.ResponseAPI
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val charactersApi : CharactersApi
) : CharactersRepository {

    private val cache by lazy {
        CharacterCache
    }

    override suspend fun getCharacters(page : String) : ResponseAPI {
        return if (cache.Character[page] != null) {
            cache.Character[page]!!
        } else {
            val response = charactersApi.getCharacters(page)
            if (response.isSuccessful && response.body() != null) {
                cache.Character[page] = response.body()!!
                response.body()!!
            } else {
                val dummy = ResponseAPI(null, null)
                dummy
            }
        }
    }
}