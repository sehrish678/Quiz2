package com.example.quiz2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quiz2.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val content = intent.getStringExtra("content")
        val image = intent.getStringExtra("image")
        val source = intent.getStringExtra("source")
        val date = intent.getStringExtra("date")
        val url = intent.getStringExtra("url")

        binding.apply {
            tvDetailTitle.text = title
            tvDetailDescription.text = description
            tvDetailContent.text = content
            tvDetailSource.text = source
            tvDetailDate.text = date?.substringBefore("T")

            Glide.with(this@DetailActivity)
                .load(image)
                .placeholder(android.R.drawable.progress_horizontal)
                .error(android.R.drawable.stat_notify_error)
                .into(ivDetailImage)

            btnReadFull.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
        }
    }
}
