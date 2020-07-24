package id.smkcoding.teamalvan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.smkcoding.teamalvan.model.ArticlesModel
import kotlinx.android.synthetic.main.activity_add_articles.*
import kotlinx.android.synthetic.main.activity_update_articles.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateArticlesActivity : AppCompatActivity() {
   //Deklarasi Variable
    private var titleBaru: EditText? = null
    private var captionBaru: EditText? = null
    lateinit var coverBaru: ImageView
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var cekTitle: String? = null
    private var cekCaption: String? = null
    lateinit var ref : DatabaseReference

    //Post Image
    private var reference: StorageReference? = null
    private var progressBar: ProgressBar? = null
    private var imgPath: Uri? = null
    private var PICK_IMAGE_REQUEST = 71

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_articles)
        title = "Update Article"

        getSupportActionBar()?.setTitle("Update Data")
        ref = FirebaseDatabase.getInstance().getReference()
        titleBaru = findViewById(R.id.txt_title_new)
        captionBaru = findViewById(R.id.txt_caption_new)
        coverBaru = findViewById(R.id.imageContainer_new)

        //Mendapatkan Instance autentikasi dan Referensi dari Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        getData();

        select_image_new.setOnClickListener {
            getImage()
        }

        update.setOnClickListener {
            updateData()
        }
    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imgPath = data?.data
            imageContainer_new.setImageURI(imgPath)
        }
    }

    private fun updateData(){
        cekTitle = titleBaru?.getText().toString()
        cekCaption = captionBaru?.getText().toString()
        reference = FirebaseStorage.getInstance().getReference("Cover")

        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val current = SimpleDateFormat("yyyy-MM-dd")
        val getTime = current.format(Date())
        val getPrimaryKey = ref.push().key.toString()
        val getNameDokter: String = auth?.getCurrentUser()?.displayName.toString()
        val getPhotoDokter: String = auth?.getCurrentUser()?.photoUrl.toString()
        val getCategory: String = getIntent().getExtras()?.getString("dataCategory").toString()
        val getCoverOld: String = getIntent().getExtras()?.getString("dataCover").toString()

        //Mengecek agar tidak ada data yang kosong, saat proses update
//        if (isEmpty(cekTitle) || isEmpty(cekCaption)) {
//            Toast.makeText(this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
//        } else {
//            /* Menjalankan proses update data. Method Setter digunakan untuk mendapakan data baru yang diinputkan User.*/
//            val articlesBaru = ArticlesModel(getUserID,getNameDokter,getPhotoDokter,getTime,cekTitle!!, cekCaption!!, getCategory, "",getPrimaryKey)
//            val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
//            val getKey: String = getIntent().getStringExtra("getPrimaryKey").toString()
//            database!!.child(getUserID).child("tb_articles").child(getKey).setValue(articlesBaru).addOnCompleteListener {
//                    Toast.makeText(this, "Load Data Successful", Toast.LENGTH_SHORT).show()
//                    finish();
//                }
//        }

        when {
            cekTitle!!.isEmpty() -> txt_title_new.error = "Can't be empty"
            cekCaption!!.isEmpty() -> txt_caption_new.error = "Can't be empty"
            else -> {
                if (imgPath == null) {
                    val articlesBaru = ArticlesModel(
                        getUserID,
                        getNameDokter,
                        getPhotoDokter,
                        getTime,
                        cekTitle!!,
                        cekCaption!!,
                        getCategory,
                        getCoverOld,
                        getPrimaryKey
                    )
                    Toast.makeText(this@UpdateArticlesActivity, "Uploading...", Toast.LENGTH_SHORT).show()
                    val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
                    val getKey: String = getIntent().getStringExtra("getPrimaryKey").toString()
                    database!!.child(getUserID).child("tb_articles").child(getKey).setValue(articlesBaru).addOnCompleteListener {
                        finish()

                    }
                } else {
                    finish()
                    Toast.makeText(this@UpdateArticlesActivity, "Uploading...", Toast.LENGTH_LONG).show()
                    reference?.child(getUserID)?.putFile(imgPath!!)?.addOnSuccessListener {
                        reference?.child(getUserID)?.downloadUrl?.addOnSuccessListener {
                            val articlesBaru = ArticlesModel(
                                getUserID,
                                getNameDokter,
                                getPhotoDokter,
                                getTime,
                                cekTitle!!,
                                cekCaption!!,
                                getCategory,
                                it.toString(),
                                getPrimaryKey
                            )
                            val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
                            val getKey: String = getIntent().getStringExtra("getPrimaryKey").toString()
                            database!!.child(getUserID).child("tb_articles").child(getKey).setValue(articlesBaru).addOnCompleteListener {
                                val coverGambar = getIntent().getExtras()?.getString("dataCover").toString()
                                reference = FirebaseStorage.getInstance().getReference("Cover")
                                reference!!.child(coverGambar).delete().addOnCompleteListener {  }
                            }
                        }
                    }
                        ?.addOnFailureListener {
                            Toast.makeText(this@UpdateArticlesActivity, it.message, Toast.LENGTH_SHORT).show()
                        }

                }
            }
        }
    }



    private fun getData() {
        //Menampilkan data dari item yang dipilih sebelumnya
        val getTitle: String = getIntent().getStringExtra("dataTitle").toString()
        val getCaption: String = getIntent().getExtras()?.getString("dataCaption").toString()
        val getCover: String = getIntent().getExtras()?.getString("dataCover").toString()
        titleBaru?.setText(getTitle);
        captionBaru?.setText(getCaption);
        Glide.with(this)
            .load(getCover)
            .into(coverBaru);
        Toast.makeText(this, getTitle, Toast.LENGTH_SHORT).show()
    }
}