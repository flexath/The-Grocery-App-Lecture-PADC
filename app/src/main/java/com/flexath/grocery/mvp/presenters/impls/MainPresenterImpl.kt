package com.flexath.grocery.mvp.presenters.impls

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.flexath.grocery.data.models.GroceryModelImpl
import com.flexath.grocery.data.vos.GroceryVO
import com.flexath.grocery.mvp.presenters.AbstractBasePresenter
import com.flexath.grocery.mvp.presenters.MainPresenter
import com.flexath.grocery.mvp.views.MainView

class MainPresenterImpl : MainPresenter, AbstractBasePresenter<MainView>() {

    private val mGroceryModel = GroceryModelImpl

    private var mGroceryFromFileUpload: GroceryVO? = null

    override fun onTapAddGrocery(name: String, description: String, amount: Int) {
        mGroceryModel.addGrocery(name, description, amount,"")
    }

    override fun onPhotoTaken(bitmap: Bitmap) {
        mGroceryFromFileUpload?.let { grocery ->
            mGroceryModel.uploadImageAndUpdateGrocery(bitmap, grocery)
        }
    }

    override fun onUiReady(owner: LifecycleOwner) {
        mGroceryModel.getGroceries(
            onSuccess = {
                mView.showGroceryData(it)
            },
            onFailure = {
                mView.showErrorMessage(it)
            }
        )
    }

    override fun onTapDeleteGrocery(name: String) {
        mGroceryModel.removeGrocery(name)
    }

    override fun onTapEditGrocery(name: String, description: String, amount: Int) {
        mView.showGroceryDialog(name, description, amount)
    }

    override fun onTapFileUpload(grocery: GroceryVO) {
        mGroceryFromFileUpload = grocery
        mView.showGallery()
    }
}