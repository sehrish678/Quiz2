package com.example.quiz2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiz2.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val apiKey = "df53471f3cc067b40edd4305363fa73c"
    
    private val countries = mapOf(
        "Pakistan" to "pk",
        "United States" to "us",
        "United Kingdom" to "gb",
        "India" to "in",
        "Saudi Arabia" to "sa",
        "UAE" to "ae"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        setupRecyclerView()

        binding.btnRefresh.setOnClickListener {
            val selectedCountry = countries[binding.spinnerCountry.selectedItem.toString()] ?: "us"
            fetchNews(selectedCountry)
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = adapter

        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountry = countries[parent?.getItemAtPosition(position).toString()] ?: "us"
                fetchNews(selectedCountry)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupRecyclerView() {
        binding.rvNews.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchNews(country: String) {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getTopHeadlines(country = country, apikey = apiKey)
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val articles = response.body()!!.articles
                    binding.rvNews.adapter = NewsAdapter(articles) { article ->
                        val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                            putExtra("title", article.title)
                            putExtra("description", article.description)
                            putExtra("content", article.content)
                            putExtra("image", article.image)
                            putExtra("source", article.source.name)
                            putExtra("date", article.publishedAt)
                            putExtra("url", article.url)
                        }
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Failed to connect: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
