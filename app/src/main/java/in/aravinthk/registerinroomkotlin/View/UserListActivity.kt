package `in`.aravinthk.registerinroomkotlin.View

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import `in`.aravinthk.registerinroomkotlin.Adapter.UserListAdapter
import `in`.aravinthk.registerinroomkotlin.DataBase.Entity.UserEntity
import `in`.aravinthk.registerinroomkotlin.DataBase.UserDataBase
import `in`.aravinthk.registerinroomkotlin.DataBase.UserRepository
import `in`.aravinthk.registerinroomkotlin.Listener.ItemListener
import `in`.aravinthk.registerinroomkotlin.R
import `in`.aravinthk.registerinroomkotlin.ViewModel.UserViewModel
import `in`.aravinthk.registerinroomkotlin.ViewModel.UserViewModelFactory
import `in`.aravinthk.registerinroomkotlin.databinding.ActivityUserListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListActivity : AppCompatActivity(), ItemListener {

    private lateinit var viewModel: UserViewModel
    private lateinit var userDatabase: UserDataBase
    private lateinit var repository: UserRepository
    private lateinit var factory: UserViewModelFactory

    lateinit var binding: ActivityUserListBinding

    var userDataList: List<UserEntity> = ArrayList<UserEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //@todo bad practice because boilerplate code, but we'll be change this later using DI.
        userDatabase = UserDataBase(this)
        repository = UserRepository(userDatabase)
        factory = UserViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        showData()
        binding.addUserButton.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        binding.clearUsersButton.setOnClickListener {
            clearUser()
        }
    }

    //READ (show data)
    private fun showData() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getAllUsers().observe(this@UserListActivity, { users ->
                //do action here, and now you have data in list (notes)
                // Log.d("TAG", "showData: " + users.get(0).user_name)
                if (users != null && !users.isEmpty()) {
                    userDataList = users
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.noUsersDataText.visibility = View.GONE
                    setAdapter()
                    for (s in userDataList) {
                        Log.d("TAG", "getData: " + s.user_name)
                    }
                } else {
                    binding.recyclerView.visibility = View.GONE
                    binding.noUsersDataText.visibility = View.VISIBLE
                }
            })
        }
    }

    fun setAdapter() {
        val recycler_view = binding.recyclerView
        if (userDataList.size > 0) {

            recycler_view.visibility = View.VISIBLE
            binding.noUsersDataText.visibility = View.GONE
            recycler_view.layoutManager = LinearLayoutManager(this)

            recycler_view.adapter = UserListAdapter(userDataList, this, this)

            recycler_view.addItemDecoration(
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            )
        }
    }

    //UPDATE (update data)
    private fun updateData(updateById: Int, position: Int) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setTitle(R.string.dialog_update_top_text)
        dialog.setContentView(R.layout.custom_dialog_layout)
        val dialogUserId = dialog.findViewById(R.id.userDialogIdEdit) as TextView
        val dialogUserName = dialog.findViewById(R.id.userDialogNameEdit) as TextInputEditText
        val dialogUserEmail = dialog.findViewById(R.id.userDialogEmailEdit) as TextInputEditText
        val dialogUserPhone = dialog.findViewById(R.id.userDialogPhoneEdit) as TextInputEditText
        val dialogUserGender = dialog.findViewById(R.id.dialog_gender_options) as RadioGroup
        val dialogUpdateUpdate = dialog.findViewById(R.id.userDialogUpdateButton) as AppCompatButton

        dialogUserId.text = updateById.toString()

        dialogUpdateUpdate.setOnClickListener {
            try {
                val name: String = dialogUserName.text.toString()
                val email: String = dialogUserEmail.text.toString()

                val phone: Int = Integer.parseInt(dialogUserPhone.text.toString())
                val selectedGender = dialogUserGender.checkedRadioButtonId
                val gender = when (selectedGender) {
                    R.id.option_male -> "Male"
                    R.id.option_female -> "Female"
                    else -> "Other"
                }
                val prev_image = userDataList[position].user_image

                CoroutineScope(Dispatchers.Main).launch {
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.updateUser(name, gender, email, phone, updateById).also {
                            Log.d("TAG", "updateData: $name  $gender, $email, $phone, $updateById")
                        }
                    }
                }
            } catch (e: NumberFormatException) {

            }

        }

        dialog.show()
    }

    //DELETE(delete one row note)
    private fun deleteuser(user: UserEntity) {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.deleteUser(user).also {
                //do action here
            }
        }
    }

    //DELETE(delete by id note)
    private fun deleteUserById(id: Int) {
        //val id = 1 //sample id

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.deleteUserById(id).also {
                //do action here
            }
        }
    }

    //DELETE or Clear All Note
    private fun clearUser() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.clearUser().also {
                //do action here
            }
        }
    }

    override fun itemDelete(deleteByTd: Int) {
        Toast.makeText(applicationContext, "$deleteByTd", Toast.LENGTH_SHORT).show()
        deleteUserById(deleteByTd)
        //deleteuser(userDataList[deleteByTd])
    }

    override fun itemUpdate(updateById: Int, position: Int) {
        Toast.makeText(applicationContext, "$updateById", Toast.LENGTH_SHORT).show()
        updateData(updateById, position)
    }

    override fun itemClicked(position: Int) {
        Toast.makeText(applicationContext, "$position", Toast.LENGTH_SHORT).show()

    }

}