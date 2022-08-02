package com.example.rickandmortyapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapi.data.model.Info
import com.example.rickandmortyapi.data.model.Result
import com.example.rickandmortyapi.repository.CharactersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val charactersRepository : CharactersRepository
) : ViewModel() {

    val errorResponse : MutableLiveData<String> = MutableLiveData()

    private val info = MutableLiveData<Info>()

    private var _currentPage = 1
    val currentPage : MutableLiveData<Int> = MutableLiveData()
    private val _characters = MutableLiveData<List<Result>>()
    val characters : LiveData<List<Result>> = _characters

    init {
        currentPage.value = _currentPage
        viewModelScope.launch(Dispatchers.IO) {
            val responseAPI = charactersRepository.getCharacters(_currentPage.toString())
            if (responseAPI.isSuccessful && responseAPI.body()!!.results.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    _characters.value = responseAPI.body()!!.results
                    info.value = responseAPI.body()!!.info
                }
            } else {
                errorResponse.value = "Terjadi kegagalan"
            }
        }
    }

    fun nextPage() = viewModelScope.launch {
        if (info.value!!.next.isEmpty()) {
            errorResponse.value = "Anda berada pada halaman akhir"
        } else {
            withContext(Dispatchers.IO) {
                val responseAPI = charactersRepository.getCharacters((_currentPage + 1).toString())
                if (responseAPI.isSuccessful && responseAPI.body()!!.results.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        _characters.value = responseAPI.body()!!.results
                        info.value = responseAPI.body()!!.info
                        _currentPage += 1
                        currentPage.value = _currentPage
                    }
                } else {
                    errorResponse.value = "Terjadi kegagalan"
                }
            }
        }
    }

    fun previousPage() = viewModelScope.launch {
        if (_currentPage == 1) {
            errorResponse.value = "Anda berada pada halaman awal"
        } else {
            withContext(Dispatchers.IO) {
                val responseAPI = charactersRepository.getCharacters((_currentPage - 1).toString())
                if (responseAPI.isSuccessful && responseAPI.body()!!.results.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        _characters.value = responseAPI.body()!!.results
                        info.value = responseAPI.body()!!.info
                        _currentPage = _currentPage - 1
                        currentPage.value = _currentPage
                    }
                } else {
                    errorResponse.value = "Terjadi kegagalan"
                }
            }
        }
    }

    fun clearResponse() {
        errorResponse.value = ""
    }
}