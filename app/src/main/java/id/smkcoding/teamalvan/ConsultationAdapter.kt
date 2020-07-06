package id.smkcoding.teamalvan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import id.smkcoding.teamalvan.model.ConsultationModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_consultation.*

class ConsultationAdapter(private val context: Context, var list: MutableList<ConsultationModel>):
        RecyclerView.Adapter<ConsultationAdapter.ViewHolder>() {

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
        }
    }

}