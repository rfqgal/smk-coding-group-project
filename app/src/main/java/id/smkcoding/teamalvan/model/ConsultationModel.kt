package id.smkcoding.teamalvan.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_consultation")
data class ConsultationModel(
    val idpertanyaan : String,
    val iduser : String,
    val jenis : String,
    val text : String,
    val time : String,
    val token : String
){
    constructor(): this("","","","","","")
}