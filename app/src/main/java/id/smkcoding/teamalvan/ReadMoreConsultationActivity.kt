package id.smkcoding.teamalvan

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import id.smkcoding.teamalvan.notification.FirebaseServices
import id.smkcoding.teamalvan.notification.NotificationData
import id.smkcoding.teamalvan.notification.PushNotification
import id.smkcoding.teamalvan.notification.RetrofitInstance
import kotlinx.android.synthetic.main.activity_read_more_consultation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ReadMoreConsultationActivity : AppCompatActivity() {

    private val TAG: String? = "ReadMoreConsultation"
    private var nama: String? = ""
    private var time: String? = ""
    private var description: String? = ""
    private var tokenUser: String? = ""
    private lateinit var balasan: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_more_consultation)
        initialize()
    }

    private fun initialize() {
        nama = intent.getStringExtra("user")
        time = intent.getStringExtra("time")
        description = intent.getStringExtra("description")
        tokenUser = intent.getStringExtra("token")

        balasan = findViewById<EditText>(R.id.replies_consultation)

        tv_nama_konsultasi.text = nama
        tv_timestamp_konsultasi.text = time
        tv_deskripsi_konsultasi.text = description
        tv_token_konsultasi.text = tokenUser

        FirebaseServices.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseServices.token = it.token
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        balas.setOnClickListener { replies() }

    }

    private fun replies() {
        val getBalasan = balasan.text.toString().trim()
        val titleNotif = "Balasan baru"
        if(getBalasan.isEmpty()) {
            replies_consultation.error = "Mohon isi terlebih dahulu"
            return
        } else {
            if (tokenUser == null && tokenUser == "") {
                PushNotification(
                    NotificationData(titleNotif, getBalasan),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            } else {
                PushNotification(
                    NotificationData(titleNotif, getBalasan),
                    tokenUser.toString()
                ).also {
                    sendNotification(it)
                }
            }
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
//                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
//                Log.e(TAG, response.errorBody().toString())
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}