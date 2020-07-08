package id.smkcoding.teamalvan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import id.smkcoding.teamalvan.api.ApiCovidIndonesiaItem
import id.smkcoding.teamalvan.api.data.IndonesiaCovidAdapter
import id.smkcoding.teamalvan.api.data.IndonesiaCovidService
import id.smkcoding.teamalvan.api.data.apiRequest
import id.smkcoding.teamalvan.model.ArticlesModel
import id.smkcoding.teamalvan.notification.FirebaseServices
import id.smkcoding.teamalvan.notification.NotificationData
import id.smkcoding.teamalvan.notification.PushNotification
import id.smkcoding.teamalvan.notification.RetrofitInstance
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_my_articles.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

const val TOPIC = "/topics/myTopic2"

class HomeFragment: Fragment() {

    val TAG = "HomeFragment"
    lateinit var ref: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var dataArticles: ArrayList<ArticlesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.title = "Home"
    }

    override fun onResume() {
        super.onResume()
        activity!!.title = "Home"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApiCovid()
        getDataArticles()
        txt_show_more.setOnClickListener {
            val intent = Intent(context, MyArticlesActivity::class.java)
            this.startActivity(intent)
        }
//        FirebaseServices.sharedPref = this.activity!!.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//            FirebaseServices.token = it.token
//            etToken.setText(it.token)
//        }
//        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
//        btn_sendNotif.setOnClickListener {
//            val message = edt_sendNotif.text.toString()
//            val title = "Konsultasi baru"
//            val recipientToken = etToken.text.toString()
//            if(message.isNotEmpty() && recipientToken.isNotEmpty()) {
//                PushNotification(
//                    NotificationData(title, message),
//                    recipientToken
//                ).also {
//                    sendNotification(it)
//                }
//            }
//        }
    }

    private fun callApiCovid() {
        val httpClient = OkHttpClient()
        val apiRequest = apiRequest<IndonesiaCovidService>(httpClient)
        val call = apiRequest.getCovidData()
        call.enqueue(object : Callback<List<ApiCovidIndonesiaItem>> {
            override fun onFailure(call: Call<List<ApiCovidIndonesiaItem>>, t: Throwable) {
                // Tampil toast
            }

            override fun onResponse(
                call: Call<List<ApiCovidIndonesiaItem>>,
                response: Response<List<ApiCovidIndonesiaItem>>
            ) {
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null ->
                                displayCovidData(response.body()!!)
                            else -> {
                                // Toast Berhasil
                            }
                        }
                    else -> {
                        // Toast Gagal
                    }
                }
            }

        })
    }

    private fun displayCovidData(body: List<ApiCovidIndonesiaItem>) {
        rv_covid_indonesia.layoutManager = LinearLayoutManager(context)
        rv_covid_indonesia.adapter =
            IndonesiaCovidAdapter(
                context!!,
                body
            ) {
                val item = it
            }
    }

    private fun getDataArticles() {
        //Mendapatkan Referensi Database
        Toast.makeText(context, "Please Wait", Toast.LENGTH_LONG).show()
        auth = FirebaseAuth.getInstance()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        ref = FirebaseDatabase.getInstance().getReference()
        ref.child(getUserID).child("tb_articles").addValueEventListener(object :
            ValueEventListener { override fun onCancelled(p0: DatabaseError) {
            Toast.makeText(
                context, "Database Error", Toast.LENGTH_LONG).show()
        }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Inisialisasi ArrayList
                dataArticles = java.util.ArrayList<ArticlesModel>()
                for (snapshot in dataSnapshot.children) {
                    //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                    val articles = snapshot.getValue(ArticlesModel::class.java)
                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    articles?.key = snapshot.key.toString()
                    dataArticles.add(articles!!)
                }
                //Memasang Adapter pada RecyclerView
                rv_recent_article.layoutManager = LinearLayoutManager(context)
                rv_recent_article.adapter = RecentArticlesAdapter(context!!, dataArticles)
                Toast.makeText(context, "Load Data Successful", Toast.LENGTH_LONG).show()
            }
        })
    }

//    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val response = RetrofitInstance.api.postNotification(notification)
//            if(response.isSuccessful) {
////                Log.d(TAG, "Response: ${Gson().toJson(response)}")
//            } else {
////                Log.e(TAG, response.errorBody().toString())
//            }
//        } catch(e: Exception) {
//            Log.e(TAG, e.toString())
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }

}
