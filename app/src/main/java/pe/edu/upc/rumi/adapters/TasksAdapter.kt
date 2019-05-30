package pe.edu.upc.rumi.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_task.view.*
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.activities.OwnerTaskDetailActivity
import pe.edu.upc.rumi.activities.TaskDetailActivity
import pe.edu.upc.rumi.model.Task
import java.io.Serializable

class TasksAdapter(var tasks: ArrayList<Task>, var tipo:Int) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    var selectedIndex = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context
        val itemGroup = itemView.itemGroup
        val taskImageView = itemView.taskImageView
        val descriptionTextView = itemView.descriptionTextView
        val groupNameTextView = itemView.groupNameTextView

        fun bindTo(task: Task) {
            taskImageView.apply {
                setDefaultImageResId(R.drawable.ic_task)
                setErrorImageResId(R.drawable.ic_task)
            }
            itemGroup.setOnClickListener {
                if (tipo == 1) {
                    this@TasksAdapter.selectedIndex = adapterPosition
                    val intent = Intent(it.context, OwnerTaskDetailActivity::class.java)
                    intent.putExtra("task", task as Serializable)
                    it.context.startActivity(intent)
                }
                else if (tipo == 2){
                    this@TasksAdapter.selectedIndex = adapterPosition
                    val intent = Intent(it.context, TaskDetailActivity::class.java)
                    intent.putExtra("task", task as Serializable)
                    it.context.startActivity(intent)
                }
            }
            descriptionTextView.apply {
                text = task.title
            }
            groupNameTextView.apply {
                text = task.groupName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false))
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TasksAdapter.ViewHolder, position: Int) {
        holder.bindTo(tasks.get(position))
    }

    fun verifyItemChanged() {
        notifyItemChanged(selectedIndex)
    }
}