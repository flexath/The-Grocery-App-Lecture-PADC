package com.flexath.grocery.data.models

import android.graphics.Bitmap
import com.flexath.grocery.data.vos.GroceryVO
import com.flexath.grocery.network.FirebaseApi
import com.flexath.grocery.network.remoteconfig.FirebaseRemoteConfigManager

interface GroceryModel {

    var mFirebaseApi : FirebaseApi
    var mFirebaseRemoteConfig : FirebaseRemoteConfigManager

    fun getGroceries(onSuccess: (List<GroceryVO>) -> Unit, onFailure: (String) -> Unit)
    fun addGrocery(name:String,description:String,amount:Int,image:String)
    fun removeGrocery(name: String)
    fun uploadImageAndUpdateGrocery(image : Bitmap,grocery: GroceryVO)

    fun setUpRemoteConfigWithDefaultValues()
    fun fetchRemoteConfigs()
    fun getAppNameFromRemoteConfig() : String
    fun getRecyclerViewLayoutNumber() : Long
}