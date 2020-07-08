package id.smkcoding.teamalvan

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.smkcoding.teamalvan.api.ApiCovidIndonesiaItem
import id.smkcoding.teamalvan.api.data.IndonesiaCovidService
import id.smkcoding.teamalvan.api.data.apiRequest
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_my_articles.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.item_my_article.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment: Fragment() {

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.title = "Profile"
    }

    override fun onResume() {
        super.onResume()
        activity!!.title = "Profile"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setHasOptionsMenu(true)
        btnGoToArticles.setOnClickListener {
            val intent = Intent(context, MyArticlesActivity::class.java)
            this.startActivity(intent)
        }
        btn_to_form_dokter.setOnClickListener {
            val intent = Intent(context, FormDokterActivity::class.java)
            this.startActivity(intent)
        }
    }

    private fun initialize() {
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        tv_profile_name.text = user?.displayName
        tv_profile_username.text = user?.email
        Glide.with(this)
            .load(user?.photoUrl)
            .into(img_profile_pic);
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun confirmLogout() {
        AlertDialog.Builder(activity)
            .setTitle("Logout")
            .setMessage("Apakah Anda yakin akan keluar?")
            .setPositiveButton("Ya") { dialog, which -> signOut() }
            .setNegativeButton("Tidak", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return super.onCreateOptionsMenu(menu, menuInflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return true
    }

    private fun setMode(item: Int) {
        when (item) {
            R.id.menu_logout -> confirmLogout()
        }
    }
}