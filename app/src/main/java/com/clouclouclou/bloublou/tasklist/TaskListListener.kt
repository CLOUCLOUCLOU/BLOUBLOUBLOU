package com.clouclouclou.bloublou.tasklist

import com.clouclouclou.bloublou.model.Task

interface  TaskListListener {
    fun onClickDelete(task : Task);
    fun onClickEdit(task: Task);

}