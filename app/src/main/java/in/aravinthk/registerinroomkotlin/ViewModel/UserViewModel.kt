package `in`.aravinthk.registerinroomkotlin.ViewModel

import androidx.lifecycle.ViewModel
import `in`.aravinthk.registerinroomkotlin.DataBase.Entity.UserEntity
import `in`.aravinthk.registerinroomkotlin.DataBase.UserRepository

class UserViewModel(
    private val repository: UserRepository
): ViewModel() {

    suspend fun insertUser(user: UserEntity) = repository.insertUser(user)

    suspend fun updateUser(user_name :  String, user_gender: String, user_email: String, user_phone: Int, id: Int) = repository.updateUser(user_name,user_gender,user_email,user_phone,id)

    suspend fun deleteUser(user: UserEntity) = repository.deleteUser(user)

    suspend fun deleteUserById(id: Int) = repository.deleteUserById(id)

    suspend fun clearUser() = repository.clearUser()

    fun getAllUsers() = repository.getAllUsers()
}