package com.flexath.grocery.data.models

import com.flexath.grocery.data.vos.GroceryVO
import com.flexath.grocery.network.FirebaseApi

interface GroceryModel {

    var mFirebaseApi : FirebaseApi

    fun getGroceries(onSuccess: (List<GroceryVO>) -> Unit, onFailure: (String) -> Unit)
    fun addGrocery(name:String,description:String,amount:Int)
    fun removeGrocery(name: String)
}