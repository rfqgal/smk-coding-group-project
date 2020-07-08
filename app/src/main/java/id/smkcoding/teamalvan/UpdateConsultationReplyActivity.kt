package id.smkcoding.teamalvan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import id.smkcoding.teamalvan.model.ConsultationRepliesModel
import id.smkcoding.teamalvan.notification.FirebaseServices
import id.smkcoding.teamalvan.notification.NotificationData
import id.smkcoding.teamalvan.notification.PushNotification
import id.smkcoding.teamalvan.notification.RetrofitInstance
import kotlinx.android.synthetic.main.activity_update_consultation_reply.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class UpdateConsultationReplyActivity : AppCompatActivity() {

    private var idreply: String? = ""
    private var iduser: String? = ""
    private var text: String? = ""
    private var time: String? = ""
    private var token: String? = ""
    private var keyParent: String? = ""

    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_consultation_reply)
        initialize()
    }

    private fun initialize() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference

        idreply = intent.getStringExtra("key")
        iduser = intent.getStringExtra("user")
        text = intent.getStringExtra("description")
        time = intent.getStringExtra("time")
        token = intent.getStringExtra("token")
        keyParent = intent.getStringExtra("key_parent")

        pertanyaan.text = text!!.toEditable()

        FirebaseServices.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseServices.token = it.token
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        simpan.setOnClickListener {
            updateData()
        }

        hapus.setOnClickListener {
            deleteData()
        }
    }

    private fun deleteData() {
        val builder = AlertDialog.Builder(this@UpdateConsultationReplyActivity)
        builder.setTitle("Yakin hapus balasan ?")
        builder.setMessage("Balasan akan dihapus secara permanen")
        builder.setIcon(R.drawable.ic_trash)
        builder.setPositiveButton("Delete") { dialog, which ->
            mDatabase = FirebaseDatabase.getInstance()
            val mRefConsul = mDatabase!!.reference.child(iduser.toString())
                .child("tb_consultation")
                .child(keyParent.toString())
                .child("tb_replies")
                .child(idreply.toString())
            mRefConsul.removeValue().addOnSuccessListener {
                val mRefUser = mDatabaseReference
                mRefUser!!.child(iduser.toString())
                    .child("tb_consultation")
                    .child(keyParent.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            //
                        }
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val bundle = Bundle()
                            bundle.putString("user" ,snapshot.child("iduser").value.toString())
                            bundle.putString("key", snapshot.child("idpertanyaan").value.toString())
                            bundle.putString("token", snapshot.child("token").value.toString())
                            bundle.putString("description", snapshot.child("text").value.toString())
                            bundle.putString("time", snapshot.child("time").value.toString())
                            val intent = Intent(this@UpdateConsultationReplyActivity, ReadMoreConsultationActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
                            val title = "Komentar dihapus"
                            val message = "Ada komentar yang dihapus"
                            PushNotification(
                                NotificationData(title, message),
                                token.toString()
                            ).also {
                                sendNotification(it)
                            }
                            finish()
                        }
                    })
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun updateData() {
        if(pertanyaan.text.isEmpty()) {
            pertanyaan.error = "Silahkan diisi dulu"
            return
        }
        mDatabaseReference!!.child(iduser.toString())
            .child("tb_consultation")
            .child(keyParent.toString())
            .child("tb_replies")
            .child(idreply.toString())
            .child("text").setValue(pertanyaan.text.toString()).addOnSuccessListener {
                val title = "Balasan komentar di edit"
                val message = pertanyaan.text.toString()
                PushNotification(
                    NotificationData(title, message),
                    token.toString()
                ).also {
                    sendNotification(it)
                }
            }
        val bundle = Bundle()
        mDatabaseReference!!.child(iduser.toString())
            .child("tb_consultation")
            .child(keyParent.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bundle = Bundle()
                    bundle.putString("user" ,snapshot.child("iduser").value.toString())
                    bundle.putString("key", snapshot.child("idpertanyaan").value.toString())
                    bundle.putString("token", snapshot.child("token").value.toString())
                    bundle.putString("description", snapshot.child("text").value.toString())
                    bundle.putString("time", snapshot.child("time").value.toString())
                    val intent = Intent(this@UpdateConsultationReplyActivity, ReadMoreConsultationActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }
            })
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
//            Log.e(TAG, e.toString())
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}