package com.example.todoapp

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoModel::class], version = 1)
abstract class AppDatabase: RoomDatabase(){

    abstract fun todoDao(): TodoDao

    companion object {

//        use of Volatile is that , whenever such variable value is changed it gets updated in all
//        the threads associated with that variable

        @Volatile
        private var INSTANCE: AppDatabase?=null

        fun getDatabase(context: Context):AppDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}