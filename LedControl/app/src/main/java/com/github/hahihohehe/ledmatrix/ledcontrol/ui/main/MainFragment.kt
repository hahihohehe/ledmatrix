package com.github.hahihohehe.ledmatrix.ledcontrol.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.hahihohehe.ledmatrix.ledcontrol.ColorPaletteView
import com.github.hahihohehe.ledmatrix.ledcontrol.MatrixView
import com.github.hahihohehe.ledmatrix.ledcontrol.R
import com.pes.androidmaterialcolorpickerdialog.ColorPicker

class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var matrixView: MatrixView
    private lateinit var colorPaletteView: ColorPaletteView
    private lateinit var btnUpload: Button
    private lateinit var etIpAddress: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matrixView = view.findViewById(R.id.matrixView)
        matrixView.onPixelClickedListerner = { x, y ->
            viewModel.updateColor(x, y, colorPaletteView.selected)
        }
//        etIpAddress = view.findViewById(R.id.etIpAddress)
//        btnUpload = view.findViewById(R.id.btnUpload)
//        btnUpload.setOnClickListener {
//            viewModel.upload(etIpAddress.text.toString())
//        }
        colorPaletteView = view.findViewById(R.id.colorPaletteView)
        colorPaletteView.onItemClickedListener = { x ->
            if (colorPaletteView.isSelected(x)) {
                val color = viewModel.getPaletteColor(x)
                val colorPicker = ColorPicker(
                    requireActivity(),
                    Color.red(color),
                    Color.green(color),
                    Color.blue(color)
                )
                colorPicker.show()
                colorPicker.enableAutoClose()
                colorPicker.setCallback { newColor -> viewModel.updatePaletteColor(x, newColor) }
            } else {
                colorPaletteView.selected = x
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.colors.observe(viewLifecycleOwner,
            { colors -> matrixView.setColors(colors) })
        viewModel.palette.observe(viewLifecycleOwner,
            { colors -> colorPaletteView.setColors(colors) })
    }

}