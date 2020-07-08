package id.smkcoding.teamalvan

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import kotlinx.android.synthetic.main.activity_read_more_consultation.*
import kotlinx.android.synthetic.main.activity_update_articles.*
import kotlinx.android.synthetic.main.activity_update_consultation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class UpdateConsultationActivity : AppCompatActivity() {

    private val TAG: String? = "Update Consultation"
    private var auth: FirebaseAuth? = null
    private var description: EditText? = null
    private var nama: TextView? = null
    private var token: TextView? = null
    private var tanggal: TextView? = null
    private var id_p: TextView? = null
    private var foto: TextView? = null
    private var namau: TextView? = null
    private var jenis: TextView? = null
    private var database: DatabaseReference? = null
    private var cekpertanyaan: String? = null
    private var ceknama: String? = null
    private var cektanggal: String? = null
    private var cekidp: String? = null
    private var cekjenis: String? = null
    private var cektoken: String? = null
    private var cekfoto: String? = null
    private var ceknamauser: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_consultation)
        description = findViewById(R.id.pertanyaan)
        token = findViewById(R.id.token)
        tanggal = findViewById(R.id.tanggal)
        id_p = findViewById(R.id.idpertanyaan)
        jenis = findViewById(R.id.jenis)
        nama = findViewById(R.id.pengirim)
        foto = findViewById(R.id.foto)
        namau = findViewById(R.id.namau)


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        getData()
        simpan.setOnClickListener {

            cekpertanyaan = description?.getText().toString()
            ceknama = nama?.getText().toString()
            cektoken = token?.getText().toString()
            cektanggal = tanggal?.getText().toString()
            cekidp = id_p?.getText().toString()
            cekjenis = jenis?.getText().toString()
            cekfoto = foto?.getText().toString()
            ceknamauser = namau?.getText().toString()

            if (isEmpty(cekpertanyaan)) {
                Toast.makeText(this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
            } else {

                val dataupdate = ConsultationModel(cekidp!!,ceknama!!,cekjenis!!,cekpertanyaan!!,cektanggal!!,cektoken!!,cekfoto!!,ceknamauser!!)
                val UserID: String = auth?.getCurrentUser()?.getUid().toString()
                val getKey: String = getIntent().getStringExtra("key").toString()
                database!!.child(UserID).child("tb_consultation")
                    .child(getKey).setValue(dataupdate)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Data Berhasil DiUpdate", Toast.LENGTH_SHORT).show()
                        finish();
                    }
            }
        }

        hapus.setOnClickListener{
            hapusdata()
        }
    }

    private fun hapusdata() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()
        val getKey: String = getIntent().getStringExtra("key").toString()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        if (database != null) {
            database!!.child(getUserID)
                .child("tb_consultation")
                .child(getKey)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                finish();
                }
        }

    }

    private fun getData() {

        val getPertanyaan: String  = getIntent().getStringExtra("pertanyaan").toString()
        val getTanggal: String  = getIntent().getStringExtra("tanggal").toString()
        val getNama: String  = getIntent().getStringExtra("nama").toString()
        val getToken: String  = getIntent().getStringExtra("token").toString()
        val getIDP: String  = getIntent().getStringExtra("IDP").toString()
        val getJenis: String  = getIntent().getStringExtra("jenis").toString()
        val getFoto: String  = getIntent().getStringExtra("foto").toString()
        val getNamau: String  = getIntent().getStringExtra("namauser").toString()

        description?.setText(getPertanyaan);
        tanggal?.setText(getTanggal);
        nama?.setText(getNama);
        token?.setText(getToken);
        id_p?.setText(getIDP);
        jenis?.setText(getJenis);
        foto?.setText(getFoto);
        namau?.setText(getNamau);
        Toast.makeText(this, getNama, Toast.LENGTH_SHORT).show()

    }
}




