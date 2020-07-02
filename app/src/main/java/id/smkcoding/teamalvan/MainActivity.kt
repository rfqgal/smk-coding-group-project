package id.smkcoding.teamalvan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    val menuIcon = arrayOf(R.drawable.ic_home, R.drawable.ic_user)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
//        if(mAuth!!.currentUser == null) {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
        val adapter = ViewPagerAdapter(this)
        view_pager.setAdapter(adapter)
        TabLayoutMediator(tab_layout, view_pager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.icon = ResourcesCompat.getDrawable(resources, menuIcon[position], null)
        }).attach()
    }


}
