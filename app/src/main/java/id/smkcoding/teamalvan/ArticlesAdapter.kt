package id.smkcoding.teamalvan

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import id.smkcoding.teamalvan.model.ArticlesModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_recent_article.*

class ArticlesAdapter(private val context: Context, private val list: ArrayList<ArticlesModel>) :
    RecyclerView.Adapter<ArticlesAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_recent_article, parent, false)
    )
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list.get(position))

        //Mengambil Value di RecyclerView berdasarkan Posisi Tertentu
        val Title: String? = list.get(position).title
        val Time: String? = list.get(position).time
//        val Cover: String? = list.get(position).cover
        val Name: String? = list.get(position).name_dokter
//        val PhotoProfile: String? = list.get(position).photo_dokter

        //Memasukan Nilai/Value ke dalam View (TextView: Nama, Email, Telp, Alamat)
        holder.tv_judul_artikel_terbaru.setText("$Title")
        holder.tv_waktu.setText("$Time")
        holder.tv_dokter_artikel_terbaru.setText("$Name")
//        holder.txtFriendAlamat.setText("$PhotoProfile")

        //Deklarasi objek dari Interfece
//        val listener: dataListener? = null
        lateinit var ref : DatabaseReference
        lateinit var auth: FirebaseAuth


        holder.card_view_recent_articles.setOnLongClickListener(View.OnLongClickListener { view ->
            val action = arrayOf("Update", "Delete")
            val alert = AlertDialog.Builder(view.context)
            alert.setItems(action) { dialog, i ->
                when (i) {
                    0 -> {
                        /* Berpindah Activity pada halaman layout updateData dan
                       mengambil data pada listMahasiswa, berdasarkan posisinya untuk dikirim pada
                       activity selanjutnya
                        */
                        val bundle = Bundle()
                        bundle.putString("dataNama", list.get(position).nama)
                        bundle.putString("dataEmail", list.get(position).email)
                        bundle.putString("dataTelp", list.get(position).telp)
                        bundle.putString("dataAlamat", list.get(position).alamat)
                        bundle.putString("getPrimaryKey", list.get(position).key)

                        val intent = Intent(view.context, NewsUpdateActivity::class.java)
                        intent.putExtras(bundle)
                        context.startActivity(intent)
                    }
                    1 -> {
                        auth = FirebaseAuth.getInstance()
                        ref = FirebaseDatabase.getInstance().getReference()
                        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
                        if (ref != null) {
                            ref.child(getUserID)
                                .child("Teman")
                                .child(list.get(position)?.key.toString())
                                .removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context, "Data Berhasil Dihapus",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                }
            }
            alert.create()
            alert.show()
            true
        })

    }
    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItem(item: ArticlesModel) {
            tv_judul_artikel_terbaru.text = item.title
            tv_waktu.text = item.time
            tv_dokter_artikel_terbaru.text = item.name_dokter
        }
    }
}