package com.flexath.grocery.network

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import com.flexath.grocery.data.vos.GroceryVO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID

@SuppressLint("StaticFieldLeak")
object CloudFirestoreFirebaseApiImpl : FirebaseApi {

    private var database: FirebaseFirestore = Firebase.firestore
    private val storageRef = FirebaseStorage.getInstance().reference

    override fun getGroceries(
        onSuccess: (groceries: List<GroceryVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
//        database.collection("groceries")
//            .get()
//            .addOnSuccessListener {
//                val groceryList: MutableList<GroceryVO> = arrayListOf()
//                for(document in it.documents) {
//                    val data = document.data
//                    val name = data?.get("name") as String
//                    val description = data["description"] as String
//                    val amount = (data["amount"] as Long).toInt()
//                    val grocery = GroceryVO(name,description,amount)
//                    groceryList.add(grocery)
//                }
//                onSuccess(groceryList)
//            }
//            .addOnFailureListener {
//                onFailure(it.localizedMessage ?: "Check Internet Connection")
//            }

        database.collection("groceries")
            .addSnapshotListener { value, error ->
                error?.let {
                    onFailure(it.localizedMessage ?: "Check Internet Connection")
                } ?: run {
                    val groceryList: MutableList<GroceryVO> = arrayListOf()
                    val documentList = value?.documents ?: arrayListOf()
                    for (document in documentList) {
                        val data = document.data
                        val name = data?.get("name") as String
                        val description = data["description"] as String
                        val amount = (data["amount"] as Long).toInt()
                        val image = data["image"] as String?
                        val grocery = GroceryVO(name, description, amount,image)
                        groceryList.add(grocery)
                    }
                    onSuccess(groceryList)
                }
            }
    }

    override fun addGrocery(name: String, description: String, amount: Int,image:String) {

        val groceryMap = hashMapOf(
            "name" to name,
            "description" to description,
            "amount" to amount.toLong(),
            "image" to image
        )

        database.collection("groceries")
            .document(name)
            .set(groceryMap)
            .addOnSuccessListener {
                Log.i("FirebaseCall", "Successfully Added")
            }.addOnFailureListener {
                Log.i("FirebaseCall", "Failed Added")
            }
    }

    override fun removeGrocery(name: String) {
        database.collection("groceries")
            .document(name)
            .delete()
            .addOnSuccessListener {
                Log.i("FirebaseCall", "Successfully deleted")
            }.addOnFailureListener {
                Log.i("FirebaseCall", "Failed deletion")
            }
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
            addGrocery(
                grocery.name ?: "",
                grocery.description ?: "",
                grocery.amount ?: 0,
                imageUrl ?: ""
            )
        }
    }
}