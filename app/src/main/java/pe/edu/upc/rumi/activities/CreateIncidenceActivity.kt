package pe.edu.upc.rumi.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.CardView
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import org.json.JSONArray
import org.json.JSONObject
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.model.Group
import pe.edu.upc.rumi.model.Task
import pe.edu.upc.rumi.network.RumiApi
import pe.edu.upc.rumi.util.UserDefaults

class CreateIncidenceActivity : AppCompatActivity() {
    private lateinit var backImageButton: AppCompatImageButton
    private lateinit var progressBar: ProgressBar

    private lateinit var cancelarCardView: CardView

    private lateinit var titleCustomEditText: EditText
    private lateinit var descripcionCustomEditText: EditText
    private lateinit var enviarValidCardView: CardView

    private var group: Group = Group()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_incidence)
        supportActionBar?.hide()

        progressBar = findViewById(R.id.progressBar)
        backImageButton = findViewById(R.id.backImageButton)
        backImageButton.setOnClickListener {
            finish()
        }

        cancelarCardView = findViewById(R.id.cancelarCardView)
        cancelarCardView.setOnClickListener {
            finish()
        }

        titleCustomEditText = findViewById(R.id.titleCustomEditText)
        descripcionCustomEditText = findViewById(R.id.descripcionCustomEditText)
        enviarValidCardView = findViewById(R.id.enviarValidCardView)
        enviarValidCardView.setOnClickListener {
            createIncidence()
        }

        getGroup()
    }

    fun createIncidence() {
        progressBar.visibility = View.VISIBLE

        val jsonBodyPost = JSONObject()
        jsonBodyPost.put("GroupId", group.groupId)
        jsonBodyPost.put("Description",titleCustomEditText.text)

        AndroidNetworking.post(RumiApi.incidencesUrl)
            .addHeaders("Authorization", "Bearer ${UserDefaults.token}")
            .addHeaders("Content-Type", "application/json")
            .addJSONObjectBody(jsonBodyPost)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(
                Task::class.java,
                object: ParsedRequestListener<Task> {
                    override fun onResponse(response: Task?) {
                        if (response != null) {
                            showDialogPositiveFinal("Mensaje", "Incidencia creada correctamente.")
                        }
                        else{
                            showDialogPositive("Mensaje", "Error al crear la incidencia")
                        }
                    }
                    override fun onError(anError: ANError?) {
                        progressBar.visibility = View.GONE

                        anError?.apply {
                            if (errorCode == 0){
                                showDialogPositive("Error","Error de conexión.")
                            }
                        }
                    }
                })
    }

    private fun getGroup() {
        progressBar.visibility = View.VISIBLE
        enviarValidCardView.visibility = View.GONE

        AndroidNetworking.get(RumiApi.groupsUrl)
            .addHeaders("Authorization", "Bearer ${UserDefaults.token}")
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.HIGH)
            .setTag(getString(R.string.app_name))
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    var groups = Group.from(response)
                    if (groups.isNotEmpty()){
                        group = groups[0]
                    }
                    progressBar.visibility = View.GONE
                    enviarValidCardView.visibility = View.VISIBLE
                }
                override fun onError(anError: ANError) {
                    anError?.apply { Toast.makeText(this@CreateIncidenceActivity, "Error al cargar los grupos", Toast.LENGTH_LONG).show() }
                    progressBar.visibility = View.GONE
                    enviarValidCardView.visibility = View.GONE
                }
            })
    }

    fun showDialogPositive(titulo: String?, detalle: String?) {
        val builder = AlertDialog.Builder(this@CreateIncidenceActivity)
        builder.setTitle(titulo)
        builder.setMessage(detalle)
        builder.setPositiveButton("Ok"){dialog, which ->}
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showDialogPositiveFinal(titulo: String?, detalle: String?) {
        val builder = AlertDialog.Builder(this@CreateIncidenceActivity)
        builder.setTitle(titulo)
        builder.setMessage(detalle)
        builder.setPositiveButton("Ok"){dialog, which -> finish()}
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
