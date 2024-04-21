package com.fatih.newsapp.domain.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.fatih.newsapp.R
import com.fatih.newsapp.data.remote.dto.NewsDto
import com.fatih.newsapp.databinding.RecyclerRowBinding
import com.fatih.newsapp.domain.model.NewsModel
import com.fatih.newsapp.presentation.viewmodel.NewsViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private lateinit var binding: RecyclerRowBinding

    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RecyclerRowBinding.inflate(inflater,parent,false)
        return ViewHolder()
    }
    inner class ViewHolder : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(differ.currentList[position].title !="[Removed]"){

            val url = differ.currentList[position].url

            val binding = RecyclerRowBinding.bind(holder.itemView)
            binding.txtAuthor.text = differ.currentList[position].author
            binding.txtTitle.text = differ.currentList[position].title
            binding.txtTarih.text = formatDate(differ.currentList[position].publishedAt.toString())
            binding.imgNews.downloadFromUrl(differ.currentList[position].urlToImage.toString())

            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }

        }
        else{
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0,0)
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<NewsDto.Article>(){
        override fun areItemsTheSame(oldItem: NewsDto.Article, newItem: NewsDto.Article): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NewsDto.Article,
            newItem: NewsDto.Article
        ): Boolean {
            return oldItem == newItem
        }


    }

    val differ = AsyncListDiffer(this,differCallback)

    fun ImageView.downloadFromUrl(url: String?) {
        val circularProgressDrawable = CircularProgressDrawable(context).apply {
            strokeWidth = 10f
            centerRadius = 50f
            setColorSchemeColors(Color.RED, Color.BLUE,Color.GREEN)
            start()
    }

    Glide.with(context)
        .load(url)
        .placeholder(circularProgressDrawable)
        .into(this)
}

}
