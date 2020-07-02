package id.smkcoding.teamalvan.api.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.smkcoding.teamalvan.R
import id.smkcoding.teamalvan.api.ApiCovidProvinceItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_covid_province.*

class ProvinceCovidAdapter(private val context: Context, private val items: List<ApiCovidProvinceItem>,
private val listener: (ApiCovidProvinceItem)-> Unit): RecyclerView.Adapter<ProvinceCovidAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        context,
        LayoutInflater.from(context).inflate(
            R.layout.item_covid_province,
            parent,
            false
        )
    )
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), listener)
    }

    class ViewHolder(val context: Context, override val containerView: View):
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(
            item: ApiCovidProvinceItem,
            listener: (ApiCovidProvinceItem) -> Unit
        ) {
            val meninggal = item.attributes.kasusMeni
            val sembuh = item.attributes.kasusSemb
            val positif = item.attributes.kasusPosi
            val dirawat = positif - sembuh - meninggal
            tv_nama_provinsi.text = item.attributes.provinsi
            tv_positif_provinsi.text = positif.toString()
            tv_dirawat_provinsi.text = dirawat.toString()
            tv_meninggal_provinsi.text = meninggal.toString()
            tv_sembuh_provinsi.text = sembuh.toString()
        }
    }


}