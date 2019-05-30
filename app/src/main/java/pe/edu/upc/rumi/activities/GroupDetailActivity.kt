package pe.edu.upc.rumi.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.adapters.ParticipantsAdapter
import pe.edu.upc.rumi.model.Group
import pe.edu.upc.rumi.model.Profile
import java.io.Serializable

class GroupDetailActivity : AppCompatActivity() {
    private lateinit var participantsRecyclerView: RecyclerView
    private lateinit var backImageButton: AppCompatImageButton
    private lateinit var crearTareaCardView: CardView
    private var participants: List<Profile> = ArrayList()
    private var participantsAdapter = ParticipantsAdapter(participants)

        private var group: Group = Group()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail)
        supportActionBar?.hide()

        crearTareaCardView = findViewById(R.id.crearTareaCardView)
        crearTareaCardView.setOnClickListener {
            val intent = Intent(this@GroupDetailActivity, CreateTaskActivity::class.java)
            intent.putExtra("group", group as Serializable)
            this@GroupDetailActivity.startActivity(intent)
        }

        backImageButton = findViewById(R.id.backImageButton)
        backImageButton.setOnClickListener {
            finish()
        }

        participantsRecyclerView = findViewById(R.id.participantsRecyclerView)
        participantsRecyclerView.apply {
            adapter = participantsAdapter
            this.layoutManager = GridLayoutManager(this@GroupDetailActivity, 2)
        }
        getParticipants()
    }

    override fun onResume() {
        super.onResume()
        participantsAdapter.verifyItemChanged()
    }

    private fun getParticipants() {
        intent?.extras?.apply {
            group = getSerializable("group") as Group
            participantsAdapter.apply {
                participants = group.participants!!
                notifyDataSetChanged()
            }
        }
    }
}
