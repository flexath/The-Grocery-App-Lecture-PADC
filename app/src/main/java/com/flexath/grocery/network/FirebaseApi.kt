package com.flexath.grocery.network

import com.flexath.grocery.data.vos.GroceryVO

interface FirebaseApi {
    fun getGroceries(onSuccess: (groceries: List<GroceryVO>) -> Unit, onFailure: (String) -> Unit)
    fun addGrocery(name:String,description:String,amount:Int)
    fun removeGrocery(name: String)
}