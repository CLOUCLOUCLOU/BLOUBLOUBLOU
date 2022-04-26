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
        val editDescription  = findViewById<EditText>(R.id.edtiTextDescription);

        val task = intent.getSerializableExtra("editTask") as Task?;


        editTitle.setText(task?.title);
        editDescription.setText(task?.description);

        val id = task?.id ?: UUID.randomUUID().toString()

        btnValidate.setOnClickListener{
            val newTask = Task(
                id = id,
                title = editTitle.text.toString(),
                description = editDescription.text.toString())
            intent.putExtra("task", newTask);
            setResult(RESULT_OK, intent);
            finish()
        }



    }
}