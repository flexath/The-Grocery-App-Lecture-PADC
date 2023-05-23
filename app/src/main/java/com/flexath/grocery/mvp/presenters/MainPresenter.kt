package com.flexath.grocery.mvp.presenters

import android.graphics.Bitmap
import com.flexath.grocery.delegates.GroceryViewItemActionDelegate
import com.flexath.grocery.mvp.views.MainView

interface MainPresenter : BasePresenter<MainView> , GroceryViewItemActionDelegate {
    fun onTapAddGrocery(name: String, description: String, amount: Int)
    fun onPhotoTaken(bitmap: Bitmap)

    fun showUserName() : String
}
