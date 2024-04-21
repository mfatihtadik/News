package com.fatih.newsapp.presentation.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatih.newsapp.R
import com.fatih.newsapp.data.remote.dto.NewsDto
import com.fatih.newsapp.databinding.ActivityMainBinding
import com.fatih.newsapp.databinding.RecyclerRowBinding
import com.fatih.newsapp.domain.adapter.NewsAdapter
import com.fatih.newsapp.domain.model.NewsModel
import com.fatih.newsapp.presentation.viewmodel.NewsViewModel
import com.fatih.newsapp.util.Constants.API_KEY
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {


    private val newsAdapter by lazy {NewsAdapter()}
    private lateinit var binding: ActivityMainBinding
    private val newsViewModel: NewsViewModel by viewModels()
    private var countryName = "us"
    private var categorySelect = "sports"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
                    //newsAdapter.differ.submitList(it.articles)
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

