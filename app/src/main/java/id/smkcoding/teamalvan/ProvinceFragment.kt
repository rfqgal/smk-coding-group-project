package id.smkcoding.teamalvan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.smkcoding.teamalvan.api.ApiCovidProvinceItem
import id.smkcoding.teamalvan.api.data.IndonesiaCovidService
import id.smkcoding.teamalvan.api.data.ProvinceCovidAdapter
import id.smkcoding.teamalvan.api.data.apiRequest
import kotlinx.android.synthetic.main.fragment_province.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProvinceFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.title = "COVID-19 Province"
    }

    override fun onResume() {
        super.onResume()
        activity!!.title = "COVID-19 Province"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_province, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApiProvince()
    }

    private fun callApiProvince() {
        val httpClient = OkHttpClient()
        val apiRequest = apiRequest<IndonesiaCovidService>(httpClient)
        val call = apiRequest.getCovidProvince()
        call.enqueue(object : Callback<List<ApiCovidProvinceItem>> {
            override fun onFailure(call: Call<List<ApiCovidProvinceItem>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<List<ApiCovidProvinceItem>>,
                response: Response<List<ApiCovidProvinceItem>>
            ) {
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null ->
                                displayCovidProvince(response.body()!!)
                            else -> {
                                // Toast Berhasil
                            }
                        }
                    else -> {
                        // Toast Gagal
                    }
                }
            }
        })
    }

    private fun displayCovidProvince(body: List<ApiCovidProvinceItem>) {
        rv_covid_province.layoutManager = LinearLayoutManager(context)
        rv_covid_province.adapter =
            ProvinceCovidAdapter(
                context!!,
                body
            ) {
                val item = it
            }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}