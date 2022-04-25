package com.clouclouclou.bloublou.tasklist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clouclouclou.bloublou.R
import com.clouclouclou.bloublou.databinding.TasklistfragmentBinding
import com.clouclouclou.bloublou.form.FormActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.UUID


class TaskListFragment : Fragment() {


    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
    )

    private var _binding: TasklistfragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = TaskListAdapter()
    val formLauncher  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        val task = result.data?.getSerializableExtra("task")  as? Task ?: return@registerForActivityResult
        taskList = taskList + task;
        adapter.submitList(taskList);

    }

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
            val intent = Intent(context, FormActivity::class.java)
            formLauncher.launch(intent)
        }

        adapter.onClickDelete = {task ->
            taskList = taskList - task;
            adapter.submitList(taskList)
        }
   }



}