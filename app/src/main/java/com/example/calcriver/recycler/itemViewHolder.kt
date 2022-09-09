package com.example.calcriver.recycler

import android.graphics.Color
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.calcriver.MainActivity
import com.example.calcriver.MainViewModel
import com.example.calcriver.R
import com.example.calcriver.databinding.ActivityMainBinding
import com.example.calcriver.databinding.ItemBtnBinding

class itemViewHolder(val view: View, val context : MainActivity) : RecyclerView.ViewHolder(view) {

    val binding = ItemBtnBinding.bind(view)


    fun render(word: String, color: String) {
        binding.btn.text = word
        binding.btn.setTextColor(Color.parseColor(color))
        val viewModel2 = ViewModelProviders.of(context).get(MainViewModel::class.java)
        binding.viewModel = viewModel2
    }
}