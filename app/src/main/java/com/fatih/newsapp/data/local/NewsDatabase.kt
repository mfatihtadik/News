package com.fatih.newsapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fatih.newsapp.data.convertor.NewsModelTypeConverter
import com.fatih.newsapp.domain.model.NewsModel

@Database(entities = arrayOf(NewsModel::class), version = 1)
@TypeConverters(NewsModelTypeConverter::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object{

        @Volatile private var instance : NewsDatabase? = null

        private val lock = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(lock){
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,NewsDatabase::class.java,"newsdatabase"
        ).build()

    }

}