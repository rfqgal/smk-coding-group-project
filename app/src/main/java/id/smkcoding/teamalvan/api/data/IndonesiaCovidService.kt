package id.smkcoding.teamalvan.api.data

import id.smkcoding.teamalvan.api.ApiCovidIndonesiaItem
import id.smkcoding.teamalvan.api.ApiCovidProvinceItem
import retrofit2.Call
import retrofit2.http.GET

interface IndonesiaCovidService {
    @GET("indonesia")
    fun getCovidData(): Call<List<ApiCovidIndonesiaItem>>
    @GET("indonesia/provinsi")
    fun getCovidProvince(): Call<List<ApiCovidProvinceItem>>
}