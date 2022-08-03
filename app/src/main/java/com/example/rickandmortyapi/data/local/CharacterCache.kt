package com.example.rickandmortyapi.data.local

import com.example.rickandmortyapi.data.model.ResponseAPI

object CharacterCache {
    val Character : MutableMap<String, ResponseAPI> = mutableMapOf()
}