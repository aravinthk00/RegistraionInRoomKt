package `in`.aravinthk.registerinroomkotlin.Adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import `in`.aravinthk.registerinroomkotlin.DataBase.Entity.UserEntity
import `in`.aravinthk.registerinroomkotlin.Listener.ItemListener
import `in`.aravinthk.registerinroomkotlin.R

class UserListAdapter (
    val userDataList: List<UserEntity>,
    val context: Context,
    val listener : ItemListener
        ): RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    class UserViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {
        val user_id = itemView.findViewById<TextView>(R.id.userId)
        val user_name = itemView.findViewById<TextView>(R.id.userName)
        val user_email = itemView.findViewById<TextView>(R.id.userEmail)
        val user_phone = itemView.findViewById<TextView>(R.id.userPhone)
        val user_gender = itemView.findViewById<TextView>(R.id.userGender)
        val user_iamge = itemView.findViewById<ShapeableImageView>(R.id.userImageView)
        val user_update = itemView.findViewById<AppCompatButton>(R.id.user_update_button)
        val user_delete = itemView.findViewById<AppCompatButton>(R.id.user_delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.users_list_item, parent, false)
        return UserViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return userDataList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.user_id.text = userDataList[position].user_id.toString()
        holder.user_name.text = userDataList[position].user_name.toString()
        holder.user_email.text = userDataList.get(position).user_email.toString()
        holder.user_phone.text = userDataList.get(position).phone_no.toString()
        if(userDataList[position].gender.toString().equals("Male")){
            holder.user_gender.text = userDataList[position].gender.toString()
            holder.user_gender.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_male_24,0,0,0)
        }else if(userDataList[position].gender.toString().equals("Female")){
            holder.user_gender.text = userDataList[position].gender.toString()
            holder.user_gender.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_female_24,0,0,0)
        }else{
            holder.user_gender.text = userDataList[position].gender.toString()
            holder.user_gender.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_hide_source_24,0,0,0)
        }
        holder.user_iamge.setShapeAppearanceModel(
            holder.user_iamge.getShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(30F)
                .build()
        )

        if(userDataList[position].user_image != null) {
            try {
                val imageUri = Uri.parse(userDataList[position].user_image)
                Log.d("adapter", "onBindViewHolder: " + imageUri)
                holder.user_iamge.setImageURI(imageUri)

//                Glide.with(context)
//                    .load(imageUri) // Uri of the picture
//                    //.transform(new CircleTransform(..))
//                    //.listener()
//                    .into(holder.user_iamge)
            }catch (ex:NullPointerException){
                Log.d("adapter", "onBindViewHolder: " + ex)
            }
        }
        else{
            holder.user_iamge.setBackgroundResource(R.drawable.baseline_person_add_24)
        }
        holder.user_update.setOnClickListener {
            userDataList[position].user_id?.let { it1 -> listener.itemUpdate(it1, position) }
        }

        holder.user_delete.setOnClickListener {
            userDataList[position].user_id?.let { it1 -> listener.itemDelete(it1) }
        }

//        holder.itemView.setOnClickListener{
//            listener.itemClicked(position)
//        }
    }
}