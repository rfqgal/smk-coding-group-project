package id.smkcoding.teamalvan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.smkcoding.teamalvan.model.ArticlesModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_my_articles.*
import kotlinx.android.synthetic.main.fragment_articles.*

class MyArticlesActivity : AppCompatActivity() {
    lateinit var ref: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var dataArticles: ArrayList<ArticlesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_articles)

        getData()
        btn_write_article.setOnClickListener {
            val intent = Intent(this, AddArticlesActivity::class.java)
            this.startActivity(intent)
        }
    }


    private fun getData() {
        //Mendapatkan Referensi Database
        Toast.makeText(this, "Mohon Tunggu Sebentar...", Toast.LENGTH_LONG).show()
        auth = FirebaseAuth.getInstance()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        ref = FirebaseDatabase.getInstance().getReference()
        ref.child(getUserID).child("tb_articles").addValueEventListener(object :
            ValueEventListener { override fun onCancelled(p0: DatabaseError) {
            Toast.makeText(
                this@MyArticlesActivity, "Database Error yaa...", Toast.LENGTH_LONG).show()
        }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Inisialisasi ArrayList
                dataArticles = java.util.ArrayList<ArticlesModel>()
                for (snapshot in dataSnapshot.children) {
                    //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                    val teman = snapshot.getValue(ArticlesModel::class.java)
                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    teman?.key = snapshot.key.toString()
                    dataArticles.add(teman!!)
                }
                //Memasang Adapter pada RecyclerView
                rv_my_article.layoutManager = LinearLayoutManager(this@MyArticlesActivity)
                rv_my_article.adapter = ArticlesAdapter(this@MyArticlesActivity, dataArticles)
                Toast.makeText(
                    this@MyArticlesActivity, "Data Berhasil Dimuat",
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}