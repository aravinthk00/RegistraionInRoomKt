package `in`.aravinthk.registerinroomkotlin.View

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import `in`.aravinthk.registerinroomkotlin.DataBase.Entity.UserEntity
import `in`.aravinthk.registerinroomkotlin.DataBase.UserDataBase
import `in`.aravinthk.registerinroomkotlin.DataBase.UserRepository
import `in`.aravinthk.registerinroomkotlin.R
import `in`.aravinthk.registerinroomkotlin.ViewModel.UserViewModel
import `in`.aravinthk.registerinroomkotlin.ViewModel.UserViewModelFactory
import `in`.aravinthk.registerinroomkotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: UserViewModel
    private lateinit var userDatabase: UserDataBase
    private lateinit var repository: UserRepository
    private lateinit var factory: UserViewModelFactory

    lateinit var mainBinding: ActivityMainBinding

    var userDataList: List<UserEntity> = ArrayList<UserEntity>()
    private var imageUri: Uri? = null
    private var selectedImagePath: String? = null

    companion object {
        private val IMAGE_CHOOSE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        //@todo bad practice because boilerplate code, but we'll be change this later using DI.
        userDatabase = UserDataBase(this)
        repository = UserRepository(userDatabase)
        factory = UserViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        mainBinding.submitButton.setOnClickListener {
            insertData()
        }

        mainBinding.userDataViewText.setOnClickListener {
            val mainIntent = Intent(this, UserListActivity::class.java)
            startActivity(mainIntent)
        }

        mainBinding.selectImageButton.setOnClickListener {
            Log.d("TAG", "insertData: file")
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(
                    intent,
                    "Select Picture"
                ), IMAGE_CHOOSE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_CHOOSE) {
            imageUri = data?.data
            selectedImagePath = imageUri?.let { getRealPathFromURI(it, this) }
        }
    }

    fun getRealPathFromURI(uri: Uri, context: Context): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.filesDir, name)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable: Int = inputStream?.available() ?: 0
            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream?.read(buffers).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream?.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)

        } catch (e: java.lang.Exception) {
            Log.e("Exception", e.message!!)
        }
        return file.path
    }

    //CREATE (INSERT)
    private fun insertData() {
        if (mainBinding.userNameEdit.text.toString().isEmpty()) {
            mainBinding.userNameEdit.setError("Ender valid detail")
            return
        }
        if (mainBinding.userEmailEdit.text.toString().isEmpty()) {
            mainBinding.userEmailEdit.setError("Ender valid detail")
            return
        }
        if (mainBinding.userPhoneEdit.text.toString().isEmpty()) {
            mainBinding.userPhoneEdit.setError("Ender valid detail")
            return
        }
        val name = mainBinding.userNameEdit.text.toString()
        val email = mainBinding.userEmailEdit.text.toString()
        val phone: Int = Integer.parseInt(mainBinding.userPhoneEdit.text.toString())
        val selectedGender = mainBinding.genderOptions.checkedRadioButtonId
        val gender = when (selectedGender) {
            R.id.option_male -> "Male"
            R.id.option_female -> "Female"
            else -> "Other"
        }

        val image = selectedImagePath
        Log.d("TAG", "selectedImagePath: " + image)
        val user = UserEntity(
            user_id = null,
            user_name = name,
            phone_no = phone,
            user_image = image,
            user_email = email,
            gender = gender
        ) //why id null? because id is auto generate
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.insertUser(user).also {
                //do action here
                Log.d("TAG", "insertData: ")
                Toast.makeText(
                    applicationContext,
                    "$name Added Successfully!..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}

