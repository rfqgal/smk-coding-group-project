package id.smkcoding.teamalvan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.smkcoding.teamalvan.api.ApiCovidIndonesiaItem
import id.smkcoding.teamalvan.api.data.IndonesiaCovidService
import id.smkcoding.teamalvan.api.data.apiRequest
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApiCovid()
    }

    private fun callApiCovid() {
        val httpClient = OkHttpClient()
        val apiRequest = apiRequest<IndonesiaCovidService>(httpClient)
        val call = apiRequest.getCovidData()
        call.enqueue(object : Callback<List<ApiCovidIndonesiaItem>> {
            override fun onFailure(call: Call<List<ApiCovidIndonesiaItem>>, t: Throwable) {
                // Tampil toast
            }

            override fun onResponse(
                call: Call<List<ApiCovidIndonesiaItem>>,
                response: Response<List<ApiCovidIndonesiaItem>>
            ) {
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null ->
                                displayCovidData(response.body()!!)
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

    private fun displayCovidData(body: List<ApiCovidIndonesiaItem>) {
        rv_covid_indonesia.layoutManager = LinearLayoutManager(context)
        rv_covid_indonesia.adapter =
            IndonesiaCovidAdapter(context!!, body) {
                val item = it
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }

}
