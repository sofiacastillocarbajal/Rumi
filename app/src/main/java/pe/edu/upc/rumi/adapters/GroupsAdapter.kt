package pe.edu.upc.rumi.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_group.view.*
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.activities.GroupDetailActivity
import pe.edu.upc.rumi.model.Group
import java.io.Serializable

class GroupsAdapter(var groups: List<Group>) :
    RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
    var selectedIndex = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context
        val itemGroup = itemView.itemGroup
        val groupImageView = itemView.groupImageView
        val nameTextView = itemView.nameTextView

        fun bindTo(group: Group) {
            groupImageView.apply {
                setDefaultImageResId(R.drawable.ic_group)
                setErrorImageResId(R.drawable.ic_group)
            }
            nameTextView.text = group.name
            itemGroup.setOnClickListener {
                this@GroupsAdapter.selectedIndex = adapterPosition
                val intent = Intent(it.context, GroupDetailActivity::class.java)
                intent.putExtra("group", group as Serializable)
                it.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_group, parent, false))
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    override fun onBindViewHolder(holder: GroupsAdapter.ViewHolder, position: Int) {
        holder.bindTo(groups.get(position))
    }

    fun verifyItemChanged() {
        notifyItemChanged(selectedIndex)
    }
}