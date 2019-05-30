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
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import org.json.JSONArray
import org.json.JSONObject
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.model.Group
import pe.edu.upc.rumi.network.RumiApi
import pe.edu.upc.rumi.util.UserDefaults

class AddGroupActivity : AppCompatActivity() {
    private lateinit var backImageButton: AppCompatImageButton
    private lateinit var progressBar: ProgressBar

    private lateinit var groupNameCustomEditText: EditText
    private lateinit var membersCustomEditText: EditText
    private lateinit var addMemberCardView: CardView
    private lateinit var crearGrupoValidCardView: CardView

    private var participantsId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)
        supportActionBar?.hide()

        groupNameCustomEditText = findViewById(R.id.groupNameCustomEditText)
        membersCustomEditText = findViewById(R.id.membersCustomEditText)
        addMemberCardView = findViewById(R.id.addMemberCardView)
        addMemberCardView.setOnClickListener {
            showAddMemberDialog()
        }
        crearGrupoValidCardView = findViewById(R.id.crearGrupoValidCardView)
        crearGrupoValidCardView.setOnClickListener {
            createGroup()
        }
        progressBar = findViewById(R.id.progressBar)
        backImageButton = findViewById(R.id.backImageButton)
        backImageButton.setOnClickListener {
            finish()
        }
    }

    fun createGroup() {
        if (groupNameCustomEditText.text.toString() == "" ||
            membersCustomEditText.text.toString() == "") {
            showDialogPositive("Error", "Ingrese un nombre para el grupo y al menos un integrante")
        }
        else{
            createGroupRequest()
        }
    }

    fun createGroupRequest() {
        progressBar.visibility = View.VISIBLE

        val jsonBodyPost = JSONObject()
        jsonBodyPost.put("Name", groupNameCustomEditText.text)
        val jsonArrayParticipants = JSONArray()
        val participants = participantsId.split(",")
        for (p in participants){
            if (p != "") jsonArrayParticipants.put(p)
        }
        jsonBodyPost.put("Participants", jsonArrayParticipants)

        AndroidNetworking.post(RumiApi.groupsUrl)
            .addHeaders("Authorization", "Bearer ${UserDefaults.token}")
            .addHeaders("Content-Type", "application/json")
            .addJSONObjectBody(jsonBodyPost)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(
                Group::class.java,
                object: ParsedRequestListener<Group> {
                    override fun onResponse(response: Group?) {
                        response?.apply {
                            if (groupId != ""){
                                Toast.makeText(this@AddGroupActivity, "Grupo creado correctamente", Toast.LENGTH_LONG).show()
                                finish()
                            }
                            else{
                                showDialogPositive("Error","Error de al crear el grupo.")
                            }
                        }
                    }
                    override fun onError(anError: ANError?) {
                        progressBar.visibility = View.GONE

                        anError?.apply {
                            if (errorCode == 0){
                                showDialogPositive("Error","Error de conexión.")
                            }
                            else if (errorCode == 500){
                                showDialogPositiveFinal("Error","No puedes crear mas grupos, actualiza tu plan.")
                            }
                        }
                    }
                })
    }

    fun showAddMemberDialog() {
        //valido que solo hayan 10 miembros
        val participants = participantsId.split(",")
        if (participants.size - 1 < 10) {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_add_member, null)
            val categoryEditText = view.findViewById(R.id.emailEditText) as EditText

            builder.setTitle("Agregar integrante")
            builder.setView(view)
            builder.setCancelable(false)
            builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
                val email = categoryEditText.text.toString()
                if (email == "") showDialogPositive("Error","Campo de correo vacio.")
                else validIfUsernameEmailExists(email)
            }
            builder.setNegativeButton(android.R.string.cancel) { dialog, p1 -> dialog.cancel() }
            builder.show()
        }
        else {
            showDialogPositiveFinal("Error","solo puede agregar 10 integrantes por grupo, actualiza tu plan.")
        }
    }

    fun validIfUsernameEmailExists(email: String) {
        progressBar.visibility = View.VISIBLE

        val jsonBodyPost = JSONObject()
        jsonBodyPost.put("Email", email)

        AndroidNetworking.post(RumiApi.validUsernameUrl)
            .addHeaders("Authorization", "Bearer ${UserDefaults.token}")
            .addHeaders("Content-Type", "application/json")
            .addJSONObjectBody(jsonBodyPost)
            .setPriority(Priority.HIGH)
            .build()
            .getAsString(
                object: StringRequestListener {
                    override fun onResponse(response: String?) {
                        if (response != ""){
                            participantsId += "$response,"
                            membersCustomEditText.text.append("$email,")
                        }
                        else {
                            showDialogPositive("Error","Usuario invalido")
                        }

                        progressBar.visibility = View.GONE
                    }
                    override fun onError(anError: ANError?) {
                        progressBar.visibility = View.GONE

                        anError?.apply {
                            if (errorCode == 0){
                                showDialogPositive("Error","Error de conexión.")
                            }
                            else {
                                showDialogPositive("Error","Usuario invalido.")
                            }
                        }
                    }
                })
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
