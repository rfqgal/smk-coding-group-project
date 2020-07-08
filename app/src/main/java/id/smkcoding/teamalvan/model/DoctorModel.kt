package id.smkcoding.teamalvan.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_calon_dokter")
data class DoctorModel(
    var name: String,
    var gender: String,
    var category: String,
    @PrimaryKey var doctorID: String,
    var identity: String,
    var str: String
){
    constructor(): this("","","","","","")
}