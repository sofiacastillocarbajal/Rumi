package pe.edu.upc.rumi.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.CardView
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import org.json.JSONObject
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.model.LoginResponse
import pe.edu.upc.rumi.model.RegisterResponse
import pe.edu.upc.rumi.network.RumiApi
import pe.edu.upc.rumi.util.UserDefaults

class RegisterRoomerActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var apellidosEditText: EditText
    private lateinit var generoEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var termsAppCompatCheckBox: AppCompatCheckBox
    private lateinit var registrarAhoraCardView: CardView

    private lateinit var progressBar: ProgressBar

    val TAG = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_roomer)
        supportActionBar?.hide()

        nombreEditText = findViewById(R.id.nombreEditText)
        apellidosEditText = findViewById(R.id.apellidosEditText)
        generoEditText = findViewById(R.id.generoEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        termsAppCompatCheckBox = findViewById(R.id.termsAppCompatCheckBox)
        progressBar = findViewById(R.id.progressBar)

        registrarAhoraCardView = findViewById(R.id.registrarAhoraCardView)
        registrarAhoraCardView.setOnClickListener {
            register()
        }
    }

    fun register() {
        if (nombreEditText.text.toString() == "" ||
            apellidosEditText.text.toString() == "" ||
            generoEditText.text.toString() == "" ||
            emailEditText.text.toString() == "" ||
            passwordEditText.text.toString() == "" ){
            showDialogPositive("Error", "No deje ningun campo vacio")
        }
        else {
            if (termsAppCompatCheckBox.isChecked){
                registerRequest()
            }
            else{
                showDialogPositive("Error", "Acepte los terminos y condiciones para continuar")
            }
        }
    }

    fun registerRequest() {
        registrarAhoraCardView.isEnabled = false
        progressBar.visibility = View.VISIBLE

        val loginPost = JSONObject()
        loginPost.put("Name", nombreEditText.text)
        loginPost.put("Surname",apellidosEditText.text)
        loginPost.put("Gender",generoEditText.text)
        loginPost.put("Position","ROOMER")
        loginPost.put("Email",emailEditText.text)
        loginPost.put("Password",passwordEditText.text)

        AndroidNetworking.post(RumiApi.signUp)
            .addHeaders("Content-Type", "application/json")
            .addJSONObjectBody(loginPost)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(
                RegisterResponse::class.java,
                object: ParsedRequestListener<RegisterResponse> {
                    override fun onResponse(response: RegisterResponse?) {
                        response?.apply {
                            response.apply {
                                if (success == "true"){
                                    loginRequest()
                                }
                                else {
                                    showDialogPositive("Error","Error al realizar el registro.")
                                }
                            }
                        }
                    }
                    override fun onError(anError: ANError?) {
                        registrarAhoraCardView.isEnabled = true
                        progressBar.visibility = View.GONE

                        anError?.apply {
                            if (errorCode == 0){
                                showDialogPositive("Error","Error de conexión.")
                            }
                            else {
                                showDialogPositive("Error","Error al realizar el registro.")
                            }
                        }
                    }
                })
    }

    private fun loginRequest() {
        val loginPost = JSONObject()
        loginPost.put("email", emailEditText.text)
        loginPost.put("password",passwordEditText.text)

        AndroidNetworking.post(RumiApi.signInUrl)
            .addHeaders("Content-Type", "application/json")
            .addJSONObjectBody(loginPost)
            .setPriority(Priority.HIGH)
            .setTag(TAG)
            .build()
            .getAsObject(
                LoginResponse::class.java,
                object: ParsedRequestListener<LoginResponse> {
                    override fun onResponse(response: LoginResponse?) {
                        response?.apply {
                            UserDefaults.name = profile.name
                            UserDefaults.surname = profile.surname
                            UserDefaults.phone = profile.phone
                            UserDefaults.gender = profile.gender
                            UserDefaults.birthdate = profile.birthdate
                            UserDefaults.image = profile.image
                            UserDefaults.description = profile.description
                            UserDefaults.occupation = profile.occupation
                            UserDefaults.skills = profile.skillsToString()
                            UserDefaults.score = profile.score
                            UserDefaults.role = role
                            UserDefaults.token = token
                            UserDefaults.saveInSharedPreferences(this@RegisterRoomerActivity)

                            if (UserDefaults.token != "") goToMainRoomer()
                        }
                    }
                    override fun onError(anError: ANError?) {
                        registrarAhoraCardView.isEnabled = true
                        progressBar.visibility = View.GONE

                        anError?.apply {
                            if (errorCode == 400){
                                showDialogPositive("Error","Error de credenciales, usuario o contraseña invalidos.")
                            }
                            else if (errorCode == 0){
                                showDialogPositive("Error","Error de conexión.")
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

    fun goToMainRoomer() {
        val intent = Intent(this, MainRoomerActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}
