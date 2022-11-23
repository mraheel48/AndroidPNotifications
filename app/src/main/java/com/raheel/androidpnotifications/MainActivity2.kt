package com.raheel.androidpnotifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.raheel.androidpnotifications.databinding.ActivityMain2Binding
import pub.devrel.easypermissions.EasyPermissions

class MainActivity2 : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var mainBinding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.btnNoti.setOnClickListener {

            //for notification channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                if (EasyPermissions.hasPermissions(
                        this@MainActivity2,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                ) {
                    Log.d("myPermission", "hasPermissions allow")
                    createNotificationChannel()
                } else {
                    EasyPermissions.requestPermissions(
                        this@MainActivity2, "Please allow permissions to proceed further",
                        1001, Manifest.permission.POST_NOTIFICATIONS
                    )
                }

            } else {
                createNotificationChannel()
            }
        }

        mainBinding.showMes.setOnClickListener {

            val notificationManager = ContextCompat.getSystemService(
                applicationContext, NotificationManager::class.java
            ) as NotificationManager

            notificationManager.cancelNotifications()

            notificationManager.sendNotification(
                "This is message Tile",
                "This is Message Body",
                applicationContext
            )

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //Note this method not Remove in Application
    //1. This is Allow to create Channel in Android
    //2. Also Allow the Android 33 Notification Permission
    //3. Calling in In Your App.!!!
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                applicationContext.getString(R.string.channel_1),
                applicationContext.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Reminder"

            val notificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

        when (requestCode) {
            1001 -> {
                if (perms.size == 1) {
                    Log.d("myPermissionsGranted", "all Permission allow")
                    createNotificationChannel()
                } else {
                    Log.d("myPermissionsGranted", "not all Permission allow")
                }
            }
            else -> {
                Log.d("myPermissionsGranted", "no any  Permission allow")
            }
        }

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }
}