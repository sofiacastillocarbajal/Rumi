package pe.edu.upc.rumi.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.GridLayoutManager
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
import kotlinx.android.synthetic.main.fragment_groups.view.*
import org.json.JSONArray

import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.activities.PropietarioProfileDetailActivity
import pe.edu.upc.rumi.adapters.GroupsAdapter
import pe.edu.upc.rumi.model.Group
import pe.edu.upc.rumi.network.RumiApi
import pe.edu.upc.rumi.util.UserDefaults


class GroupsFragment : Fragment() {
    private lateinit var groupsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var vista: View
    private lateinit var profileImageButton: AppCompatImageButton

    private var groups: List<Group> = ArrayList()
    private var groupsAdapter = GroupsAdapter(groups)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vista = view
        progressBar = view.progressBar
        groupsRecyclerView = view.groupsRecyclerView
        groupsRecyclerView.apply {
            adapter = groupsAdapter
            this.layoutManager = GridLayoutManager(view.context, 2)
        }

        profileImageButton = view.profileImageButton
        profileImageButton.setOnClickListener {
            val intent = Intent(view.context, PropietarioProfileDetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getGroups()
        groupsAdapter.verifyItemChanged()
    }

    private fun getGroups() {
        progressBar.visibility = View.VISIBLE

        AndroidNetworking.get(RumiApi.groupsUrl)
            .addHeaders("Authorization", "Bearer ${UserDefaults.token}")
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.HIGH)
            .setTag(getString(R.string.app_name))
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    groupsAdapter.apply {
                        groups = Group.from(response)
                        notifyDataSetChanged()
                    }

                    progressBar.visibility = View.GONE
                }
                override fun onError(anError: ANError) {
                    anError?.apply { Toast.makeText(vista.context, "Error al cargar los grupos", Toast.LENGTH_LONG).show() }
                    groupsAdapter.apply {
                        groups = ArrayList()
                        notifyDataSetChanged()
                    }

                    progressBar.visibility = View.GONE
                }
            })
    }
}
