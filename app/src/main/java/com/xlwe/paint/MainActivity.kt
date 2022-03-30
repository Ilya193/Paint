package com.xlwe.paint

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.nvt.color.ColorPickerDialog
import com.xlwe.paint.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.undo.setOnClickListener {
            binding.paint.undo()
        }

        binding.reset.setOnClickListener {
            binding.paint.reset()
        }

        binding.color.setOnClickListener {
            ColorPickerDialog(this, binding.paint.color, true, object : ColorPickerDialog.OnColorPickerListener {
                override fun onCancel(dialog: ColorPickerDialog?) {

                }

                override fun onOk(dialog: ColorPickerDialog?, color: Int) {
                    binding.paint.color = color
                }
            }).show()
        }

        binding.seekbar.max = Constants.MAX
        binding.seekbar.progress = Constants.PROGRESS
        binding.tvWidth.text = Constants.PROGRESS.toString()

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress == 0) {
                    binding.tvWidth.text = Constants.MIN
                }
                else {
                    binding.tvWidth.text = progress.toString()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar!!.progress == 0) {
                    binding.paint.setStrokeWidth(1)
                }
                else {
                    binding.paint.setStrokeWidth(seekBar.progress)
                }
            }
        })
    }
}