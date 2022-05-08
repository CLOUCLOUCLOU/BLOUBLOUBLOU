package com.clouclouclou.bloublou.tasklist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clouclouclou.bloublou.R
import com.clouclouclou.bloublou.databinding.TasklistfragmentBinding
import com.clouclouclou.bloublou.form.FormActivity
import com.clouclouclou.bloublou.network.Api
import com.clouclouclou.bloublou.model.Task
import com.clouclouclou.bloublou.network.viewmodel.TasksListViewModel
import kotlinx.coroutines.launch
import coil.load
import coil.transform.CircleCropTransformation
import com.clouclouclou.bloublou.user.UserInfoActivity
import java.util.*


class TaskListFragment : Fragment() {

   private val tasksListViewModel : TasksListViewModel by viewModels();

    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
    )

    private var _binding: TasklistfragmentBinding? = null
    private val binding get() = _binding!!


    val adapterListener: TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            taskList = taskList - task;
            adapter.submitList(taskList)
        }

        override fun onClickEdit(task: Task) {
            val intent = Intent(context, FormActivity::class.java)
            intent.putExtra("editTask", task)
            editTask.launch(intent)
        }
    }
    private val adapter = TaskListAdapter(adapterListener)


    val createTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = result.data?.getSerializableExtra("task") as? Task
                ?: return@registerForActivityResult
            taskList = taskList + task;
            adapter.submitList(taskList);

        }

    val editTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = result.data?.getSerializableExtra("task") as? Task
                ?: return@registerForActivityResult
            taskList = taskList.map { if (it.id == task.id) task else it }
            adapter.submitList(taskList);

        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TasklistfragmentBinding.inflate(inflater, container, false)

        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            tasksListViewModel.tasksStateFlow.collect { newList ->
                // cette lambda est executée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter

                adapter.submitList(newList);
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        val userInfoTextView = view?.findViewById<TextView>(R.id.userInfoTextView);
        val avatarImageView = view?.findViewById<ImageView>(R.id.avatarImageView);
        lifecycleScope.launch {
            tasksListViewModel.refresh();
        }


        lifecycleScope.launch {
            val info = Api.userWebService.getInfo()
            val userInfo = info.body();
            userInfoTextView?.text = "${userInfo?.firstName} ${userInfo?.lastName}"

            binding.avatarImageView.load("https://goo.gl/gEgYUd") {
                transformations(CircleCropTransformation())
            }
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recylerview)

        recyclerView.adapter = this.adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.submitList(taskList);
        binding.fabButton.setOnClickListener() {
            val intent = Intent(context, FormActivity::class.java)
            createTask.launch(intent)
        }
        binding.avatarImageView.setOnClickListener {
            val intent = Intent(context, UserInfoActivity::class.java)
            startActivity(intent)
        }


    }
}