package id.smkcoding.teamalvan.api


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ApiCovidIndonesiaItem(
    @SerializedName("meninggal")
    val meninggal: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("positif")
    val positif: String,
    @SerializedName("sembuh")
    val sembuh: String
) : Serializable