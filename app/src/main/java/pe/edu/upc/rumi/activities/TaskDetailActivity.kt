package pe.edu.upc.rumi.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.CardView
import android.view.View
import android.widget.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.widget.ANImageView
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner
import com.libizo.CustomEditText
import org.json.JSONObject
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.model.Incident
import pe.edu.upc.rumi.model.Task
import pe.edu.upc.rumi.network.RumiApi
import pe.edu.upc.rumi.util.UserDefaults

class TaskDetailActivity : AppCompatActivity() {
    private lateinit var backImageButton: AppCompatImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var statusSpinner: Spinner

    private lateinit var userImageView: ANImageView
    private lateinit var userTextView: TextView
    private lateinit var titleTaskTextView: TextView
    private lateinit var descriptionCustomEditText: CustomEditText
    private lateinit var statusCustomEditText: CustomEditText
    private lateinit var updateCardView: CardView

    private var task: Task = Task()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        supportActionBar?.hide()
        progressBar = findViewById(R.id.progressBar)
        statusSpinner = findViewById(R.id.statusSpinner)
        backImageButton = findViewById(R.id.backImageButton)
        backImageButton.setOnClickListener {
            finish()
        }

        userImageView = findViewById(R.id.userImageView)
        userTextView = findViewById(R.id.userTextView)
        titleTaskTextView = findViewById(R.id.titleTaskTextView)
        descriptionCustomEditText = findViewById(R.id.descriptionCustomEditText)
        statusCustomEditText = findViewById(R.id.statusCustomEditText)
        updateCardView = findViewById(R.id.updateCardView)
        updateCardView.setOnClickListener {
            updateTask()
        }

        setTaskDetail()
    }

    private fun setTaskDetail() {
        intent?.extras?.apply {
            task = getSerializable("task") as Task

            userImageView.apply {
                setDefaultImageResId(R.drawable.ic_account_circle_primary)
                setErrorImageResId(R.drawable.ic_account_circle_primary)
            }
            userTextView.text = task.groupName
            titleTaskTextView.text = task.title
            descriptionCustomEditText.setText(task.description)
            statusCustomEditText.setText(task.status)
            statusSpinner.setSelection(getPosStatus())
        }
    }

    fun getPosStatus(): Int {
        if (task.status == "TODO")
            return 0
        else if (task.status == "DOING")
            return 1
        else //done
            return 2
    }

    fun getPosValue(): String {
        if (statusSpinner.selectedItemPosition == 0)
            return "TODO"
        else if (statusSpinner.selectedItemPosition == 1)
            return "DOING"
        else //done
            return "DONE"
    }

    fun updateTask() {
        progressBar.visibility = View.VISIBLE

        val jsonBody = JSONObject()
        jsonBody.put("Title", titleTaskTextView.text)
        jsonBody.put("Description", descriptionCustomEditText.text.toString())
        jsonBody.put("Status", getPosValue())

        AndroidNetworking.patch(RumiApi.tasksById(task.taskId))
            .addHeaders("Authorization", "Bearer ${UserDefaults.token}")
            .addHeaders("Content-Type", "application/json")
            .addJSONObjectBody(jsonBody)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(
                Incident::class.java,
                object: ParsedRequestListener<Incident> {
                    override fun onResponse(response: Incident?) {
                        response?.apply {
                            showDialogPositiveFinal("Mensaje", "Tarea actualizada correctamente")
                        }
                    }
                    override fun onError(anError: ANError?) {
                        progressBar.visibility = View.GONE

                        anError?.apply {
                            if (errorCode == 0){
                                showDialogPositive("Error","Error de conexión.")
                            }
                            else{
                                showDialogPositive("Error","Error al actualizar la tarea.")
                            }
                        }
                    }
                })
    }

    fun showDialogPositive(titulo: String?, detalle: String?) {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle(titulo)
//        builder.setMessage(detalle)
//        builder.setPositiveButton("Ok"){dialog, which ->}
//        val dialog: AlertDialog = builder.create()
//        dialog.show()

        Toast.makeText(this, detalle, Toast.LENGTH_LONG).show()
    }

    fun showDialogPositiveFinal(titulo: String?, detalle: String?) {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle(titulo)
//        builder.setMessage(detalle)
//        builder.setPositiveButton("Ok"){dialog, which -> finish()}
//        val dialog: AlertDialog = builder.create()
//        dialog.show()

        Toast.makeText(this, detalle, Toast.LENGTH_LONG).show()
        finish()
    }
}
