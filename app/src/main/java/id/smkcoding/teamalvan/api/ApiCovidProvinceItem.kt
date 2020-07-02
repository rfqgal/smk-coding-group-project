package id.smkcoding.teamalvan.api


import com.google.gson.annotations.SerializedName

data class ApiCovidProvinceItem(
    @SerializedName("attributes")
    val attributes: Attributes
)