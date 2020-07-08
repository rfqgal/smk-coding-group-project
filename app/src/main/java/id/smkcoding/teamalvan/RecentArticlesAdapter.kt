package id.smkcoding.teamalvan

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import id.smkcoding.teamalvan.model.ArticlesModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_recent_article.*
import kotlinx.android.synthetic.main.item_recent_article.img_cover_berita
import kotlinx.android.synthetic.main.item_recent_article.img_dokter_artikel_terbaru
import kotlinx.android.synthetic.main.item_recent_article.tv_deskripsi_artikel_terbaru
import kotlinx.android.synthetic.main.item_recent_article.tv_dokter_artikel_terbaru
import kotlinx.android.synthetic.main.item_recent_article.tv_judul_artikel_terbaru
import kotlinx.android.synthetic.main.item_recent_article.tv_waktu

class RecentArticlesAdapter(private val context: Context, private val list: ArrayList<ArticlesModel>) :
    RecyclerView.Adapter<RecentArticlesAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_recent_article, parent, false)
    )
    override fun getItemCount(): Int {
        val batasArtikel = 2
        when{
            list.size > batasArtikel ->
                return batasArtikel
            else ->{
                return list.size
            }
        }

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list.get(position))

        //Mengambil Value di RecyclerView berdasarkan Posisi Tertentu
        val Title: String? = list.get(position).title
        val Time: String? = list.get(position).time
        val Cover: String? = list.get(position).cover
        val Name: String? = list.get(position).name_dokter
        val Caption: String? = list.get(position).caption
        val PhotoProfile: String? = list.get(position).photo_dokter
        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/chalenge3-e740d.appspot.com/o/gambar%2F7c02a166-6971-460f-ae49-9c946862bd0d.jpg?alt=media&token=20486837-b5bd-4cfa-b48a-c40942aa5de0")
//        ("gs://chalenge3-e740d.appspot.com/gambar/7c02a166-6971-460f-ae49-9c946862bd0d.jpg")

        //Memasukan Nilai/Value ke dalam View (TextView: Nama, Email, Telp, Alamat)
        holder.tv_judul_artikel_terbaru.setText("$Title")
        holder.tv_waktu.setText("$Time")
        holder.tv_dokter_artikel_terbaru.setText("$Name")
//        holder.txtFriendAlamat.setText("$PhotoProfile")
        holder.tv_deskripsi_artikel_terbaru.setText("$Caption")


        Glide.with(context)
            .load("$PhotoProfile")
            .into(holder.img_dokter_artikel_terbaru);

        Glide.with(context)
            .load("$gsReference")
            .into(holder.img_cover_berita);

//        Glide.with(context)
//            .load("gs://chalenge3-e740d.appspot.com/gambar/7c02a166-6971-460f-ae49-9c946862bd0d.jpg")
//            .into(holder.img_cover_berita);

        //Deklarasi objek dari Interfece
//        val listener: dataListener? = null
        lateinit var ref : DatabaseReference
        lateinit var auth: FirebaseAuth

        holder.tv_read_more.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("dataTitle", list.get(position).title)
            bundle.putString("dataTime", list.get(position).time)
            bundle.putString("dataPhotoDokter", list.get(position).photo_dokter)
            bundle.putString("dataNameDokter", list.get(position).name_dokter)
            bundle.putString("dataCaption", list.get(position).caption)
            bundle.putString("getPrimaryKey", list.get(position).key)

            val intent = Intent(context, ReadMoreArticlesActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)

        })


//        holder.btn_more.setOnClickListener(View.OnClickListener { view ->
//            val action = arrayOf("Update", "Delete")
//            val alert = AlertDialog.Builder(view.context)
//            alert.setItems(action) { dialog, i ->
//                when (i) {
//                    0 -> {
//
//                        val bundle = Bundle()
//                        bundle.putString("dataTitle", list.get(position).title)
//                        bundle.putString("dataTime", list.get(position).time)
//                        bundle.putString("dataNameDokter", list.get(position).name_dokter)
//                        bundle.putString("dataCategory", list.get(position).category)
//                        bundle.putString("dataCaption", list.get(position).caption)
//                        bundle.putString("getPrimaryKey", list.get(position).key)
//
//                        val intent = Intent(view.context, UpdateArticlesActivity::class.java)
//                        intent.putExtras(bundle)
//                        context.startActivity(intent)
//                    }
//                    1 -> {
//                        auth = FirebaseAuth.getInstance()
//                        ref = FirebaseDatabase.getInstance().getReference()
//                        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
//                        if (ref != null) {
//                            ref.child(getUserID)
//                                .child("tb_articles")
//                                .child(list.get(position)?.key.toString())
//                                .removeValue()
//                                .addOnSuccessListener {
//                                    Toast.makeText(
//                                        context, "Data Berhasil Dihapus",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                        }
//                    }
//                }
//            }
//            alert.create()
//            alert.show()
//            true
//        })

    }
    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItem(item: ArticlesModel) {
            tv_judul_artikel_terbaru.text = item.title
            tv_waktu.text = item.time
            tv_dokter_artikel_terbaru.text = item.name_dokter
            tv_deskripsi_artikel_terbaru.text = item.caption
        }
    }
}