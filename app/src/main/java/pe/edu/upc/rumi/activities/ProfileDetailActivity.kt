package pe.edu.upc.rumi.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.CardView
import android.view.View
import android.widget.TextView
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.model.Profile
import pe.edu.upc.rumi.util.UserDefaults

class ProfileDetailActivity : AppCompatActivity() {

    private lateinit var backImageButton: AppCompatImageButton
    private lateinit var contactCardView: CardView
    private lateinit var cerrarSesionImageButton: AppCompatImageButton

    private lateinit var nameTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var ocupacionValueTextView: TextView
    private lateinit var birthdateValueTextView: TextView

    private lateinit var editSkillsImageButton: AppCompatImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_detail)
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

        editSkillsImageButton = findViewById(R.id.editSkillsImageButton)
        editSkillsImageButton.setOnClickListener {

        }

        contactCardView = findViewById(R.id.contactCardView)
        contactCardView.setOnClickListener {
            contactCardView.visibility = View.GONE
        }

        cerrarSesionImageButton = findViewById(R.id.cerrarSesionImageButton)
        cerrarSesionImageButton.setOnClickListener {
            showDialogSimple("Mensaje", "¿Seguro que desea cerrar sesión?")
        }
        if (UserDefaults.role == "ROOMER") {
            cerrarSesionImageButton.visibility = View.VISIBLE
        }

        getParticipantData()
    }

    private fun getParticipantData() {
        intent?.extras?.apply {
            val profile = getSerializable("profile") as Profile
            nameTextView.apply { text = profile.name }
            phoneTextView.apply {
                if (profile.phone == "null")
                    text = "Sin teléfono"
                else
                    text = profile.phone
            }
            addressTextView.apply { text = "Lima, Perú" }
            descriptionTextView.apply {
                if (profile.description == "null")
                    text = "Sin descripción"
                else
                    text = profile.description
            }
            ocupacionValueTextView.apply {
                if (profile.skills?.size == 0)
                    text = "Sin cualidades asignadas"
                else {
                    var skills_s = ""
                    for (skill in profile.skills!!){
                        skills_s += "$skill | "
                    }
                    skills_s = skills_s.substring(0, skills_s.length - 3)
                    text = skills_s
                }
            }
            birthdateValueTextView.apply {
                if (profile.birthdate == "null" || profile.birthdate == "" || profile.birthdate == null)
                    text = "Sin cumpleaños colocado"
                else
                    text = profile.birthdate
            }
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
