package com.xlwe.paint.screens

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.xlwe.paint.R
import com.xlwe.paint.core.BaseFragment
import com.xlwe.paint.core.OnClickListener
import com.xlwe.paint.core.OnLongClickListener
import com.xlwe.paint.core.ReadFile
import com.xlwe.paint.databinding.FragmentPicturesBinding
import com.xlwe.paint.presentation.PictureAdapter
import com.xlwe.paint.viewmodels.MainViewModel
import java.io.File

class PicturesFragment : BaseFragment(), OnClickListener, OnLongClickListener {
    private var _binding: FragmentPicturesBinding? = null
    private val binding: FragmentPicturesBinding
        get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var readFile: ReadFile

    private val pictureAdapter = PictureAdapter(this, this)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ReadFile)
            readFile = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPicturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readFile.read()
        initRecyclerView()
        observeViewModel()
        setListeners()
    }

    private fun initRecyclerView() {
        binding.listPicture.adapter = pictureAdapter
    }

    private fun observeViewModel() {
        mainViewModel.listPicture.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.noSaveTV.visibility = View.VISIBLE
                binding.listPicture.visibility = View.GONE
            } else {
                binding.noSaveTV.visibility = View.GONE
                binding.listPicture.visibility = View.VISIBLE

                pictureAdapter.submitList(it)
            }
        }
    }

    override fun setListeners() {
        binding.draw.setOnClickListener {
            launchFragment(DrawingFragment.newInstanceAdd())
        }
    }

    private fun launchFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onClick(absolutePath: String, name: String) {
        launchFragment(DrawingFragment.newInstanceEdit(absolutePath, name))
    }

    override fun onLongClick(absolutePath: String) {
        AlertDialog.Builder(context)
            .setTitle("Удаление файла")
            .setMessage("Вы действительно хотите удалить файл?")
            .setPositiveButton("Да") { _, _ ->
                val file = File(absolutePath)
                val isDelete = file.delete()

                if (isDelete)
                    readFile.read()
            }
            .setNegativeButton("Нет") { _, _ -> }
            .create()
            .show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() =
            PicturesFragment()
    }
}