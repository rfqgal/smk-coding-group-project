package id.smkcoding.teamalvan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.smkcoding.teamalvan.model.ConsultationModel
import kotlinx.android.extensions.LayoutContainer

class ConsultationAdapter(val mCtx: Context, val layoutResId: Int, val consultationlist: List<ConsultationModel> )
    : ArrayAdapter<ConsultationModel>(mCtx,layoutResId,consultationlist)  {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view : View = layoutInflater.inflate(layoutResId, null)

        val tvnama : TextView = view.findViewById(R.id.tv_nama_konsultasi)
        val tvwaktu : TextView = view.findViewById(R.id.tv_timestamp_konsultasi)
        val tvtext : TextView = view.findViewById(R.id.tv_deskripsi_konsultasi)
        val consul = consultationlist[position]



        tvnama.text = consul.IDuser
        tvwaktu.text = consul.Time
        tvtext.text = consul.Text

        return view
    }
}