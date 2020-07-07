package id.smkcoding.teamalvan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import id.smkcoding.teamalvan.model.ConsultationRepliesModel
import id.smkcoding.teamalvan.notification.FirebaseServices
import id.smkcoding.teamalvan.notification.NotificationData
import id.smkcoding.teamalvan.notification.PushNotification
import id.smkcoding.teamalvan.notification.RetrofitInstance
import kotlinx.android.synthetic.main.activity_reply_consultation_activty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReplyConsultationActivty : AppCompatActivity() {

    private var idReply: String? = ""
    private var idUser: String? = ""
    private var descritpion: String? = ""
    private var dateTime: String? = ""
    private var tokenUser: String? = ""
    private var keyParent: String? = ""

    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    private lateinit var consultationRepliesList: MutableList<ConsultationRepliesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply_consultation_activty)
        initialize()
    }

    private fun initialize() {
        idReply = intent.getStringExtra("key")
        idUser = intent.getStringExtra("user")
        descritpion = intent.getStringExtra("description")
        dateTime = intent.getStringExtra("time")
        tokenUser = intent.getStringExtra("token")
        keyParent = intent.getStringExtra("key_parent")

        tv_deskripsi_konsultasi_reply.text = descritpion
        tv_nama_konsultasi_reply.text = idUser
        tv_timestamp_konsultasi_reply.text = dateTime

        FirebaseServices.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseServices.token = it.token
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        balas_reply.setOnClickListener { replyDoctor() }

        displayDataReplies()
    }

    private fun replyDoctor() {
        val balasan = edt_replies_consultation_reply.text.toString().trim()
        val title = "Balasan baru"
        if(balasan.isEmpty()) {
            edt_replies_consultation_reply.error = "Mohon isi terlebih dahulu"
            return
        }
        if(balasan.isEmpty()) {
            edt_replies_consultation_reply.error = "Mohon isi terlebih dahulu"
            return
        } else {
            if(tokenUser == null && tokenUser == "") {
                PushNotification(
                    NotificationData(title, balasan),
                    TOPIC)
                .also {
                    sendNotification(it)
                    addReply()
                }
            } else {
                PushNotification(
                    NotificationData(title, balasan),
                    tokenUser.toString())
                    .also {
                        sendNotification(it)
                        addReply()
                    }
            }
        }
    }

    private fun addReply() {
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val current = SimpleDateFormat("yyyy-MM-dd")
        val tanggal = current.format(Date())
        val user = mAuth!!.currentUser
        mDatabaseReference = mDatabase!!.reference.child(user!!.uid)
            .child("tb_consultation")
            .child(keyParent!!)
            .child("tb_replies")
            .child(idReply!!)
            .child("tb_users_replies").push()
        val mRef = mDatabaseReference
        mRef!!.child("idbalasan").setValue(mRef.key.toString())
        mRef.child("iduser").setValue(idUser)
        mRef.child("text").setValue(edt_replies_consultation_reply.text.toString().trim())
        mRef.child("time").setValue(tanggal)
        mRef.child("token").setValue(tokenUser)
        val balasan = edt_replies_consultation_reply.text.toString().trim()
        val bundle = Bundle()
        bundle.putString("description", tv_deskripsi_konsultasi_reply.text.toString())
        bundle.putString("user", tv_nama_konsultasi_reply.text.toString())
        bundle.putString("time", tv_timestamp_konsultasi_reply.text.toString())
        bundle.putString("token", tokenUser)
        bundle.putString("key", idReply)
        bundle.putString("key_parent", keyParent)
        val intent = Intent(this, ReplyConsultationActivty::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                //
            } else {
                //
            }
        } catch (e: Exception) {
            //
        }
    }

    private fun displayDataReplies() {
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        mDatabaseReference = mDatabase!!.reference.child(user!!.uid)
            .child("tb_consultation")
            .child(keyParent!!)
            .child("tb_replies")
            .child(idReply!!)
            .child("tb_users_replies")
        mDatabaseReference!!.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                consultationRepliesList = ArrayList<ConsultationRepliesModel>()
                if(snapshot.exists()) {
                    for(data in snapshot.children) {
                        val reply = data.getValue(ConsultationRepliesModel::class.java)
                        consultationRepliesList.add(reply!!)
                    }
                    list_replies_reply.layoutManager = LinearLayoutManager(this@ReplyConsultationActivty)
                    list_replies_reply.adapter = ConsultationRepliesLastAdapter(this@ReplyConsultationActivty, consultationRepliesList)
                }
            }

        })
    }
}