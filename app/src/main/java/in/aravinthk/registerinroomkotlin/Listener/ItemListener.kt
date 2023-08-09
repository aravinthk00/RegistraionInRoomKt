package `in`.aravinthk.registerinroomkotlin.Listener

import java.text.FieldPosition

interface ItemListener {

    fun itemDelete(deleteByTd: Int)

    fun itemUpdate(updateById: Int, position: Int)

    fun itemClicked(position: Int)
}