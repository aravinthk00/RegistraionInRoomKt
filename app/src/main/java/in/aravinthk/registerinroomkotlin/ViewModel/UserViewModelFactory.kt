package `in`.aravinthk.registerinroomkotlin.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import `in`.aravinthk.registerinroomkotlin.DataBase.UserRepository

class UserViewModelFactory(
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        try {
            val constructor = modelClass.getDeclaredConstructor(UserRepository::class.java)
            return constructor.newInstance(repository)
        } catch (e: Exception) {
            Log.e("UserViewModelFactory", e.message.toString() )
        }
        return super.create(modelClass)
    }
}