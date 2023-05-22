package com.flexath.grocery.activities

import android.app.ProgressDialog.show
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

class MainActivity : BaseActivity(), MainView {

    private lateinit var mAdapter: GroceryAdapter
    private lateinit var mPresenter: MainPresenter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        setUpPresenter()
        setUpRecyclerView()

        setUpActionListeners()

        mPresenter.onUiReady(this)
    }

    private fun setUpPresenter() {
        mPresenter = getPresenter<MainPresenterImpl, MainView>()
    }

    private fun setUpActionListeners() {
        binding.fab.setOnClickListener {
            GroceryDialogFragment.newFragment()
                .show(supportFragmentManager, GroceryDialogFragment.TAG_ADD_GROCERY_DIALOG)
        }
    }

    private fun setUpRecyclerView() {
        mAdapter = GroceryAdapter(mPresenter)
        binding.rvGroceries.adapter = mAdapter
        binding.rvGroceries.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
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
        bundle.putString(GroceryDialogFragment.BUNDLE_NAME,name)
        bundle.putString(GroceryDialogFragment.BUNDLE_DESCRIPTION,description)
        bundle.putInt(GroceryDialogFragment.BUNDLE_AMOUNT,amount)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, GroceryDialogFragment.TAG_ADD_GROCERY_DIALOG)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_LONG).show()
    }
}