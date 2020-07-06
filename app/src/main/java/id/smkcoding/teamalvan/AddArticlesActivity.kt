package id.smkcoding.teamalvan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.smkcoding.teamalvan.model.ArticlesModel
import kotlinx.android.synthetic.main.activity_add_articles.*
import java.text.SimpleDateFormat
import java.util.*

class AddArticlesActivity : AppCompatActivity() {
    private var Title: EditText? = null
    private var Caption: EditText? = null
    private var Category: EditText? = null
    private var Cover: EditText? = null
    lateinit var ref : DatabaseReference
    private var auth: FirebaseAuth? = null
    private lateinit var button : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_articles)

        Title = findViewById<EditText>(R.id.txt_title)
        Caption = findViewById<EditText>(R.id.txt_caption)
        Category = findViewById<EditText>(R.id.category)
        Cover = findViewById<EditText>(R.id.cover)
        ref = FirebaseDatabase.getInstance().getReference()
        auth = FirebaseAuth.getInstance()

        simpan.setOnClickListener{
            prosesSave()
        }
    }
    private fun prosesSave() {
        val getTitle: String = Title?.getText().toString()
        val getCaption: String = Caption?.getText().toString()
        val getCategory: String = Category?.getText().toString()
        val getCover: String = Cover?.getText().toString()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val current = SimpleDateFormat("yyyy-MM-dd")
        val getTime = current.format(Date())
        val getPrimaryKey = ref.push().key.toString()
        val getNameDokter: String = auth?.getCurrentUser()?.displayName.toString()
        val getPhotoDokter: String = auth?.getCurrentUser()?.photoUrl.toString()

        if (getTitle.isEmpty() && getCaption.isEmpty() && getCategory.isEmpty() && getCover.isEmpty()) {
            //Jika kosong, maka akan menampilkan pesan singkat berikut ini.
            Toast.makeText(this@AddArticlesActivity,"Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
        } else {
            val articles = ArticlesModel(getUserID, getNameDokter, getPhotoDokter, getTime, getTitle, getCaption, getCategory, getCover, getPrimaryKey)
            //struktur databasenya adalah: ID >> Teman >> Key >> Data
            ref.child(getUserID).child("tb_articles").push().setValue(articles).addOnCompleteListener {
                Toast.makeText(this, "Data Berhasil Disimpan",
                    Toast.LENGTH_SHORT).show()
            }
            val intent = Intent (this, MyArticlesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}