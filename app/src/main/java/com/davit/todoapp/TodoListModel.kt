package com.davit.todoapp

data class TodoListModel(
    var text: String? = "",
    var isComplete : Boolean? = false,
    var isEdit : Boolean? = false
)