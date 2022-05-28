package com.xlwe.paint.screens

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import com.nvt.color.ColorPickerDialog
import com.xlwe.paint.core.*
import com.xlwe.paint.databinding.FragmentDrawingBinding
import java.io.File

class DrawingFragment : BaseFragment() {
    private var _binding: FragmentDrawingBinding? = null
    private val binding: FragmentDrawingBinding
        get() = _binding!!

    private lateinit var saveFile: SaveFile
    private var screenSettings = ""
    private var absolutePath = ""
    private var namePicture = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SaveFile)
            saveFile = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startSettingsScreen()
    }

    override fun setListeners() {
        binding.undo.setOnClickListener { binding.paint.undo() }

        binding.reset.setOnClickListener { binding.paint.reset() }

        binding.save.setOnClickListener {
            val bitmap = Bitmap.createBitmap(
                binding.paint.width,
                binding.paint.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            binding.paint.draw(canvas)

            //or val bitmap = binding.paint.drawToBitmap()

            if (isSettingsScreen(SETTINGS_EDIT)) {
                saveFile.save(bitmap, namePicture.replace(".png", ""))
                parentFragmentManager.popBackStack()
            } else {
                launchDialog(bitmap)
            }
        }

        binding.color.setOnClickListener {
            ColorPickerDialog(
                context,
                binding.paint.color,
                true,
                object : OnColorPickerListener() {
                    override fun onOk(dialog: ColorPickerDialog?, color: Int) {
                        binding.paint.color = color
                    }
                }).show()
        }

        binding.seekbar.max = Constants.MAX
        binding.seekbar.progress = Constants.PROGRESS
        binding.tvWidth.text = Constants.PROGRESS.toString()

        binding.seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress == 0) binding.tvWidth.text = Constants.MIN
                else binding.tvWidth.text = progress.toString()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar!!.progress == 0) binding.paint.setStrokeWidth(1)
                else binding.paint.setStrokeWidth(seekBar.progress)
            }
        })
    }

    private fun launchDialog(bitmap: Bitmap) {
        val name = EditText(context)
        AlertDialog.Builder(context)
            .setTitle("Введите имя")
            .setView(name)
            .setPositiveButton("Сохранить") { _, _ ->
                saveFile.save(bitmap, name.text.toString())
                parentFragmentManager.popBackStack()
            }
            .setNegativeButton("Отмена") { _, _ -> }
            .create()
            .show()
    }

    override fun isSettingsScreen(screen: String) = screenSettings == screen

    override fun startSettingsScreen() {
        if (isSettingsScreen(SETTINGS_ADD)) startScreenAdd()
        else startScreenEdit()
    }

    private fun startScreenAdd() {
        setListeners()
    }

    private fun startScreenEdit() {
        val directory = File(absolutePath)
        binding.paint.background = Drawable.createFromPath(directory.toString())
        setListeners()
    }

    override fun parseParams() {
        val args = requireArguments()
        screenSettings = args.getString(SCREEN_SETTINGS) ?: ""

        if (isSettingsScreen(SETTINGS_EDIT)) {
            absolutePath = args.getString(ABSOLUTE_PATH, "")
            namePicture = args.getString(NAME_PICTURE, "")
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val SCREEN_SETTINGS = "screen_settings"
        private const val SETTINGS_EDIT = "screen_edit"
        private const val SETTINGS_ADD = "screen_add"

        private const val ABSOLUTE_PATH = "absolute_path"
        private const val NAME_PICTURE = "name_picture"

        fun newInstanceAdd() =
            DrawingFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_SETTINGS, SETTINGS_ADD)
                }
            }

        fun newInstanceEdit(absolutePath: String, name: String): DrawingFragment {
            return DrawingFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_SETTINGS, SETTINGS_EDIT)
                    putString(ABSOLUTE_PATH, absolutePath)
                    putString(NAME_PICTURE, name)
                }
            }
        }
    }
}