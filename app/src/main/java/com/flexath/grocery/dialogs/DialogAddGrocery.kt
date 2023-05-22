package com.flexath.grocery.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.flexath.grocery.R
import com.flexath.grocery.databinding.DialogAddGroceryBinding
import com.flexath.grocery.mvp.presenters.MainPresenter
import com.flexath.grocery.mvp.presenters.impls.MainPresenterImpl

class GroceryDialogFragment : DialogFragment() {

    private lateinit var binding:DialogAddGroceryBinding

    companion object {
        const val TAG_ADD_GROCERY_DIALOG = "TAG_ADD_GROCERY_DIALOG"
        const val BUNDLE_NAME = "BUNDLE_NAME"
        const val BUNDLE_DESCRIPTION = "BUNDLE_DESCRIPTION"
        const val BUNDLE_AMOUNT = "BUNDLE_AMOUNT"

        fun newFragment(): GroceryDialogFragment {
            return GroceryDialogFragment()
        }
    }

    private lateinit var mPresenter: MainPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddGroceryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPresenter()

        binding.etGroceryName.setText(arguments?.getString(BUNDLE_NAME))
        binding.etDescription.setText(arguments?.getString(BUNDLE_DESCRIPTION))
        binding.etAmount.setText(arguments?.getInt(BUNDLE_AMOUNT).toString())

        binding.btnAddGrocery.setOnClickListener {
            mPresenter.onTapAddGrocery(
                binding.etGroceryName.text.toString(),
                binding.etDescription.text.toString(),
                binding.etAmount.text.toString().toInt()
            )
            dismiss()
        }
    }

    private fun setUpPresenter() {
        activity?.let {
            mPresenter = ViewModelProviders.of(it).get(MainPresenterImpl::class.java)
        }
    }
}