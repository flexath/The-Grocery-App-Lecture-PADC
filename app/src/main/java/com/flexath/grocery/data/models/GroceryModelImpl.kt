package com.flexath.grocery.data.models

import com.flexath.grocery.data.vos.GroceryVO
import com.flexath.grocery.network.CloudFirestoreFirebaseApiImpl
import com.flexath.grocery.network.FirebaseApi
import com.flexath.grocery.network.RealtimeDatabaseFirebaseApiImpl

object GroceryModelImpl : GroceryModel {
//    override var mFirebaseApi: FirebaseApi = RealtimeDatabaseFirebaseApiImpl

    override var mFirebaseApi: FirebaseApi = CloudFirestoreFirebaseApiImpl

    override fun getGroceries(onSuccess: (List<GroceryVO>) -> Unit, onFailure: (String) -> Unit) {
        mFirebaseApi.getGroceries(onSuccess, onFailure)
    }

    override fun addGrocery(name: String, description: String, amount: Int) {
        mFirebaseApi.addGrocery(name,description,amount)
    }

    override fun removeGrocery(name: String) {
        mFirebaseApi.removeGrocery(name)
    }
}