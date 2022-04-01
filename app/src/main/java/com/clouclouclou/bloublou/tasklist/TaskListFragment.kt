package com.clouclouclou.bloublou.tasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clouclouclou.bloublou.R
import com.clouclouclou.bloublou.databinding.TasklistfragmentBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.UUID


class TaskListFragment : Fragment() {


    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    private var _binding: TasklistfragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = TaskListAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TasklistfragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recylerview)
        recyclerView.adapter = this.adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.submitList(taskList);
        binding.fabButton.setOnClickListener() {
            val newTask =
                Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList += newTask
            adapter.submitList(taskList)
        }
    }


}