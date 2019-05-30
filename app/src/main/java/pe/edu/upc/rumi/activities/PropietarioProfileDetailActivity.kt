package pe.edu.upc.rumi.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatImageButton
import android.widget.TextView
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.util.UserDefaults

class PropietarioProfileDetailActivity : AppCompatActivity() {

    private lateinit var backImageButton: AppCompatImageButton
    private lateinit var cerrarSesionImageButton: AppCompatImageButton

    private lateinit var nameTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var ocupacionValueTextView: TextView
    private lateinit var birthdateValueTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_propietario_profile_detail)

        supportActionBar?.hide()

        nameTextView = findViewById(R.id.nameTextView)
        phoneTextView = findViewById(R.id.phoneTextView)
        addressTextView = findViewById(R.id.addressTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        ocupacionValueTextView = findViewById(R.id.ocupacionValueTextView)
        birthdateValueTextView = findViewById(R.id.birthdateValueTextView)
        backImageButton = findViewById(R.id.backImageButton)
        backImageButton.setOnClickListener {
            finish()
        }

        cerrarSesionImageButton = findViewById(R.id.cerrarSesionImageButton)
        cerrarSesionImageButton.setOnClickListener {
            showDialogSimple("Mensaje", "¿Seguro que desea cerrar sesión?")
        }

        getParticipantData()
    }

    private fun getParticipantData() {
        nameTextView.apply { text = UserDefaults.name }
        phoneTextView.apply {
            if (UserDefaults.phone == "null" || UserDefaults.phone == ""  || UserDefaults.phone == null)
                text = "Sin teléfono"
            else
                text = UserDefaults.phone
        }
        addressTextView.apply { text = "Lima, Perú" }
        descriptionTextView.apply {
            if (UserDefaults.description == "null" || UserDefaults.description == "" || UserDefaults.description == null)
                text = "Sin descripción"
            else
                text = UserDefaults.description
        }
        ocupacionValueTextView.apply {
            if (UserDefaults.occupation == "null" || UserDefaults.occupation == "" || UserDefaults.occupation == null)
                text = "Sin ocupación"
            else
                text = UserDefaults.occupation
        }
        birthdateValueTextView.apply {
            if (UserDefaults.birthdate == "null" || UserDefaults.birthdate == "" || UserDefaults.birthdate == null)
                text = "Sin cumpleaños colocado"
            else
                text = UserDefaults.birthdate
        }
    }

    fun showDialogSimple(titulo: String?, detalle: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(detalle)
        builder.setPositiveButton("Ok"){dialog, which ->
            UserDefaults.clearFromSharedPreferences(this)
            val intent = Intent(this, FullscreenActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        builder.setNegativeButton("Cancel"){dialog, which ->}
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
