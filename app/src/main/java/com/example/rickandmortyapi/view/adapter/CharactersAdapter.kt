package com.example.rickandmortyapi.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.rickandmortyapi.R
import com.example.rickandmortyapi.data.model.Result
import com.example.rickandmortyapi.databinding.ListCharacterItemBinding

class CharactersAdapter :
    ListAdapter<Result, CharactersAdapter.ViewHolder>(CharactersComparator()) {

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val binding =
            ListCharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class ViewHolder(private val binding : ListCharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(characters : Result) {
            binding.apply {
                Glide.with(itemView)
                    .load(characters.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(ivCharacter)

                tvCharacterName.text = characters.name
                tvCharacterStatusAndSpecies.text = "${characters.status} - ${characters.species}"
                tvLastKnownLocation.text = characters.location.name
            }
        }
    }

    class CharactersComparator : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem : Result, newItem : Result) : Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem : Result,
            newItem : Result
        ) : Boolean = oldItem.name == newItem.name
    }
}