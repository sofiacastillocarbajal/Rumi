package pe.edu.upc.rumi.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.widget.ANImageView
import com.libizo.CustomEditText
import org.json.JSONObject
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.model.Incident
import pe.edu.upc.rumi.network.RumiApi
import pe.edu.upc.rumi.util.UserDefaults

class IncidenceDetailActivity : AppCompatActivity() {
    private lateinit var backImageButton: AppCompatImageButton
    private lateinit var progressBar: ProgressBar

    private lateinit var userImageView: ANImageView
    private lateinit var userTextView: TextView
    private lateinit var incidentDescriptionTextView: TextView
    private lateinit var resolvedAppCompatCheckBox: AppCompatCheckBox
    private lateinit var responseCustomEditText: CustomEditText
    private lateinit var updateCardView: CardView

    private var incident: Incident = Incident()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incidence_detail)
        supportActionBar?.hide()

        progressBar = findViewById(R.id.progressBar)
        backImageButton = findViewById(R.id.backImageButton)
        backImageButton.setOnClickListener {
            finish()
        }

        userImageView = findViewById(R.id.userImageView)
        userTextView = findViewById(R.id.userTextView)
        incidentDescriptionTextView = findViewById(R.id.incidentDescriptionTextView)
        resolvedAppCompatCheckBox = findViewById(R.id.resolvedAppCompatCheckBox)
        responseCustomEditText = findViewById(R.id.responseCustomEditText)
        updateCardView = findViewById(R.id.updateCardView)
        updateCardView.setOnClickListener {
            updateIncident()
        }

        setIncidentDetails()
    }

    private fun setIncidentDetails() {
        intent?.extras?.apply {
            incident = getSerializable("incident") as Incident

            userImageView.apply {
                setDefaultImageResId(R.drawable.ic_account_circle_primary)
                setErrorImageResId(R.drawable.ic_account_circle_primary)
            }
            userTextView.text = incident.profile?.name
            incidentDescriptionTextView.text = incident.description
            resolvedAppCompatCheckBox.isChecked = (incident.resolved == "true")
            responseCustomEditText.setText(incident.response)
        }
    }

    fun updateIncident() {
        progressBar.visibility = View.VISIBLE

        val jsonBody = JSONObject()
        jsonBody.put("GroupId", incident.groupId)
        jsonBody.put("Description", incident.description)
        jsonBody.put("Response", incident.response)
        jsonBody.put("Resolved", resolvedAppCompatCheckBox.isChecked.toString())

        AndroidNetworking.patch(RumiApi.incidencesById(incident.incidenceId))
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
                            if (response != null){
                                showDialogPositiveFinal("Mensaje", "Incidencia actualizada correctamente")
                            }
                            else{
                                showDialogPositive("Error","Error al actualizar la incidencia.")
                            }
                        }
                    }
                    override fun onError(anError: ANError?) {
                        progressBar.visibility = View.GONE

                        anError?.apply {
                            if (errorCode == 0){
                                showDialogPositive("Error","Error de conexión.")
                            }
                            else{
                                showDialogPositive("Error","Error al actualizar la incidencia.")
                            }
                        }
                    }
                })
    }

    fun showDialogPositive(titulo: String?, detalle: String?) {
//        val builder = AlertDialog.Builder(this@IncidenceDetailActivity)
//        builder.setTitle(titulo)
//        builder.setMessage(detalle)
//        builder.setPositiveButton("Ok"){dialog, which ->}
//        val dialog: AlertDialog = builder.create()
//        dialog.show()

        Toast.makeText(this, detalle, Toast.LENGTH_LONG).show()
    }

    fun showDialogPositiveFinal(titulo: String?, detalle: String?) {
//        val builder = AlertDialog.Builder(this@IncidenceDetailActivity)
//        builder.setTitle(titulo)
//        builder.setMessage(detalle)
//        builder.setPositiveButton("Ok"){dialog, which -> finish()}
//        val dialog: AlertDialog = builder.create()
//        dialog.show()

        Toast.makeText(this, detalle, Toast.LENGTH_LONG).show()
        finish()
    }
}




























