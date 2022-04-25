package com.clouclouclou.bloublou.tasklist

import java.io.Serializable

data class Task(val id: String, val title: String, val description  : String? = null):Serializable {}
