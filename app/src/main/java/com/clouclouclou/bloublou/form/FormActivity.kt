package com.clouclouclou.bloublou.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.clouclouclou.bloublou.R
import com.clouclouclou.bloublou.tasklist.Task
import java.util.*

class FormActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val btnValidate = findViewById<Button>(R.id.btnValidate);
        val editTitle = findViewById<EditText>(R.id.editTextTitle);
        val editButton  = findViewById<EditText>(R.id.edtiTextDescription);

        btnValidate.setOnClickListener{
            val newTask = Task(
                id = UUID.randomUUID().toString(),
                title = editTitle.text.toString(),
                description =editButton.text.toString())
            intent.putExtra("task", newTask);
            setResult(RESULT_OK, intent);
            finish()
        }

    }
}