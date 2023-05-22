package com.flexath.grocery.network

import android.graphics.Bitmap
import com.flexath.grocery.data.vos.GroceryVO

interface FirebaseApi {
    fun getGroceries(onSuccess: (groceries: List<GroceryVO>) -> Unit, onFailure: (String) -> Unit)
    fun addGrocery(name:String,description:String,amount:Int,image:String)
    fun removeGrocery(name: String)

    fun uploadImageAndEditGrocery(bitmap:Bitmap , grocery: GroceryVO)
}