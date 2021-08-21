package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

const val DB_NAME = "todo.db"
class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    var finalTime : Long = 0L
    var finalDate: Long = 0L

    private val spinnerLabels = arrayListOf(
            "Banking","Personal", "Health", "Office","Family","Shopping"
    )

    val db by lazy{
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        dateEdt.setOnClickListener(this)
        timeEdt.setOnClickListener(this)
        saveBtn.setOnClickListener(this)
        setUpSpinner()
    }

    private fun setUpSpinner() {

        val spinnerAdapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerLabels
        )
        spinnerLabels.sort()
        spinnerCategory.adapter = spinnerAdapter
    }

    override fun onClick(v: View) {

        when(v.id) {
            R.id.dateEdt ->{
                setListener()
            }
            R.id.timeEdt ->{
                setTimeListener()
            }
            R.id.saveBtn ->{
                saveTodo()
            }
        }
    }

    private fun saveTodo() {
        val title = titleText.text.toString()
        val task = taskText.text.toString()
        val category = spinnerCategory.selectedItem.toString()
        val todoModel = TodoModel(
                title,
                task,
                category,
                finalDate,
                finalTime,
                0
        )

        GlobalScope.launch(Dispatchers.IO) {
            db.todoDao().insertTask(todoModel)
        }
        finish()
    }


    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()

        timeSetListener = TimePickerDialog.OnTimeSetListener{ timePicker: TimePicker, hourOfDay: Int, minute: Int ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            myCalendar.set(Calendar.MINUTE, minute)
            updateTime()
        }

        var timePickerDialog = TimePickerDialog(
                this,
                timeSetListener,
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),
                false
        )
        timePickerDialog.show()
    }

    private fun setListener(){
        myCalendar = Calendar.getInstance()
        dateSetListener = DatePickerDialog.OnDateSetListener{_: DatePicker, year: Int, month:Int, dayOfMonth:Int->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
                this,
                dateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)
        dateEdt.setText(sdf.format(myCalendar.time))
        finalDate = myCalendar.time.time
        timeInptLay.visibility = View.VISIBLE
    }

    private fun updateTime() {
        val format = "h: mm a"
        var sdf = SimpleDateFormat(format)
        finalTime = myCalendar.time.time
        timeEdt.setText(sdf.format(myCalendar.time))
    }
}
