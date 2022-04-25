package com.clouclouclou.bloublou.tasklist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.clouclouclou.bloublou.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

object ItemsDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    // are they the same "entity" ? (usually same id)j
    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }// do they have the same data ? (content)
}


// l'IDE va râler ici car on a pas encore implémenté les méthodes nécessaires
class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(ItemsDiffCallback) {

    var onClickDelete: (Task) -> Unit = {}
    var onClickEdit : (Task) -> Unit = {}
    // on utilise `inner` ici afin d'avoir accès aux propriétés de l'adapter directement
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deleteButton = itemView.findViewById<ImageButton>(R.id.buttonDelete);
        val editButton = itemView.findViewById<ImageButton>(R.id.buttonEditable);
        fun bind(task: Task) {
            val title = itemView.findViewById<TextView>(R.id.task_title)
            val description = itemView.findViewById<TextView>(R.id.task_description)

            title.text = task.title
            description.text = task.description
        }
        fun deleteTask (task: Task) {
            deleteButton.setOnClickListener {
                onClickDelete(task);
            }
        }
        fun editTask (task : Task) {
            editButton.setOnClickListener{
                onClickEdit(task);
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false);
        return TaskViewHolder(itemView);
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.deleteTask(getItem(position))

    }

}