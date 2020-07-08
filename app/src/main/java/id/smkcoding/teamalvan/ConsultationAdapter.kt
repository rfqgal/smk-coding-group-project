package id.smkcoding.teamalvan

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.smkcoding.teamalvan.model.ConsultationModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_consultation.*
import kotlinx.android.synthetic.main.item_consultation.btn_more
import kotlinx.android.synthetic.main.item_my_article.*

class ConsultationAdapter(private val context: Context, var list: MutableList<ConsultationModel>, var keylist: MutableList<String>):
        RecyclerView.Adapter<ConsultationAdapter.ViewHolder>() {

    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var database: DatabaseReference? = null
    lateinit var listener: MutableList<ConsultationModel>
    private var item = emptyList<ConsultationModel>()
    lateinit var ref: DatabaseReference
    lateinit var auth: FirebaseAuth

    private var keyConsul: String = ""

    internal fun setData(item: List<ConsultationModel>) {
        this.list = item as MutableList<ConsultationModel>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.item_consultation, parent, false))

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position], list as ArrayList<ConsultationModel>)

    }

    inner class ViewHolder(val context: Context, override val containerView: View):
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem( item: ConsultationModel, list: ArrayList<ConsultationModel>) {
            tv_deskripsi_konsultasi.text = item.text
            tv_nama_konsultasi.text = item.iduser
            tv_timestamp_konsultasi.text = item.time
            tv_nama_konsultasi.text = item.nama
            Glide.with(context)
                .load(item.foto)
                .into(img_konsultasi);
            tv_consultation_readmore.setOnClickListener {
                displayConsultation(item)
            }
            btn_more.setOnClickListener(View.OnClickListener { view ->
                val action = arrayOf("Update", "Delete")
                val alert = AlertDialog.Builder(view.context)
                alert.setItems(action) { dialog, i ->
                    when (i) {
                        0 -> {

                            more(item)
                        }
                        1 -> {
                            hapusdata(item)
                        }
                    }
                }
                alert.create()
                alert.show()
                true
            })
        }
    }
    private fun hapusdata(item: ConsultationModel) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()
        val getKey: String = item.idpertanyaan
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        if (database != null) {
            database!!.child(getUserID)
                .child("tb_consultation")
                .child(getKey)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(context, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()

                }
        }

    }


    private fun more(item: ConsultationModel) {
        val bundle = Bundle()
        bundle.putString("pertanyaan", item.text)
        bundle.putString("nama", item.iduser)
        bundle.putString("tanggal", item.time)
        bundle.putString("token", item.token)
        bundle.putString("IDP", item.idpertanyaan)
        bundle.putString("jenis", item.jenis)
        bundle.putString("key", item.idpertanyaan)
        bundle.putString("foto", item.foto)
        bundle.putString("namauser", item.nama)
        val intent = Intent(context, UpdateConsultationActivity::class.java)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    private fun displayConsultation(item: ConsultationModel) {
        val bundle = Bundle()
        bundle.putString("description", item.text)
        bundle.putString("user", item.iduser)
        bundle.putString("time", item.time)
        bundle.putString("token", item.token)
        bundle.putString("key", item.idpertanyaan)
        bundle.putString("foto", item.foto)
        bundle.putString("nama", item.nama)

        val intent = Intent(context, ReadMoreConsultationActivity::class.java)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }



}