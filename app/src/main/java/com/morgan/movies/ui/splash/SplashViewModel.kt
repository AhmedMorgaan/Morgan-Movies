package com.morgan.movies.ui.splash

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.morgan.movies.ui.base.BaseViewModel

class SplashViewModel : BaseViewModel() {
     val REQUEST_EXTERNAL_STORAGE = 1
     val REQUEST_FINE_LOCATION = 2
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA)

    fun verifyStoragePermissions(activity: Activity): Boolean {
        // Check if we have write permission
        val permissionRead =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionWrite =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionCamera =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        return if (permissionRead != PackageManager.PERMISSION_GRANTED ||
            permissionWrite != PackageManager.PERMISSION_GRANTED ||
            permissionCamera != PackageManager.PERMISSION_GRANTED
        ) { // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
            Log.e("Storage false", "${PERMISSIONS_STORAGE[0]} ${PERMISSIONS_STORAGE[1]} ${PERMISSIONS_STORAGE[2]}")
            false
        } else {
            Log.e("Storage true", "${PERMISSIONS_STORAGE[0]} ${PERMISSIONS_STORAGE[1]} ${PERMISSIONS_STORAGE[2]}")
            true
        }
    }

    fun verifyLocationPermissions(activity: Activity): Boolean {
        val accessFineLocationPermission =
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        return if (accessFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            var permissions = arrayOf<String?>(Manifest.permission.ACCESS_FINE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissions = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
            ActivityCompat.requestPermissions(
                activity,
                permissions,
                REQUEST_FINE_LOCATION
            )
            //  Log.e("location false","${permissions[0]}+${permissions[1]}")
            false
        } else {
            Log.e("location true",accessFineLocationPermission.toString())
            true
        }
    }
}