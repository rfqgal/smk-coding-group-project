package id.smkcoding.teamalvan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import id.smkcoding.teamalvan.model.ConsultationRepliesModel
import id.smkcoding.teamalvan.model.UsersModel
import id.smkcoding.teamalvan.notification.FirebaseServices
import id.smkcoding.teamalvan.notification.NotificationData
import id.smkcoding.teamalvan.notification.PushNotification
import id.smkcoding.teamalvan.notification.RetrofitInstance
import kotlinx.android.synthetic.main.activity_read_more_consultation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReadMoreConsultationActivity : AppCompatActivity() {

    private val TAG: String? = "ReadMoreConsultation"
    private var nama: String? = ""
    private var time: String? = ""
    private var description: String? = ""
    private var tokenUser: String? = ""
    private var key: String? = ""

    private lateinit var consultationRepliesList: MutableList<ConsultationRepliesModel>
    private lateinit var userConsultation: MutableList<UsersModel>

    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

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
        key = intent.getStringExtra("key")

        val user = FirebaseDatabase.getInstance().reference
        val qRef = user.child(nama.toString()).child("tb_users").limitToFirst(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    userConsultation = ArrayList<UsersModel>()
                    if(snapshot.exists()) {
                        for(data in snapshot.children) {
                            val user = data.getValue(UsersModel::class.java)
                            tv_nama_konsultasi.text = data.child("name").value.toString()
                            Glide.with(this@ReadMoreConsultationActivity)
                                .load(data.child("photo").value)
                                .into(img_konsultasi)
                            userConsultation.add(user!!)
                        }
                    }
                }
            })
        tv_timestamp_konsultasi.text = time
        tv_deskripsi_konsultasi.text = description

        FirebaseServices.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseServices.token = it.token
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        balas.setOnClickListener { replies() }

        displayReplies()

    }

    private fun replies() {
        val getBalasan = edt_replies_consultation.text.toString().trim()
        val titleNotif = "Balasan baru  "
        if(getBalasan.isEmpty()) {
            edt_replies_consultation.error = "Mohon isi terlebih dahulu"
            return
        } else {
            if (tokenUser == null && tokenUser == "") {
                PushNotification(
                    NotificationData(titleNotif, getBalasan),
                    TOPIC
                ).also {
                    sendNotification(it)
                    addReply()
                }
            } else {
                PushNotification(
                    NotificationData(titleNotif, getBalasan),
                    tokenUser.toString()
                ).also {
                    sendNotification(it)
                    addReply()
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

    private fun addReply() {
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser

        val current = SimpleDateFormat("yyyy-MM-dd")
        val tanggal = current.format(Date())
        mDatabaseReference = mDatabase!!.reference.child(user!!.uid)
            .child("tb_consultation")
            .child(key!!)
            .child("tb_replies").push()
        val qRef = mDatabaseReference
        qRef!!.child("idreply").setValue(qRef.key.toString())
        qRef.child("iduser").setValue(user.uid)
        qRef.child("text").setValue(edt_replies_consultation.text.toString())
        qRef.child("time").setValue(tanggal)
        qRef.child("token").setValue(tokenUser)
        val bundle = Bundle()
        bundle.putString("description", tv_deskripsi_konsultasi.text.toString())
        bundle.putString("user", tv_nama_konsultasi.text.toString())
        bundle.putString("time", tv_timestamp_konsultasi.text.toString())
        bundle.putString("token", tokenUser)
        bundle.putString("key", key)
        val intent = Intent(this, ReadMoreConsultationActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun displayReplies() {
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        mDatabaseReference = mDatabase!!.reference.child(user!!.uid).child("tb_consultation").child(key!!).child("tb_replies")
        mDatabaseReference!!.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                consultationRepliesList = ArrayList<ConsultationRepliesModel>()
                if(snapshot.exists()) {
                    for(data in snapshot.children) {
                        val reply = data.getValue(ConsultationRepliesModel::class.java)
                        consultationRepliesList.add(reply!!)
                    }
                    list_replies.layoutManager = LinearLayoutManager(this@ReadMoreConsultationActivity)
                    list_replies.adapter = ConsultationRepliesAdapter(this@ReadMoreConsultationActivity, consultationRepliesList, key.toString())
                }
            }
        })
    }
}