package com.davit.todoapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davit.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private var arrData: ArrayList<TodoListModel>? = ArrayList()
    private var adapter: AdapterTodoList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
        initAdapter()

        binding.btnAdd.setOnClickListener {
            val textInput = binding.edText.text.toString()
            if (textInput.isNotEmpty()) {
                if (arrData != null && arrData?.isNotEmpty() == true) {
                    var isDuplicate = false

                    for (item in arrData ?: ArrayList()) {
                        if (item.text == textInput) {
                            isDuplicate = true
                            break
                        }
                    }

                    if (!isDuplicate) {
                        addData(textInput)
                    } else {
                        Toast.makeText(this, "Data cannot duplicate", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    addData(textInput)
                }
            } else {
                Toast.makeText(this, "Data cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addData(text: String){
        arrData?.add(TodoListModel(binding.edText.text.toString()))
        adapter?.notifyDataSetChanged()
    }


    private fun initAdapter(){
        adapter = AdapterTodoList(this,arrData ?: ArrayList())
        binding.recyclerview.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        binding.recyclerview.adapter = adapter
    }
}