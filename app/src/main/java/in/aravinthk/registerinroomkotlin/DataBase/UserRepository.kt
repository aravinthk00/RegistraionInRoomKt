package `in`.aravinthk.registerinroomkotlin.DataBase

import androidx.lifecycle.LiveData
import `in`.aravinthk.registerinroomkotlin.DataBase.Entity.UserEntity

class UserRepository(
    private val userDatabase: UserDataBase
) {

    suspend fun insertUser(user: UserEntity) = userDatabase.getUserDao().insertUser(user)

    suspend fun updateUser(user_name :  String, user_gender: String, user_email: String, user_phone: Int, id: Int) = userDatabase.getUserDao().updateUser(user_name,user_gender,user_email,user_phone,id)

    suspend fun deleteUser(user: UserEntity) = userDatabase.getUserDao().deleteUser(user)

    suspend fun deleteUserById(id: Int) = userDatabase.getUserDao().deleteUserById(id)

    suspend fun clearUser() = userDatabase.getUserDao().clearUser()

    fun getAllUsers(): LiveData<List<UserEntity>> = userDatabase.getUserDao().getAllUsers()
}