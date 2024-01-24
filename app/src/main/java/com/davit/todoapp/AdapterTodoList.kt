package com.davit.todoapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.davit.todoapp.databinding.ItemTodoListBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class AdapterTodoList(private val context: Context, private var arrData: ArrayList<TodoListModel>,private val isRemove: (id: TodoListModel) -> Unit) :
    Adapter<ViewHolderTodo>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTodo {
        return ViewHolderTodo(
            ItemTodoListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderTodo, position: Int) {
        holder.text.text = arrData[position].text
        holder.btnDelete.setOnClickListener { if (arrData.size > 0)  showDialog(holder.adapterPosition,arrData[holder.adapterPosition])}
        holder.btnEdit.setOnClickListener { if (arrData.size > 0) editItem(holder.adapterPosition) }
        holder.btnComplete.setOnClickListener {
            if (arrData.size > 0) {
                arrData[position].isComplete = arrData[position].isComplete == false
                completeItem(holder.text,holder.btnComplete,holder.adapterPosition,holder.btnEdit)
            }
        }

        if (arrData[position].isEdit == true) holder.cardView.strokeColor = context.getColor(R.color.yellow)
    }

    override fun getItemCount(): Int = arrData.size ?: 0

    private fun deleteItem(position: Int,item: TodoListModel) {
        try {
            arrData.removeAt(position)
            isRemove(item)
            notifyItemRemoved(position)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun editItem(position: Int) {
        try {
            if (arrData.size > 0) {
                showEditDialog(arrData[position], position)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun completeItem(textView: MaterialTextView, icon: ShapeableImageView, position: Int,btnEdit: ShapeableImageView) {
        if (arrData[position].isComplete == true){
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            btnEdit.visibility = View.GONE
        }else{
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            btnEdit.visibility = View.VISIBLE
        }

        icon.setImageDrawable(
            context.resources.getDrawable(
                if (arrData[position].isComplete == true) R.drawable.ic_undo else R.drawable.ic_complete,
                null
            )
        )
    }

    private fun showDialog(position: Int,item: TodoListModel){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_message)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = lp
        val buttonConfirm = dialog.findViewById<Button>(R.id.btnConfirm)
        val buttonCancel = dialog.findViewById<Button>(R.id.btnCancel)
        buttonConfirm.setOnClickListener {
            deleteItem(position,item)
            dialog.dismiss()
        }
        buttonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showEditDialog(todoItem: TodoListModel, position: Int) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_edit_todo)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = lp

        val editTextTitle = dialog.findViewById<EditText>(R.id.editTextTitle)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSave)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)

        editTextTitle.setText(todoItem.text)
        buttonSave.setOnClickListener {
            val newText = editTextTitle.text.toString()
            if (newText.isEmpty()){
                return@setOnClickListener
            }
            val findData = arrData.find { it -> it.text == newText }
            if (findData?.text != newText){
                arrData[position].text = newText
                arrData[position].isEdit = true
                notifyItemChanged(position)
            }else{
                Toast.makeText(context, "Data cannot update duplicate", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }
        buttonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSuggestions(newSuggestions: ArrayList<TodoListModel>) {
        arrData = newSuggestions
        notifyDataSetChanged()
    }
}

class ViewHolderTodo(private val binding: ItemTodoListBinding) : ViewHolder(binding.root) {
    var text = binding.text
    var btnEdit = binding.ivEdite
    val btnDelete = binding.ivDelete
    val btnComplete = binding.ivComplete
    val cardView = binding.cardView
}