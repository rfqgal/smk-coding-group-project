package id.smkcoding.teamalvan.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "tb_articles")
data class ArticlesModel(
    var id_dokter: String,
    var name_dokter: String,
    var photo_dokter: String,
    var time: String,
    var title: String,
    var caption: String,
    var category: String,
    var cover: String,
    @PrimaryKey var key: String
){
    constructor() : this("","","","","","","","",""
    )
}