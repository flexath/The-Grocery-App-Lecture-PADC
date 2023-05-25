package com.flexath.grocery.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.flexath.grocery.R
import com.flexath.grocery.adapters.GroceryAdapter
import com.flexath.grocery.data.vos.GroceryVO
import com.flexath.grocery.databinding.ActivityMainBinding
import com.flexath.grocery.dialogs.GroceryDialogFragment
import com.flexath.grocery.mvp.presenters.MainPresenter
import com.flexath.grocery.mvp.presenters.impls.MainPresenterImpl
import com.flexath.grocery.mvp.views.MainView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import java.io.IOException

class MainActivity : BaseActivity(), MainView {

    private lateinit var mAdapter: GroceryAdapter
    private lateinit var mPresenter: MainPresenter

    private lateinit var binding: ActivityMainBinding

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        setUpPresenter()

        setUpActionListeners()

        mPresenter.onUiReady(this, this)

        addCrashButton()

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener {
                val deepLink = it.link
                deepLink?.let { deepLinkUri ->
                    Log.d("deepLink", deepLinkUri.toString())
                }
            }
            .addOnFailureListener { error ->
                error.localizedMessage?.let {
                    Log.d("error", it)
                }
            }
    }

    private fun setUpPresenter() {
        mPresenter = getPresenter<MainPresenterImpl, MainView>()
    }

    private fun setUpActionListeners() {
        binding.fab.setOnClickListener {
            GroceryDialogFragment.newFragment()
                .show(supportFragmentManager, GroceryDialogFragment.TAG_ADD_GROCERY_DIALOG)
        }

        val username = "Welcome ${mPresenter.showUserName()}"
        binding.tvUserName.text = username
    }

    private fun setUpRecyclerView(number: Long) {
        mAdapter = GroceryAdapter(mPresenter, number)
        binding.rvGroceries.adapter = mAdapter
    }

    private fun addCrashButton() {
        binding.crashButton.setOnClickListener {
            throw RuntimeException("Test Crash")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showGroceryData(groceryList: List<GroceryVO>) {
        mAdapter.setNewData(groceryList)
    }

    override fun showGroceryDialog(name: String, description: String, amount: Int) {
        val dialog = GroceryDialogFragment.newFragment()
        val bundle = Bundle()
        bundle.putString(GroceryDialogFragment.BUNDLE_NAME, name)
        bundle.putString(GroceryDialogFragment.BUNDLE_DESCRIPTION, description)
        bundle.putInt(GroceryDialogFragment.BUNDLE_AMOUNT, amount)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, GroceryDialogFragment.TAG_ADD_GROCERY_DIALOG)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_LONG).show()
    }

    override fun displayToolbarTitle(title: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = title
    }

    override fun getRecyclerViewLayoutNumber(number: Long) {
        setUpRecyclerView(number)
        if (number == 0L) {
            binding.rvGroceries.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        } else {
            binding.rvGroceries.layoutManager =
                GridLayoutManager(this, 2)
        }
    }

    override fun showGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Upload Image"), 100)
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            val filePath = data.data

            try {
                filePath?.let { fileUrl ->
                    if (Build.VERSION.SDK_INT >= 29) {
                        val source = ImageDecoder.createSource(this.contentResolver, fileUrl)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        mPresenter.onPhotoTaken(bitmap)
                    } else {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            applicationContext.contentResolver, fileUrl
                        )
                        mPresenter.onPhotoTaken(bitmap)
                    }
                }


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}