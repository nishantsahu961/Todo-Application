package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.item_todo.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class TodoAdapter(val list: List<TodoModel>):RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
                LayoutInflater.from(parent.context).
                inflate(R.layout.item_todo, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemId(position: Int): Long {
        return list[position].id
    }
    override fun getItemCount(): Int = list.size

    class TodoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(todoModel: TodoModel) {
            with(itemView){
                val colors = resources.getIntArray(R.array.random_color)
                val randomColor = colors[Random.nextInt(colors.size)]
                viewColorTag.setBackgroundColor(randomColor)

                txtShowTitle.text = todoModel.title
                txtShowCategory.text = todoModel.category
                txtShowTask.text = todoModel.description
                updateTime(todoModel.time)
                updateDate(todoModel.date)
            }
        }

        private fun updateDate(time:Long){
            val myformat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myformat)
            itemView.txtShowDate.setText(sdf.format(Date(time)))
        }

        private fun updateTime(time:Long){
            val format = "h: mm a"
            var sdf = SimpleDateFormat(format)
            itemView.txtShowTime.text = (sdf.format(Date(time)))
        }
    }
}