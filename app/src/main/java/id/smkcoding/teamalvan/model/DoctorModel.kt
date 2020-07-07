package id.smkcoding.teamalvan.model

import android.net.Uri

data class DoctorModel(
    var name: String,
    var gender: String,
    var category: String,
    var doctorID: String,
    var identity: Uri,
    var str: Uri
)