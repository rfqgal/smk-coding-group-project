package id.smkcoding.teamalvan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.smkcoding.teamalvan.model.ConsultationRepliesModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_consultation_replies.*

class ConsultationRepliesLastAdapter(private val context: Context, var list: MutableList<ConsultationRepliesModel>):
    RecyclerView.Adapter<ConsultationRepliesLastAdapter.ViewHolder>() {

    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            context,
            LayoutInflater.from(context)
                .inflate(R.layout.item_consultation_replies_last, parent, false)
        )

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: ConsultationRepliesLastAdapter.ViewHolder,
        position: Int
    ) {
        holder.bindItem(list[position], list as ArrayList<ConsultationRepliesModel>)
    }

    inner class ViewHolder(val context: Context, override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(item: ConsultationRepliesModel, list: ArrayList<ConsultationRepliesModel>) {
            tv_deskripsi_konsultasi_replied.text = item.text
            tv_nama_konsultasi_replied.text = item.iduser
            tv_timestamp_konsultasi_replied.text = item.time

        }
    }

}