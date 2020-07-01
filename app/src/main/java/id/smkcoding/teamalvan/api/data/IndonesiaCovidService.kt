package id.smkcoding.teamalvan.api.data

import id.smkcoding.teamalvan.api.ApiCovidIndonesiaItem
import retrofit2.Call
import retrofit2.http.GET

interface IndonesiaCovidService {
    @GET("indonesia")
    fun getCovidData(): Call<List<ApiCovidIndonesiaItem>>
}