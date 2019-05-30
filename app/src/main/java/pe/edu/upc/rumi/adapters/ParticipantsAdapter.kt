package pe.edu.upc.rumi.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_participant.view.*
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.activities.ProfileDetailActivity
import pe.edu.upc.rumi.model.Profile
import java.io.Serializable

class ParticipantsAdapter(var participants: List<Profile>) :
    RecyclerView.Adapter<ParticipantsAdapter.ViewHolder>() {
    var selectedIndex = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context
        val itemGroup = itemView.itemGroup
        val groupImageView = itemView.groupImageView
        val nameTextView = itemView.nameTextView
        val phoneTextView = itemView.phoneTextView

        fun bindTo(participant: Profile) {
            groupImageView.apply {
                setDefaultImageResId(R.drawable.ic_account_circle_primary)
                setErrorImageResId(R.drawable.ic_account_circle_primary)
            }
            nameTextView.text = participant.name
            if (participant.phone == "null") phoneTextView.text = "Sin teléfono"
            else phoneTextView.text = participant.phone
            itemGroup.setOnClickListener {
                this@ParticipantsAdapter.selectedIndex = adapterPosition
                val intent = Intent(it.context, ProfileDetailActivity::class.java)
                intent.putExtra("profile", participant as Serializable)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_participant, parent, false))
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    override fun onBindViewHolder(holder: ParticipantsAdapter.ViewHolder, position: Int) {
        holder.bindTo(participants.get(position))
    }

    fun verifyItemChanged() {
        notifyItemChanged(selectedIndex)
    }
}