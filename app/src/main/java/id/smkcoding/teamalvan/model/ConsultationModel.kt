package id.smkcoding.teamalvan.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Konsultasi")
data class ConsultationModel(
    @PrimaryKey val IDuser : String,
    val Time : String,
    val Text : String,
    val Idpertanyaan : String,
    val Jenis : String

){
    constructor(): this("","","","","")
}