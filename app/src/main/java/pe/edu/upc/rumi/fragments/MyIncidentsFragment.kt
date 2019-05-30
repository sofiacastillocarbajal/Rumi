package pe.edu.upc.rumi.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import kotlinx.android.synthetic.main.fragment_my_incidents.view.*
import org.json.JSONArray

import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.activities.ProfileDetailActivity
import pe.edu.upc.rumi.adapters.IncidentsAdapter
import pe.edu.upc.rumi.model.Group
import pe.edu.upc.rumi.model.Incident
import pe.edu.upc.rumi.network.RumiApi
import pe.edu.upc.rumi.util.UserDefaults
import java.io.Serializable


class MyIncidentsFragment : Fragment() {
    private lateinit var incidentsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var vista: View
    private lateinit var profileImageButton: AppCompatImageButton

    private var groups: List<Group> = ArrayList()
    private var incidents: ArrayList<Incident> = ArrayList<Incident>()
    private var incidentsAdapter = IncidentsAdapter(incidents, 2)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_incidents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vista = view
        progressBar = view.progressBar

        incidentsRecyclerView = view.incidentsRecyclerView
        incidentsRecyclerView.apply {
            adapter = incidentsAdapter
            this.layoutManager = LinearLayoutManager(view.context)
        }

        profileImageButton = view.profileImageButton
        profileImageButton.setOnClickListener {
            val intent = Intent(view.context, ProfileDetailActivity::class.java)
            intent.putExtra("profile", UserDefaults.toProfile() as Serializable)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getIncidentsHistory()
        incidentsAdapter.verifyItemChanged()
    }

    private fun getIncidentsHistory() {
        progressBar.visibility = View.VISIBLE

        AndroidNetworking.get(RumiApi.groupsUrl)
            .addHeaders("Authorization", "Bearer ${UserDefaults.token}")
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.HIGH)
            .setTag(getString(R.string.app_name))
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    groups = Group.from(response)
                    getIncidents()
                }
                override fun onError(anError: ANError) {
                    anError?.apply { Toast.makeText(vista.context, "Error al cargar los grupos", Toast.LENGTH_LONG).show() }
                    groups = ArrayList()
                    progressBar.visibility = View.GONE
                }
            })
    }

    fun getIncidents() {
        incidents.clear()
        if (groups.isNotEmpty()){
            getIncidentsByGroup(groups[0].groupId)
        }
    }

    private fun getIncidentsByGroup(groupId: String?) {
        progressBar.visibility = View.VISIBLE

        AndroidNetworking.get(RumiApi.incidencesByGroup(groupId))
            .addHeaders("Authorization", "Bearer ${UserDefaults.token}")
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.HIGH)
            .setTag(getString(R.string.app_name))
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    incidentsAdapter.apply {
                        incidents.addAll(Incident.from(response))
                        notifyDataSetChanged()
                    }
                    if (incidents.size == 0)Toast.makeText(vista.context, "Sin incidentes en el grupo al que pertenece", Toast.LENGTH_LONG).show()

                    progressBar.visibility = View.GONE
                }
                override fun onError(anError: ANError) {
                    anError?.apply { Toast.makeText(vista.context, "Error al cargar las incidencias", Toast.LENGTH_LONG).show() }
                    incidentsAdapter.apply {
                        incidents = ArrayList()
                        notifyDataSetChanged()
                    }
                    progressBar.visibility = View.GONE
                }
            })
    }
}
