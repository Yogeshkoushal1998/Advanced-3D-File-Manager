package com.yk.mobile.advanced3dfilemanager.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.yk.mobile.advanced3dfilemanager.R
import dagger.hilt.android.AndroidEntryPoint
import utility.OnBackPressedListener

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPermission()
    }

    private fun takePermission() {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.setData(
                Uri.parse(
                    java.lang.String.format(
                        "package:%s",
                        getApplicationContext().getPackageName()
                    )
                )
            )
            activityResultLauncher.launch(intent)
        } catch (e: Exception) {
            val intent = Intent()
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            activityResultLauncher.launch(intent)
        }
    }

    private fun takeAppInstallPermission() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Check if permission to install unknown apps is granted
                if (!this@MainActivity.packageManager.canRequestPackageInstalls()) {
                    // Permission is not granted, request permission using ActivityResultLauncher
                    val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                        data = Uri.parse("package:${this@MainActivity.packageName}")
                    }
                    installPermissionLauncher.launch(intent)
                    return
                }
            }
        } catch (e: Exception) {
            val intent = Intent()
            intent.setAction(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            installPermissionLauncher.launch(intent)
        }
    }


    var activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                        } else {
                            takePermission()
                        }
                    }
                }
            }
        )

    var installPermissionLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (packageManager.canRequestPackageInstalls()) {
                            // Permission granted, proceed with APK installation
//                    installApk(this, apkFilePath)
                        } else {
                            takeAppInstallPermission()
                            // Permission denied
                            Toast.makeText(
                                this@MainActivity,
                                "Permission denied to install apps",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        )

    private fun startPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                takePermission()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            }
        }


        //App install Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!packageManager.canRequestPackageInstalls()) {
                // Permission granted, proceed with APK installation
                takeAppInstallPermission()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101
    }


    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Get the current fragment
        val fragment = navHostFragment.childFragmentManager.fragments.lastOrNull()
        // Now you can use the currentFragment variable
        // Check if the fragment implements a custom back press interface
        fragment.let {
            if (fragment is OnBackPressedListener) {
                if (fragment.onBackPressed()) {
                    // Fragment handled the back press
                    return
                }
            }
        }
        super.onBackPressed()
    }


}