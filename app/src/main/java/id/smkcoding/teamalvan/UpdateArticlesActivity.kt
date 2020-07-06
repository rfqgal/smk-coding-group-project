package id.smkcoding.teamalvan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.smkcoding.teamalvan.model.ArticlesModel
import java.text.SimpleDateFormat
import java.util.*

class UpdateArticlesActivity : AppCompatActivity() {
   //Deklarasi Variable
    private var titleBaru: EditText? = null
    private var captionBaru: EditText? = null
    private var categoryBaru: EditText? = null
    private var coverBaru: EditText? = null
    lateinit var update: Button
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var cekTitle: String? = null
    private var cekCaption: String? = null
    private var cekCategory: String? = null
    private var cekCover: String? = null
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_articles)

        getSupportActionBar()?.setTitle("Update Data")
        ref = FirebaseDatabase.getInstance().getReference()
        titleBaru = findViewById(R.id.txt_title_new)
        captionBaru = findViewById(R.id.txt_caption_new)
        categoryBaru = findViewById(R.id.category_new)
        coverBaru = findViewById(R.id.cover_new)
        update = findViewById(R.id.update)

        //Mendapatkan Instance autentikasi dan Referensi dari Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        getData();

        //Mengambil dari intent data yang akan di update
        update.setOnClickListener {
            //Mendapatkan Data Mahasiswa yang akan dicek
            cekTitle = titleBaru?.getText().toString()
            cekCaption = captionBaru?.getText().toString()
            cekCategory = categoryBaru?.getText().toString()
            cekCover = coverBaru?.getText().toString()
            val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
            val current = SimpleDateFormat("yyyy-MM-dd")
            val getTime = current.format(Date())
            val getPrimaryKey = ref.push().key.toString()
            val getNameDokter: String = auth?.getCurrentUser()?.displayName.toString()
            val getPhotoDokter: String = auth?.getCurrentUser()?.photoUrl.toString()

            //Mengecek agar tidak ada data yang kosong, saat proses update
            if (isEmpty(cekTitle) || isEmpty(cekCaption) || isEmpty(cekCategory) || isEmpty(cekCover)) {
                Toast.makeText(this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
            } else {
                /* Menjalankan proses update data. Method Setter digunakan untuk mendapakan data baru yang diinputkan User.*/
                val articlesBaru = ArticlesModel(getUserID,getNameDokter,getPhotoDokter,getTime,cekTitle!!, cekCaption!!, cekCategory!!, cekCover!!,getPrimaryKey)
                val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
                val getKey: String = getIntent().getStringExtra("getPrimaryKey").toString()
                database!!.child(getUserID).child("tb_articles")
                    .child(getKey).setValue(articlesBaru)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Load Data Successful", Toast.LENGTH_SHORT).show()
                        finish();
                    }
            }
        }
    }
    private fun getData() {
        //Menampilkan data dari item yang dipilih sebelumnya
        val getTitle: String = getIntent().getStringExtra("dataTitle").toString()
        val getCaption: String = getIntent().getExtras()?.getString("dataCaption").toString()
        val getCategory: String = getIntent().getExtras()?.getString("dataCategory").toString()
        val getCover: String = getIntent().getExtras()?.getString("dataCover").toString()
        titleBaru?.setText(getTitle);
        captionBaru?.setText(getCaption);
        categoryBaru?.setText(getCategory);
        coverBaru?.setText(getCover);
        Toast.makeText(this, getTitle, Toast.LENGTH_SHORT).show()
    }
}