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
import kotlinx.android.synthetic.main.fragment_my_tasks.view.*
import org.json.JSONArray
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.activities.ProfileDetailActivity
import pe.edu.upc.rumi.adapters.TasksAdapter
import pe.edu.upc.rumi.model.Group
import pe.edu.upc.rumi.model.Task
import pe.edu.upc.rumi.network.RumiApi
import pe.edu.upc.rumi.util.UserDefaults
import java.io.Serializable


class MyTasksFragment : Fragment() {

    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var vista: View
    private lateinit var profileImageButton: AppCompatImageButton

    private var groups: ArrayList<Group> = ArrayList()
    private var tasks: ArrayList<Task> = ArrayList<Task>()
    private var tasksAdapter = TasksAdapter(tasks, 2)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vista = view
        progressBar = view.progressBar

        tasksRecyclerView = view.tasksRecyclerView
        tasksRecyclerView.apply {
            adapter = tasksAdapter
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
        getTasksHistory()
        tasksAdapter.verifyItemChanged()
    }

    private fun getTasksHistory() {
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
                    getTasks()
                }
                override fun onError(anError: ANError) {
                    anError?.apply { Toast.makeText(vista.context, "Error al cargar los grupos", Toast.LENGTH_LONG).show() }
                    groups = ArrayList()
                    progressBar.visibility = View.GONE
                }
            })
    }

    fun getTasks() {
        tasks.clear()
        if (groups.isNotEmpty()){
            getTasksByGroup(groups[0].groupId)
        }
    }

    private fun getTasksByGroup(groupId: String?) {
        progressBar.visibility = View.VISIBLE

        AndroidNetworking.get(RumiApi.tasksByGroup(groupId))
            .addHeaders("Authorization", "Bearer ${UserDefaults.token}")
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.HIGH)
            .setTag(getString(R.string.app_name))
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    tasksAdapter.apply {
                        tasks.addAll(Task.from(response))
                        notifyDataSetChanged()
                    }
                    progressBar.visibility = View.GONE
                }
                override fun onError(anError: ANError) {
                    anError?.apply { Toast.makeText(vista.context, "Error al cargar los grupos", Toast.LENGTH_LONG).show() }
                    tasksAdapter.apply {
                        tasks = ArrayList()
                        notifyDataSetChanged()
                    }
                    progressBar.visibility = View.GONE
                }
            })
    }
}
