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
import kotlinx.android.synthetic.main.fragment_my_group.view.*
import org.json.JSONArray
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.activities.ProfileDetailActivity
import pe.edu.upc.rumi.adapters.ParticipantsAdapter
import pe.edu.upc.rumi.model.Group
import pe.edu.upc.rumi.model.Profile
import pe.edu.upc.rumi.network.RumiApi
import pe.edu.upc.rumi.util.UserDefaults
import java.io.Serializable


class MyGroupFragment : Fragment() {
    private lateinit var participantsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var vista: View
    private lateinit var profileImageButton: AppCompatImageButton

    private var participants: List<Profile> = ArrayList()
    private var participantsAdapter = ParticipantsAdapter(participants)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vista = view
        progressBar = view.progressBar
        participantsRecyclerView = view.participantsRecyclerView
        participantsRecyclerView.apply {
            adapter = participantsAdapter
            this.layoutManager = GridLayoutManager(view.context, 4)
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
        getGroup()
        participantsAdapter.verifyItemChanged()
    }

    private fun getGroup() {
        progressBar.visibility = View.VISIBLE

        AndroidNetworking.get(RumiApi.groupsUrl)
            .addHeaders("Authorization", "Bearer ${UserDefaults.token}")
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.HIGH)
            .setTag(getString(R.string.app_name))
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    val groups = Group.from(response)
                    if (groups.isNotEmpty()){
                        participants = groups[0].participants!!
                        participantsAdapter.participants = participants
                        participantsAdapter.apply {
                            notifyDataSetChanged()
                        }
                    }

                    progressBar.visibility = View.GONE
                }
                override fun onError(anError: ANError) {
                    anError?.apply { Toast.makeText(vista.context, "Error al cargar los compañeros de grupo", Toast.LENGTH_LONG).show() }
                    participantsAdapter.apply {
                        participants = ArrayList()
                        notifyDataSetChanged()
                    }

                    progressBar.visibility = View.GONE
                }
            })
    }
}
