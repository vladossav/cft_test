package com.example.cft_test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class RecentWordsAdapter(val clickListener: Listener) : RecyclerView.Adapter<RecentWordsAdapter.WordViewHolder>() {
    private var wordsList: MutableList<String> = mutableListOf()

    interface Listener {
        fun onClick(word: String)
    }

    inner class WordViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val word: TextView = item.findViewById(R.id.rv_text)
        private val cardItem: MaterialCardView = item.findViewById(R.id.card_item)
        init {
            cardItem.setOnClickListener {
                clickListener.onClick(word.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.word.text = wordsList[position]
    }

    override fun getItemCount(): Int {
        return wordsList.size
    }

    fun reload(newList: MutableList<String>) {
        val difResult = DiffUtil.calculateDiff(RecyclerDiffUtil(wordsList, newList))
        wordsList = newList
        difResult.dispatchUpdatesTo(this)
    }
}