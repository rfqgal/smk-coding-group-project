package id.smkcoding.teamalvan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.smkcoding.teamalvan.api.ApiCovidProvinceItem
import id.smkcoding.teamalvan.api.data.IndonesiaCovidService
import id.smkcoding.teamalvan.api.data.ProvinceCovidAdapter
import id.smkcoding.teamalvan.api.data.apiRequest
import id.smkcoding.teamalvan.model.ConsultationModel
import kotlinx.android.synthetic.main.fragment_consultation.*
import kotlinx.android.synthetic.main.fragment_province.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultationFragment: Fragment(), View.OnClickListener {
    private lateinit var ref : DatabaseReference
    private lateinit var consultationlist : MutableList<ConsultationModel>
    private var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.title = "Consultation"
    }

    override fun onResume() {
        super.onResume()
        activity!!.title = "Consultation"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consultation, container, false)

    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_write_consultation.setOnClickListener{
            konsultasi()
        }
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        ref = FirebaseDatabase.getInstance().getReference().child(user!!.uid).child("tb_consultation")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Database Error yaa...", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    var keylist = ArrayList<String>()
                    consultationlist = ArrayList<ConsultationModel>()
                    for (h in p0.children) {
                        val consul = h.getValue(ConsultationModel::class.java)
                        consultationlist.add(consul!!)
                        keylist.add(h.key.toString())
                    }
                    consultation_list.layoutManager = LinearLayoutManager(context)
                    consultation_list.adapter = ConsultationAdapter(context!!, consultationlist, keylist)
                }
            }

        })

    }

    private fun konsultasi() {
        val intent = Intent (context, ConsultationActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }


}