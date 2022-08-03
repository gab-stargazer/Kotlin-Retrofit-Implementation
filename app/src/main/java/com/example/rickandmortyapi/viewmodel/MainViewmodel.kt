package com.example.rickandmortyapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapi.data.model.Info
import com.example.rickandmortyapi.data.model.Result
import com.example.rickandmortyapi.data.repository.CharactersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val charactersRepository : CharactersRepository
) : ViewModel() {

    val errorResponse : MutableLiveData<String> = MutableLiveData()
    private val info = MutableLiveData<Info>()

    val currentPage : MutableLiveData<Int> = MutableLiveData()
    private val _characters = MutableLiveData<List<Result>>()
    val characters : LiveData<List<Result>> = _characters

    init {
        currentPage.value = 1
        viewModelScope.launch(Dispatchers.IO) {
            val responseAPI = charactersRepository.getCharacters(currentPage.value!!.toString())
            if (responseAPI.info != null && responseAPI.results != null) {
                info.postValue(responseAPI.info)
                _characters.postValue(responseAPI.results)
            } else {
                errorResponse.postValue("Terjadi kegagalan")
            }
        }
    }

    fun nextPage() = viewModelScope.launch {
        if (info.value!!.next.isEmpty()) {
            errorResponse.value = "Anda berada pada halaman akhir"
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val page = currentPage.value!! + 1
                val responseAPI = charactersRepository.getCharacters(page.toString())
                if (responseAPI.info != null && responseAPI.results != null) {
                    currentPage.postValue(page)
                    _characters.postValue(responseAPI.results)
                    info.postValue(responseAPI.info)
                } else {
                    errorResponse.postValue("Terjadi kegagalan")
                }
            }
        }
    }

    fun previousPage() = viewModelScope.launch {
        if (currentPage.value!! == 1) {
            errorResponse.value = "Anda berada pada halaman awal"
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val page = currentPage.value!! - 1
                val responseAPI = charactersRepository.getCharacters(page.toString())
                if (responseAPI.info != null && responseAPI.results != null) {
                    _characters.postValue(responseAPI.results)
                    info.postValue(responseAPI.info)
                    currentPage.postValue(page)
                } else {
                    errorResponse.postValue("Terjadi kegagalan")
                }
            }
        }
    }

    fun clearResponse() {
        errorResponse.value = ""
    }
}