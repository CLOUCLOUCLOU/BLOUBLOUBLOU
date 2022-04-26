package com.clouclouclou.bloublou.tasklist

interface  TaskListListener {
    fun onClickDelete(task : Task);
    fun onClickEdit(task: Task);

}