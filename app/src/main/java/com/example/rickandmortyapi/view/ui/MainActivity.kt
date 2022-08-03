package com.example.rickandmortyapi.view.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapi.databinding.ActivityMainBinding
import com.example.rickandmortyapi.view.adapter.CharactersAdapter
import com.example.rickandmortyapi.viewmodel.MainViewmodel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewmodel by viewModels()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#2F2F2F")))
        setupRecyclerview(viewModel)

        viewModel.currentPage.observe(this) {
            binding.tvPageNumber.text = "Current Page : $it"
        }

        binding.btnNext.setOnClickListener { next(viewModel) }
        binding.btnPrev.setOnClickListener { pref(viewModel) }

        lifecycleScope.launchWhenCreated {
            viewModel.errorResponse.observe(this@MainActivity) {
                if (it.isNotEmpty()) {
                    Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_SHORT).show()
//                    Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show()
                    viewModel.clearResponse()
                }
            }
        }
    }

    private fun pref(viewModel : MainViewmodel) {
        viewModel.previousPage()
    }

    private fun next(viewModel : MainViewmodel) {
        viewModel.nextPage()
    }

    private fun setupRecyclerview(model : MainViewmodel) {
        val adapter = CharactersAdapter()
        binding.rvListCharacters.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        model.characters.observe(this) {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            }
        }
    }
}