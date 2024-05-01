package com.fatih.newsapp.presentation.views

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatih.newsapp.R
import com.fatih.newsapp.databinding.ActivityMainBinding
import com.fatih.newsapp.domain.adapter.NewsAdapter
import com.fatih.newsapp.presentation.viewmodel.NewsViewModel
import com.fatih.newsapp.util.Constants.API_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val newsAdapter by lazy {NewsAdapter()}
    private lateinit var binding: ActivityMainBinding
    private val newsViewModel: NewsViewModel by viewModels()
    private var countryName = "us"
    private var categorySelect = "general"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnBusiness.setOnClickListener {
            categorySelect = "business"
            newsViewModel.refreshData(country = countryName, apiKey = API_KEY, category =categorySelect)
        }
        binding.btnEntertainment.setOnClickListener {
            categorySelect = "entertainment"
            newsViewModel.refreshData(country = countryName, apiKey = API_KEY, category =categorySelect)
        }
        binding.btnGeneral.setOnClickListener {
            categorySelect = "general"
            newsViewModel.refreshData(country = countryName, apiKey = API_KEY, category =categorySelect)
        }
        binding.btnHealth.setOnClickListener {
            categorySelect = "health"
            newsViewModel.refreshData(country = countryName, apiKey = API_KEY, category =categorySelect)
        }
        binding.btnScience.setOnClickListener {
            categorySelect = "science"
            newsViewModel.refreshData(country = countryName, apiKey = API_KEY, category =categorySelect)
        }
        binding.btnSports.setOnClickListener {
            categorySelect = "sports"
            newsViewModel.refreshData(country = countryName, apiKey = API_KEY, category =categorySelect)
        }
        binding.btnTech.setOnClickListener {
            categorySelect = "technology"
            newsViewModel.refreshData(country = countryName, apiKey = API_KEY, category =categorySelect)
        }

        binding.topAppBar.setOnMenuItemClickListener {menuItem->
            when(menuItem.itemId){
                R.id.turkiye ->{
                    countryName ="tr"
                    newsViewModel.refreshData(country = countryName, apiKey = API_KEY, category =categorySelect )
                    true
                }
                R.id.abd ->{
                    countryName ="us"
                    newsViewModel.refreshData(country = countryName, apiKey = API_KEY,category =categorySelect )
                    true
                }
                R.id.Italy ->{
                    countryName="it"
                    newsViewModel.refreshData(country = countryName, apiKey = API_KEY,category =categorySelect )
                    true
                }
                R.id.fransa ->{
                    countryName ="fr"
                    newsViewModel.refreshData(country = countryName, apiKey = API_KEY,category =categorySelect )
                    true
                }
                else ->{
                    false
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.newsRcycler.visibility = View.GONE
            newsViewModel.refreshData(country = countryName, apiKey = API_KEY,category =categorySelect )
            binding.swipeRefreshLayout.isRefreshing = false
            binding.newsRcycler.visibility = View.VISIBLE
        }
        binding.apply {
            newsViewModel.fetchNews(country = countryName, apiKey = API_KEY,category =categorySelect )
            newsViewModel.currentWeather.observe(this@MainActivity, Observer {
                it?.let {
                    newsAdapter.differ.submitList(it.articles)
                    newsRcycler.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
                        adapter = newsAdapter
                    }
                }
            })
        }
        binding.apply {
            newsViewModel.newDb.observe(this@MainActivity, Observer {
                it.map {
                    newsAdapter.differ.submitList(it.articles)
                    newsRcycler.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
                        adapter = newsAdapter
                    }
                }
            })
        }


    }
}

