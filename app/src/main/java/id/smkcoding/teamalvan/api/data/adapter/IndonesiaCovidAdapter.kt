package id.smkcoding.teamalvan.api.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.smkcoding.teamalvan.R
import id.smkcoding.teamalvan.api.ApiCovidIndonesiaItem
import kotlinx.android.extensions.LayoutContainer

class IndonesiaCovidAdapter(private val context: Context, private val items: List<ApiCovidIndonesiaItem>,
private val listener: (ApiCovidIndonesiaItem)-> Unit): RecyclerView.Adapter<IndonesiaCovidAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.activity_main, parent, false))

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), listener)
    }

    class ViewHolder(val context: Context, override val containerView: View):
            RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(item: ApiCovidIndonesiaItem, listener: (ApiCovidIndonesiaItem)-> Unit) {
            
        }
    }

}