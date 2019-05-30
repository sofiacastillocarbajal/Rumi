package pe.edu.upc.rumi.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_incidence.view.*
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.activities.IncidenceDetailActivity
import pe.edu.upc.rumi.activities.MyIncidentDetailActivity
import pe.edu.upc.rumi.model.Incident
import java.io.Serializable

class IncidentsAdapter(var incidents: ArrayList<Incident>, var tipo: Int) :
    RecyclerView.Adapter<IncidentsAdapter.ViewHolder>() {
    var selectedIndex = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context
        val itemGroup = itemView.itemGroup
        val incidenceImageView = itemView.incidenceImageView
        val descriptionTextView = itemView.descriptionTextView
        val profileNameTextView = itemView.profileNameTextView

        fun bindTo(incident: Incident) {
            incidenceImageView.apply {
                setDefaultImageResId(R.drawable.ic_build)
                setErrorImageResId(R.drawable.ic_build)
            }
            itemGroup.setOnClickListener {
                if (tipo == 1) {
                    this@IncidentsAdapter.selectedIndex = adapterPosition
                    val intent = Intent(it.context, IncidenceDetailActivity::class.java)
                    intent.putExtra("incident", incident as Serializable)
                    it.context.startActivity(intent)
                }
                else if (tipo == 2) {
                    this@IncidentsAdapter.selectedIndex = adapterPosition
                    val intent = Intent(it.context, MyIncidentDetailActivity::class.java)
                    intent.putExtra("incident", incident as Serializable)
                    it.context.startActivity(intent)
                }
            }
            descriptionTextView.apply {
                text = incident.description
            }
            profileNameTextView.apply {
                text = incident.profile?.name + " " + incident.profile?.surname
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentsAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_incidence, parent, false))
    }

    override fun getItemCount(): Int {
        return incidents.size
    }

    override fun onBindViewHolder(holder: IncidentsAdapter.ViewHolder, position: Int) {
        holder.bindTo(incidents.get(position))
    }

    fun verifyItemChanged() {
        notifyItemChanged(selectedIndex)
    }
}