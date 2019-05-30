package pe.edu.upc.rumi.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.CardView
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import org.json.JSONObject
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.model.Group
import pe.edu.upc.rumi.model.Task
import pe.edu.upc.rumi.network.RumiApi
import pe.edu.upc.rumi.util.Functions
import pe.edu.upc.rumi.util.UserDefaults

class CreateTaskActivity : AppCompatActivity() {
    private lateinit var backImageButton: AppCompatImageButton
    private lateinit var progressBar: ProgressBar

    private lateinit var cancelarCardView: CardView

    private lateinit var titleCustomEditText: EditText
    private lateinit var descripcionCustomEditText: EditText
    private lateinit var enviarValidCardView: CardView

    private var group: Group = Group()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        supportActionBar?.hide()

        group = getGroup()

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
            createTask()
        }
    }

    fun createTask() {
        progressBar.visibility = View.VISIBLE

        val jsonBodyPost = JSONObject()
        jsonBodyPost.put("GroupId", group.groupId)
        jsonBodyPost.put("Title", titleCustomEditText.text)
        jsonBodyPost.put("StartAt", Functions.DatetoStringForApi(Functions.getCurrentDateTime()))
        jsonBodyPost.put("Description",descripcionCustomEditText.text)

        AndroidNetworking.post(RumiApi.tasksUrl)
            .addHeaders("Authorization", "Bearer ${UserDefaults.token}")
            .addHeaders("Content-Type", "application/json")
            .addJSONObjectBody(jsonBodyPost)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(
                Task::class.java,
                object: ParsedRequestListener<Task> {
                    override fun onResponse(response: Task?) {
                        response?.apply {
                            response.apply {
                                if (taskId != "") {
                                    showDialogPositiveFinal("Mensaje", "Tarea creada correctamente.")
                                }
                                else{
                                    showDialogPositive("Mensaje", "Error al crear la tarea")
                                }
                            }
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

    private fun getGroup(): Group {
        intent?.extras?.apply {
            return getSerializable("group") as Group
        }
    }

    fun showDialogPositive(titulo: String?, detalle: String?) {
        val builder = AlertDialog.Builder(this@CreateTaskActivity)
        builder.setTitle(titulo)
        builder.setMessage(detalle)
        builder.setPositiveButton("Ok"){dialog, which ->}
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showDialogPositiveFinal(titulo: String?, detalle: String?) {
        val builder = AlertDialog.Builder(this@CreateTaskActivity)
        builder.setTitle(titulo)
        builder.setMessage(detalle)
        builder.setPositiveButton("Ok"){dialog, which -> finish()}
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}





















