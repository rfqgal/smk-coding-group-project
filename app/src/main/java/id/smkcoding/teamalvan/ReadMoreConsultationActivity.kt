package id.smkcoding.teamalvan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_read_more_articles.*
import kotlinx.android.synthetic.main.activity_read_more_consultation.*

class ReadMoreConsultationActivity : AppCompatActivity() {

    private var description: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_more_consultation)
        initialize()
    }

    private fun initialize() {
        description = intent.getStringExtra("description").toString()
        tv_text_consultation_more.text = description
    }
}