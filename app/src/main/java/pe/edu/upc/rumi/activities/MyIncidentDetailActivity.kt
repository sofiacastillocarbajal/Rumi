package pe.edu.upc.rumi.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.androidnetworking.widget.ANImageView
import com.libizo.CustomEditText
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.model.Incident

class MyIncidentDetailActivity : AppCompatActivity() {

    private lateinit var backImageButton: AppCompatImageButton
    private lateinit var progressBar: ProgressBar

    private lateinit var userImageView: ANImageView
    private lateinit var userTextView: TextView
    private lateinit var incidentDescriptionTextView: TextView
    private lateinit var resolvedAppCompatCheckBox: AppCompatCheckBox
    private lateinit var responseCustomEditText: CustomEditText

    private var incident: Incident = Incident()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_incident_detail)
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
            resolvedAppCompatCheckBox.isEnabled = false
            responseCustomEditText.setText(incident.response)
        }
    }

    fun showDialogPositive(titulo: String?, detalle: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(detalle)
        builder.setPositiveButton("Ok"){dialog, which ->}
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showDialogPositiveFinal(titulo: String?, detalle: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(detalle)
        builder.setPositiveButton("Ok"){dialog, which -> finish()}
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
