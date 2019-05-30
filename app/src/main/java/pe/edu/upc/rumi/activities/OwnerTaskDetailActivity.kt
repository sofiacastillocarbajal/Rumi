package pe.edu.upc.rumi.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.androidnetworking.widget.ANImageView
import com.libizo.CustomEditText
import pe.edu.upc.rumi.R
import pe.edu.upc.rumi.model.Task

class OwnerTaskDetailActivity : AppCompatActivity() {
    private lateinit var backImageButton: AppCompatImageButton
    private lateinit var progressBar: ProgressBar

    private lateinit var userImageView: ANImageView
    private lateinit var userTextView: TextView
    private lateinit var titleTaskTextView: TextView
    private lateinit var statusCustomEditText: CustomEditText

    private var task: Task = Task()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_task_detail)

        supportActionBar?.hide()

        progressBar = findViewById(R.id.progressBar)
        backImageButton = findViewById(R.id.backImageButton)
        backImageButton.setOnClickListener {
            finish()
        }

        userImageView = findViewById(R.id.userImageView)
        userTextView = findViewById(R.id.userTextView)
        titleTaskTextView = findViewById(R.id.titleTaskTextView)
        statusCustomEditText = findViewById(R.id.statusCustomEditText)

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
            statusCustomEditText.setText(task.status)
            statusCustomEditText.isEnabled = false
        }
    }
}
