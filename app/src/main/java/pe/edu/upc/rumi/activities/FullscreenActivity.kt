package pe.edu.upc.rumi.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.CardView
import android.view.View
import kotlinx.android.synthetic.main.activity_fullscreen.*
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.util.UserDefaults

class FullscreenActivity : AppCompatActivity() {
    private lateinit var propietarioValidCardView:CardView
    private lateinit var roomateCardView:CardView

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        supportActionBar?.show()
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        validarSesion()
        propietarioValidCardView = findViewById(R.id.propietarioValidCardView)
        propietarioValidCardView.setOnClickListener {
            val intent = Intent(this@FullscreenActivity, LoginPropietarioActivity::class.java)
            startActivity(intent)
        }

        roomateCardView = findViewById(R.id.roomateCardView)
        roomateCardView.setOnClickListener {
            val intent = Intent(this@FullscreenActivity, LoginRoomerActivity::class.java)
            startActivity(intent)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mVisible = true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(300)
    }

    private fun hide() {
        supportActionBar?.hide()
        mVisible = false
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        private val UI_ANIMATION_DELAY = 300
    }

    fun validarSesion() {
        UserDefaults.loadFromSharedPreferences(this@FullscreenActivity)
        if (UserDefaults.token != "") {
            if (UserDefaults.role == "OWNER"){
                goToMainPropietario()
            }
            else if (UserDefaults.role == "ROOMER"){
                goToMainRoomer()
            }
        }
    }

    fun goToMainPropietario() {
        val intent = Intent(this@FullscreenActivity, MainPropietarioActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
    fun goToMainRoomer() {
        val intent = Intent(this@FullscreenActivity, MainRoomerActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}
