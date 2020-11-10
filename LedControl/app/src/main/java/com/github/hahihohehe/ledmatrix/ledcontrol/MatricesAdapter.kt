package com.github.hahihohehe.ledmatrix.ledcontrol

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.hahihohehe.ledmatrix.ledcontrol.ui.main.MainFragment

class MatricesAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 10
    }

    override fun createFragment(position: Int): Fragment {
        return MainFragment()
    }
}