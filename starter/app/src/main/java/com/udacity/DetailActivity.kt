package com.udacity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding
import org.w3c.dom.Text

class DetailActivity : AppCompatActivity() {

    private var fileName = ""
    private var status = ""

    private lateinit var okButton : Button
    private lateinit var fileNameTextView: TextView
    private lateinit var statusText : TextView

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        okButton = findViewById(R.id.ok_button)
        fileNameTextView = findViewById(R.id.file_name)
        statusText = findViewById(R.id.status_text)

        okButton.setOnClickListener {
            returnToMainActivity()
        }

//        fileName = intent.getStringExtra("fileName").toString()
//        status = intent.getStringExtra("status").toString()
        fileNameTextView.text = intent.getStringExtra("fileName").toString()
        statusText.text = intent.getStringExtra("status").toString()
        Log.d("DetailActivity", "$fileName $statusText")

    }

    private fun returnToMainActivity() {
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
    }
}
