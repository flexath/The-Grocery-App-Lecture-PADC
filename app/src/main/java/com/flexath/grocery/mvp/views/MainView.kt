package com.flexath.grocery.mvp.views

import com.flexath.grocery.data.vos.GroceryVO

interface MainView : BaseView {
    fun showGroceryData(groceryList : List<GroceryVO>)
    fun showGroceryDialog(name: String, description: String, amount: Int)
    fun showErrorMessage(message : String)
}