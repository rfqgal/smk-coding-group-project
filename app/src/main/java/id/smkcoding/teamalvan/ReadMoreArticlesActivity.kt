package id.smkcoding.teamalvan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_my_article.*

class ReadMoreArticlesActivity : AppCompatActivity() {

    lateinit var titleMore: TextView
    lateinit var captionMore: TextView
    lateinit var timeMore: TextView
    lateinit var nameDokterMore: TextView
    lateinit var photoDokterMore: CircleImageView
    lateinit var coverMore: ImageView
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_more_articles)
        title = "Read More Article"

        ref = FirebaseDatabase.getInstance().getReference()
        titleMore = findViewById(R.id.tv_judul_artikel_more)
        captionMore = findViewById(R.id.tv_deskripsi_artikel_more)
        timeMore = findViewById(R.id.tv_waktu_more)
        nameDokterMore = findViewById(R.id.tv_dokter_artikel_more)
        photoDokterMore = findViewById(R.id.img_dokter_artikel_more)
        coverMore = findViewById(R.id.img_cover_berita_more)
        getData()

    }

    private fun getData() {
        val getTitle: String = getIntent().getStringExtra("dataTitle").toString()
        val getCaption: String = getIntent().getExtras()?.getString("dataCaption").toString()
        val getTime: String = getIntent().getExtras()?.getString("dataTime").toString()
        val getNameDokter: String = getIntent().getExtras()?.getString("dataNameDokter").toString()
        val getPhotoDokter: String = getIntent().getExtras()?.getString("dataPhotoDokter").toString()
        val getCover: String = getIntent().getExtras()?.getString("dataCover").toString()
        titleMore?.setText(getTitle);
        captionMore?.setText(getCaption);
        timeMore?.setText(getTime);
        nameDokterMore?.setText(getNameDokter);
        Glide.with(this)
            .load(getPhotoDokter)
            .into(photoDokterMore);
        Glide.with(this)
            .load(getCover)
            .into(coverMore);
    }
}