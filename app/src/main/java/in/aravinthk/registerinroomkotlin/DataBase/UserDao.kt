package `in`.aravinthk.registerinroomkotlin.DataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import `in`.aravinthk.registerinroomkotlin.DataBase.Entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)


    @Query("UPDATE User_Table SET user_name = :user_name, gender = :user_gender, user_email = :user_email, phone_no = :user_phone WHERE user_id = :id")
    suspend fun updateUser(user_name :  String, user_gender: String, user_email: String, user_phone: Int, id: Int)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM User_Table ORDER BY user_id ASC")
    fun getAllUsers(): LiveData<List<UserEntity>>
    // why not use suspend ? because Room does not support LiveData with suspended functions.
    // LiveData already works on a background thread and should be used directly without using coroutines

    @Query("DELETE FROM User_Table")
    suspend fun clearUser()

    @Query("DELETE FROM User_Table WHERE user_id = :id") //you can use this too, for delete note by id.
    suspend fun deleteUserById(id: Int)
}