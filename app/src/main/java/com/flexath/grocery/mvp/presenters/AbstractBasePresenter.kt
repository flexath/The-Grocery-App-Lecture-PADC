package com.flexath.grocery.mvp.presenters

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.flexath.grocery.mvp.views.BaseView
import com.google.firebase.analytics.FirebaseAnalytics

abstract class AbstractBasePresenter<T: BaseView> : BasePresenter<T>, ViewModel() {

    protected lateinit var mView : T

    override fun initPresenter(view: T){
        mView = view
    }

    fun sendEventsToFirebaseAnalytics(
        context: Context,
        eventName: String,
        key: String = "",
        value: String = ""
    ) {
        if(key.isNotEmpty() && value.isNotEmpty()) {
            FirebaseAnalytics.getInstance(context).logEvent(eventName,buildBundle(key,value))
        } else {
            FirebaseAnalytics.getInstance(context).logEvent(eventName,null)
        }
    }

    private fun buildBundle(key: String, value: String): Bundle {
        val bundle = Bundle()
        bundle.putString(key,value)
        return bundle
    }
}