package id.smkcoding.teamalvan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.smkcoding.teamalvan.model.ConsultationModel
import kotlinx.android.synthetic.main.activity_consultation.*
import java.text.SimpleDateFormat
import java.util.*

class ConsultationActivity : AppCompatActivity(), View.OnClickListener{


    lateinit var ref : DatabaseReference
    private var auth: FirebaseAuth? = null
    private lateinit var Pertanyaan : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultation)

        Pertanyaan = findViewById<EditText>(R.id.pertanyaan)

        auth = FirebaseAuth.getInstance() //Mendapakan Instance Firebase Auth
        ref = FirebaseDatabase.getInstance().getReference()

        simpan.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        save()
    }
    private fun save() {
        val getPertanyaan= Pertanyaan.text.toString().trim()
        if(getPertanyaan.isEmpty()){
            pertanyaan.error = "Mohon di Isi Terlebih Dahulu"
            return
        }

        val current = SimpleDateFormat("yyyy-MM-dd")
        val tanggal = current.format(Date())
        val pertanyaanid = ref.push().key.toString()
        val jenis = "pertanyaan"
        val UserID: String = auth?.getCurrentUser()?.getUid().toString()
        val question = ConsultationModel(UserID, tanggal, getPertanyaan, pertanyaanid,jenis)

        if (UserID != null){
            ref.child(UserID).child("tb_consultation").push().setValue(question).addOnCompleteListener{
                Toast.makeText(this,"Pertanyaan Anda Terkirim", Toast.LENGTH_SHORT).show()
                kembali()
            }

        }
    }

    private fun kembali() {
        val intent = Intent (this, ConsultationFragment::class.java)
        startActivity(intent)
    }

}