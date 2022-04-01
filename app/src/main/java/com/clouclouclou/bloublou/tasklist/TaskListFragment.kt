package com.clouclouclou.bloublou.tasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clouclouclou.bloublou.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import java.util.UUID


class TaskListFragment : Fragment() {


    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    private val adapter = TaskListAdapter()
    private var buttonFab = view?.findViewById<FloatingActionButton>(R.id.fabButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        buttonFab?.setOnClickListener(){
            val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList += newTask
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.adapter.currentList = taskList
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tasklistfragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recylerview)
        recyclerView.adapter = this.adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }


}