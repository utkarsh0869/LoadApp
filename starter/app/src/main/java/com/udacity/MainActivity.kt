package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private var selectedGitHubFileName: String? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingButton: LoadingButton

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        loadingButton = findViewById(R.id.loading_button)
        loadingButton.setOnClickListener {
            Log.d("MainActivity", "loading button clicked")
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Log.d("MainActivity", id.toString())
            val action = intent.action
            Log.d("MainActivity", "inside receiver's onreceive")
            if (downloadID == id) {
                if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    val query = DownloadManager.Query()
                    query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
                    val manager =
                        context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val cursor: Cursor = manager.query(query)
                    if (cursor.moveToFirst()) {
                        if (cursor.count > 0) {
                            val statusColumnIndex =
                                cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

                            // Check if the column exists in the Cursor
                            if (statusColumnIndex != -1) {
                                val status = cursor.getInt(statusColumnIndex)
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                    loadingButton.setLoadingButtonState(ButtonState.Completed)
                                    loadingButton.resetButtonText()
                                    notificationManager.sendNotification(
                                        selectedGitHubFileName,
                                        applicationContext,
                                        "Success"
                                    )
                                    Log.d("MainActivity", "here")
                                } else {
                                    loadingButton.setLoadingButtonState(ButtonState.Completed)
                                    notificationManager.sendNotification(
                                        selectedGitHubFileName,
                                        applicationContext,
                                        "Failed"
                                    )
                                    Log.d("MainActivity", "here2")
                                }
                            } else {
                            }
                        }
                    }
                }
            }
        }
    }

    private fun download() {

        loadingButton.setLoadingButtonState(ButtonState.Clicked)

        if (selectedGitHubFileName != null) {
            loadingButton.setLoadingButtonState(ButtonState.Loading)

            notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
            createChannel("channelId", "Repo")

            var file = File(getExternalFilesDir(null), "/repos")

            if (!file.exists()) {
                file.mkdirs()
            }

            val request =
                DownloadManager.Request(Uri.parse(URL))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "/repos/repository.zip"
                    )


            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)
            Log.d("MainActivity", downloadID.toString())
        } else {
            Toast.makeText(this, "No repo selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download is done!"

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val isChecked = view.isChecked
            when (view.getId()) {
                R.id.glide_button ->
                    if (isChecked) {
                        selectedGitHubFileName = getString(R.string.glide_text)
                    }

                R.id.load_app_button ->
                    if (isChecked) {
                        selectedGitHubFileName = getString(R.string.load_app_text)
                    }

                R.id.retrofit_button -> {
                    if (isChecked) {
                        selectedGitHubFileName = getString(R.string.retrofit_text)
                    }
                }
            }
        }
    }
}
