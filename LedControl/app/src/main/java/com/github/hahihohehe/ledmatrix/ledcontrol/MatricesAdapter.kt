package com.github.hahihohehe.ledmatrix.ledcontrol

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.github.hahihohehe.ledmatrix.ledcontrol.ui.main.MatrixFragment

class MatricesAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val imageRepository = imageRepository(activity.application)
    private var cachedImages: List<MatrixImage> = listOf()

    init {
        imageRepository.images.observe(activity) {
            cachedImages = it
            println(cachedImages)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return cachedImages.size
    }

    override fun createFragment(position: Int): Fragment {
        return MatrixFragment.getInstance(cachedImages[position].id)
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemId(position: Int): Long {
        return cachedImages[position].id
    }
}