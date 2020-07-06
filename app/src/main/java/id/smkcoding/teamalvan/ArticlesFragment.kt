package id.smkcoding.teamalvan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.smkcoding.teamalvan.model.ArticlesModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_articles.*


class ArticlesFragment : Fragment() {
//    lateinit var ref: DatabaseReference
//    lateinit var auth: FirebaseAuth
//    lateinit var dataArticles: ArrayList<ArticlesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        getData()
//
//        btn_add_articles.setOnClickListener {
//            val intent = Intent(getActivity(), AddArticlesActivity::class.java)
//            getActivity()?.startActivity(intent)
//        }
//    }
//
//    private fun getData() {
//        //Mendapatkan Referensi Database
//        Toast.makeText(getContext(), "Mohon Tunggu Sebentar...", Toast.LENGTH_LONG).show()
//        auth = FirebaseAuth.getInstance()
//        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
//        ref = FirebaseDatabase.getInstance().getReference()
//        ref.child(getUserID).child("Teman").addValueEventListener(object :
//            ValueEventListener { override fun onCancelled(p0: DatabaseError) {
//                Toast.makeText(
//                    getContext(), "Database Error yaa...", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                //Inisialisasi ArrayList
//                dataArticles = java.util.ArrayList<ArticlesModel>()
//                for (snapshot in dataSnapshot.children) {
//                    //Mapping data pada DataSnapshot ke dalam objek mahasiswa
//                    val teman = snapshot.getValue(ArticlesModel::class.java)
//                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
//                    teman?.key = snapshot.key.toString()
//                    dataArticles.add(teman!!)
//                }
//                //Memasang Adapter pada RecyclerView
//                rv_listArticles.layoutManager = LinearLayoutManager(context)
//                rv_listArticles.adapter = NewsPortalAdapter(context!!, dataArticles)
//                Toast.makeText(
//                    getContext(), "Data Berhasil Dimuat",
//                    Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        this.clearFindViewByIdCache()
//    }
}