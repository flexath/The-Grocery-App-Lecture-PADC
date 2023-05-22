package com.flexath.grocery.network

import android.graphics.Bitmap
import android.util.Log
import com.flexath.grocery.data.vos.GroceryVO
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID

object RealtimeDatabaseFirebaseApiImpl : FirebaseApi {

//    private val database: DatabaseReference = Firebase.database.reference

    private var database: DatabaseReference

    private var storageRef = FirebaseStorage.getInstance().reference

    init {
        val databaseUrl =
            "https://grocery-app-9d93b-default-rtdb.asia-southeast1.firebasedatabase.app"
        database = FirebaseDatabase.getInstance(databaseUrl).reference
    }

    override fun getGroceries(
        onSuccess: (groceries: List<GroceryVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        database.child("groceries").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onFailure(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val groceryList = arrayListOf<GroceryVO>()
                snapshot.children.forEach { dataSnapShot ->
                    dataSnapShot.getValue(GroceryVO::class.java)?.let {
                        groceryList.add(it)
                    }
                }
                onSuccess(groceryList)
            }
        })
    }

    override fun addGrocery(name: String, description: String, amount: Int , image:String) {
        database.child("groceries").child(name).setValue(GroceryVO(name, description, amount,image))
    }

    override fun removeGrocery(name: String) {
        database.child("groceries").child(name).removeValue()
    }

    override fun uploadImageAndEditGrocery(bitmap: Bitmap, grocery: GroceryVO) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data = baos.toByteArray()

        val imageRef = storageRef.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Log.i("FileUpload","File uploaded failed")
        }.addOnSuccessListener {
            Log.i("FileUpload","File uploaded successful")
        }

        val urlTask = uploadTask.continueWithTask {
            return@continueWithTask imageRef.downloadUrl
        }.addOnCompleteListener {
            val imageUrl = it.result?.toString()
            addGrocery(grocery.name ?: "",grocery.description ?: "",grocery.amount ?: 0,imageUrl ?: "")
        }
    }
}