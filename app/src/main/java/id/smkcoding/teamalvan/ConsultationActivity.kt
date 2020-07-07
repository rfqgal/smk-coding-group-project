package id.smkcoding.teamalvan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import id.smkcoding.teamalvan.model.ConsultationModel
import id.smkcoding.teamalvan.notification.FirebaseServices
import id.smkcoding.teamalvan.notification.NotificationData
import id.smkcoding.teamalvan.notification.PushNotification
import id.smkcoding.teamalvan.notification.RetrofitInstance
import kotlinx.android.synthetic.main.activity_consultation.*
import kotlinx.android.synthetic.main.activity_consultation.etToken
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ConsultationActivity : AppCompatActivity(), View.OnClickListener{

    val TAG = "ConsultationActivity"
    lateinit var ref : DatabaseReference
    private var auth: FirebaseAuth? = null
    private lateinit var Pertanyaan : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultation)
        title = "Tanya Dokter"

        Pertanyaan = findViewById<EditText>(R.id.pertanyaan)

        auth = FirebaseAuth.getInstance() //Mendapakan Instance Firebase Auth
        ref = FirebaseDatabase.getInstance().getReference()
        FirebaseServices.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseServices.token = it.token
            etToken.setText(it.token)
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        simpan.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        save()
    }
    private fun save() {
        val getPertanyaan= Pertanyaan.text.toString().trim()
        val title = "Konsultasi Baru"
        val recipientToken = etToken.text.toString()
        if(getPertanyaan.isEmpty()){
            pertanyaan.error = "Mohon di Isi Terlebih Dahulu"
            return
        }

        val current = SimpleDateFormat("yyyy-MM-dd")
        val tanggal = current.format(Date())
        val pertanyaanid = ref.push().key.toString()
        val jenis = "pertanyaan"
        val UserID: String = auth?.getCurrentUser()?.getUid().toString()
        val question = ConsultationModel(pertanyaanid, UserID, jenis, getPertanyaan, tanggal, recipientToken)

        if (UserID != null){
            if(getPertanyaan.isNotEmpty() && recipientToken.isNotEmpty()) {
                PushNotification(
                    NotificationData(title, getPertanyaan),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            }
//            ref.child(UserID).child("tb_consultation").push().setValue(question).addOnCompleteListener{
//                Toast.makeText(this,"Pertanyaan Anda Terkirim", Toast.LENGTH_SHORT).show()
//                finish()
//            }
            val qRef = ref.child(UserID).child("tb_consultation").push()
            qRef.child("idpertanyaan").setValue(qRef.key.toString())
            qRef.child("iduser").setValue(UserID)
            qRef.child("jenis").setValue(jenis)
            qRef.child("text").setValue(getPertanyaan)
            qRef.child("time").setValue(tanggal)
            qRef.child("token").setValue(recipientToken)
            val intent = Intent(this@ConsultationActivity, ConsultationFragment::class.java)
            Toast.makeText(this,"Pertanyaan Anda Terkirim", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
//                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
//                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

}