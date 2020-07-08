package id.smkcoding.teamalvan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.smkcoding.teamalvan.notification.NotificationData
import id.smkcoding.teamalvan.notification.PushNotification
import id.smkcoding.teamalvan.notification.RetrofitInstance
import kotlinx.android.synthetic.main.activity_update_consultation_reply_last.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class UpdateConsultationReplyLastActivity : AppCompatActivity() {

    private var idconsul: String? = ""
    private var idreply: String? = ""
    private var id: String? = ""
    private var text: String? = ""
    private var time: String? = ""
    private var user: String? = ""
    private var token: String? = ""
    private var mtextDescription: String? = ""

    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_consultation_reply_last)
        initialize()
    }

    private fun initialize() {
        idconsul = intent.getStringExtra("idconsul")
        idreply = intent.getStringExtra("idreply")
        id = intent.getStringExtra("id")
        time = intent.getStringExtra("time")
        user = intent.getStringExtra("user")
        text = intent.getStringExtra("description")
        mtextDescription = intent.getStringExtra("description")
        token = intent.getStringExtra("token")

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference
        mAuth = FirebaseAuth.getInstance()

        val mRef = mDatabaseReference!!.child(user!!)
            .child("tb_consultation")
            .child(idconsul!!)
            .child("tb_replies")
            .child(idreply!!)
            .child("tb_users_replies")
            .child(id!!)
        mRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val text = snapshot.child("text").value.toString().toEditable()
                pertanyaan.text = text
            }
        })

        simpan.setOnClickListener {
            updateData()
        }
        
        hapus.setOnClickListener { 
            confirmDelete()
        }
    }

    private fun confirmDelete() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Yakin hapus balasan ?")
        builder.setMessage("Balasan akan dihapus secara permanen")
        builder.setIcon(R.drawable.ic_trash)
        builder.setPositiveButton("Delete") { dialog, which ->
            deleteData()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun deleteData() {
        val mRef = mDatabaseReference!!.child(user!!)
            .child("tb_consultation")
            .child(idconsul!!)
            .child("tb_replies")
            .child(idreply!!)
            .child("tb_users_replies")
            .child(id!!)
        mRef.removeValue().addOnSuccessListener { 
            val titleDelete = "Balasan dihapus"
            val message = "Ada balasan yang dihapus"
            fcmNotification(titleDelete, message)
            actionBackActivity()
        }
    }

    private fun updateData() {
        if(pertanyaan.text.isEmpty()) {
            pertanyaan.error = "Mohon isi terlebih dahulu"
            return
        } else {
            val mRef = mDatabaseReference!!.child(user!!)
                .child("tb_consultation")
                .child(idconsul!!)
                .child("tb_replies")
                .child(idreply!!)
                .child("tb_users_replies")
                .child(id!!)
                .child("text")
            mRef.setValue(pertanyaan.text.toString()).addOnSuccessListener {
                val titleUpdate = "Balasan diupdate"
                val message = pertanyaan.text.toString()
                fcmNotification(titleUpdate, message)
                actionBackActivity()
            }
        }

    }

    private fun actionBackActivity() {
        mDatabaseReference!!.child(user!!)
            .child("tb_consultation")
            .child(idconsul!!)
            .child("tb_replies")
            .child(idreply!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bundle = Bundle()
                    bundle.putString("key", idreply)
                    bundle.putString("key_parent", idconsul)
                    bundle.putString("description", snapshot.child("text").value.toString())
                    bundle.putString("user", user)
                    bundle.putString("token", token)
                    bundle.putString("time", time)
                    val intent = Intent(this@UpdateConsultationReplyLastActivity, ReplyConsultationActivty::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            })

    }

    private fun fcmNotification(titleUpdate: String, message: String) {
        PushNotification(
            NotificationData(titleUpdate, message),
            token!!).also {
            sendNotification(it)
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
//            Log.e(TAG, e.toString())
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}