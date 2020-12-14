package com.github.hahihohehe.ledmatrix.ledcontrol

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.hahihohehe.ledmatrix.ledcontrol.ui.main.MatrixFragment

class MatricesAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val imageRepository = imageRepository(activity.application)

    override fun getItemCount(): Int {
        return imageRepository.imageList.size + 1
    }

    override fun createFragment(position: Int): Fragment {
        return if (position < imageRepository.imageList.size)
            MatrixFragment.getInstance(imageRepository.imageList[position].id)
        else
            MatrixFragment.getInstance(MatrixFragment.EMPTY_MATRIX)
    }
}