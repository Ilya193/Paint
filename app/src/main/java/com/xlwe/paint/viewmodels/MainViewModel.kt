package com.xlwe.paint.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xlwe.paint.model.Picture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainViewModel : ViewModel() {
    private val _listPicture = MutableLiveData<List<Picture>>()
    val listPicture: LiveData<List<Picture>> get() = _listPicture

    private fun changeListPicture(files: List<Picture>) {
        _listPicture.postValue(files)
    }

    fun read(filesDir: File) = viewModelScope.launch(Dispatchers.IO) {
        val files = filesDir.listFiles()

        if (files != null) {
            val mainFiles = mutableListOf<Picture>()
            for (file in files)
                mainFiles.add(Picture(file.absolutePath, file.name))
            changeListPicture(mainFiles)
        } else
            changeListPicture(listOf())
    }

    fun save(bitmap: Bitmap, name: String, filesDir: File) = viewModelScope.launch(Dispatchers.IO) {
        val filename = "$name.png"
        val file = File(filesDir, filename)

        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}