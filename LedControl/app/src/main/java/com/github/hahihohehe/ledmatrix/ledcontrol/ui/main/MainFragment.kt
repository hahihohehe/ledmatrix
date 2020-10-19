package com.github.hahihohehe.ledmatrix.ledcontrol.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.hahihohehe.ledmatrix.ledcontrol.MatrixView
import com.github.hahihohehe.ledmatrix.ledcontrol.R
import com.pes.androidmaterialcolorpickerdialog.ColorPicker

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var matrixView: MatrixView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        matrixView = view.findViewById(R.id.matrixView)
        matrixView.onPixelClickedListerner = { x, y ->
            val color = viewModel.getColor(x, y)
            val colorPicker = ColorPicker(
                requireActivity(),
                Color.red(color),
                Color.green(color),
                Color.blue(color)
            )
            colorPicker.show()
            colorPicker.enableAutoClose()
            colorPicker.setCallback { newColor -> viewModel.updateColor(x, y, newColor) }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.colors.observe(viewLifecycleOwner,
            { colors -> matrixView.setColors(colors) })
    }

}