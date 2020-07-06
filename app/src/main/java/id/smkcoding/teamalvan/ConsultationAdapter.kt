package id.smkcoding.teamalvan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.smkcoding.teamalvan.model.ConsultationModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_consultation.*

class ConsultationAdapter(private val context: Context, var list: MutableList<ConsultationModel>):
        RecyclerView.Adapter<ConsultationAdapter.ViewHolder>() {

    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    lateinit var listener: MutableList<ConsultationModel>
    private var item = emptyList<ConsultationModel>()
    lateinit var ref: DatabaseReference
    lateinit var auth: FirebaseAuth

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
        fun bindItem(
            item: ConsultationModel,
            list: ArrayList<ConsultationModel>
        ) {
            tv_deskripsi_konsultasi.text = item.text
            tv_nama_konsultasi.text = item.iduser
            tv_timestamp_konsultasi.text = item.time
            tv_consultation_readmore.setOnClickListener {
                displayConsultation(item)
            }
        }
    }

    private fun displayConsultation(item: ConsultationModel) {
        val bundle = Bundle()
        bundle.putString("description", item.text)
        bundle.putString("user", item.iduser)
        bundle.putString("time", item.time)
        bundle.putString("token", item.token)
        val intent = Intent(context, ReadMoreConsultationActivity::class.java)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

}