package id.smkcoding.teamalvan.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_users")
data class UsersModel(
    var photo: String,
    var email: String,
    var name: String,
    var level: String,
    @PrimaryKey var key: String
){
    constructor() : this("","","","",""
    )
}

