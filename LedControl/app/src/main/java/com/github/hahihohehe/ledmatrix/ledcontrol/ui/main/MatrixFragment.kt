package com.github.hahihohehe.ledmatrix.ledcontrol.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.hahihohehe.ledmatrix.ledcontrol.ColorPaletteView
import com.github.hahihohehe.ledmatrix.ledcontrol.MainActivity
import com.github.hahihohehe.ledmatrix.ledcontrol.MatrixView
import com.github.hahihohehe.ledmatrix.ledcontrol.R
import com.pes.androidmaterialcolorpickerdialog.ColorPicker

class MatrixFragment : Fragment() {
    private lateinit var viewModel: MatrixViewModel
    private lateinit var matrixView: MatrixView
    private lateinit var colorPaletteView: ColorPaletteView
    private var matrixId: Long = -1
    private var deleted = false

    companion object {
        fun getInstance(matrixId: Long): MatrixFragment {
            val fragment = MatrixFragment()
            fragment.matrixId = matrixId
            return fragment
        }

        const val EMPTY_MATRIX = -1L
        private const val MATRIX_ID = "Matrix_Id"
        private const val DELETED = "deleted"
    }

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
        if (savedInstanceState != null) {
            matrixId = savedInstanceState.getLong(MATRIX_ID)
            deleted = savedInstanceState.getBoolean(DELETED)
        }
        viewModel = ViewModelProvider(this).get(MatrixViewModel::class.java)
        viewModel.colors.observe(viewLifecycleOwner,
            { colors -> matrixView.setColors(colors) })
        viewModel.palette.observe(viewLifecycleOwner,
            { colors -> colorPaletteView.setColors(colors) })
        viewModel.loadImage(matrixId)

        setHasOptionsMenu(true)
    }

    override fun onPause() {
        super.onPause()
        if (!deleted)
            viewModel.saveMatrixImage()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.miDelete).isVisible = viewModel.matrixImage != null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miDelete) {
            deleted = true
            viewModel.deleteMatrixImage()
            return true
        } else if (item.itemId == R.id.miAddImage) {
            viewModel.addNewImage()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).activeFragment = this
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(MATRIX_ID, matrixId)
        outState.putBoolean(DELETED, deleted)
    }

    val matrixJson get() = viewModel.createJson()
}