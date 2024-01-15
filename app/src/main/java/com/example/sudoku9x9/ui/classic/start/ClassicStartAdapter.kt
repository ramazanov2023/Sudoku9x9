package com.example.sudoku9x9.ui.classic.start

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku9x9.data.local.ClassicCard
import com.example.sudoku9x9.databinding.CardClassicBinding

class ClassicStartAdapter:ListAdapter<ClassicCard,ClassicStartAdapter.Holder>(Diff) {
    class Holder(val binding:CardClassicBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(card: ClassicCard){
            binding.card = card
            binding.executePendingBindings()
        }
    }
    object Diff:DiffUtil.ItemCallback<ClassicCard>(){
        override fun areItemsTheSame(oldItem: ClassicCard, newItem: ClassicCard): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ClassicCard, newItem: ClassicCard): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(CardClassicBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val card = getItem(position)
        holder.bind(card)
    }


}