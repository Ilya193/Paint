package com.xlwe.paint.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.xlwe.paint.R
import com.xlwe.paint.core.OpeningAppSettings
import com.xlwe.paint.core.PermissionRequest
import com.xlwe.paint.core.ReadFile
import com.xlwe.paint.core.SaveFile
import com.xlwe.paint.databinding.ActivityMainBinding
import com.xlwe.paint.viewmodels.MainViewModel

class MainActivity : AppCompatActivity(), PermissionRequest, ReadFile, SaveFile,
    OpeningAppSettings {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            request()
        } else {
            launchFragment(PicturesFragment.newInstance())
        }
    }

    override fun request() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), 10
        )
    }

    override fun read() {
        mainViewModel.read(filesDir)
    }

    override fun save(bitmap: Bitmap, name: String) {
        mainViewModel.save(bitmap, name, filesDir)
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun openSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
                "package", packageName, null
            )
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Snackbar.make(binding.root, "Разрешения навсегда запрещены", Snackbar.LENGTH_SHORT)
                .show()
        } else {
            startActivity(appSettingsIntent)
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
                launchFragment(PermissionsFragment.newInstanceCancelStart())
            } else {
                launchFragment(PermissionsFragment.newInstanceFullCancelStart())
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                launchFragment(PicturesFragment.newInstance())
            } else {
                checkPermission()
            }
        }
    }
}