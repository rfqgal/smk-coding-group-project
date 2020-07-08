package id.smkcoding.teamalvan.model

import androidx.annotation.Nullable
import androidx.room.Entity

@Entity(tableName = "tb_replies")
data class ConsultationRepliesModel(
    val idreply: String,
    val idbalasan: String,
    val iduser: String,
    val text: String,
    val time: String,
    val token: String,
    @Nullable val target: String
) {
    constructor(): this("", "","", "", "", "", "")
}