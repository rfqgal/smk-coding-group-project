package id.smkcoding.teamalvan.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_consultation")
data class ConsultationModel(
    val IDuser : String,
    val Time : String,
    val Text : String,
    val Idpertanyaan : String,
    val Jenis : String,
    val token : String

){
    constructor(): this("","","","","","")
}