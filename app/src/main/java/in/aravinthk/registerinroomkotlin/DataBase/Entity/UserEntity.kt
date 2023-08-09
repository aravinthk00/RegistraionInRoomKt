package `in`.aravinthk.registerinroomkotlin.DataBase.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User_Table")
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    val user_id: Int?,
    val user_name: String?,
    val phone_no : Int?,
    val user_image: String?,
    val gender : String?,
    val user_email : String?

)
