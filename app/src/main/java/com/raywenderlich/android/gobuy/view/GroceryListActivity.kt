/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.gobuy.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raywenderlich.android.gobuy.R
import com.raywenderlich.android.gobuy.databinding.ActivityGroceryListBinding
import com.raywenderlich.android.gobuy.model.GroceryItem
import com.raywenderlich.android.gobuy.viewmodel.GroceryListViewModel

/**
 * Main Screen
 */
class GroceryListActivity : AppCompatActivity(), NewItemDialogFragment.NewItemDialogListener {

    lateinit var viewModel: GroceryListViewModel

    // TODO: remove the view items and change them for the binding object
    private lateinit var binding: ActivityGroceryListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        // TODO: remove setContentView after data binding is added
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grocery_list)

        viewModel = ViewModelProviders.of(this).get(GroceryListViewModel::class.java)

        // TODO: initialize the binding object and remove the view invocations

        // TODO: associate the layout manager, adapter and button listener with the binding object
        binding.rvGroceryList.layoutManager = LinearLayoutManager(this)

        binding.rvGroceryList.adapter = GroceryAdapter(
            viewModel.groceryListItems,
            this,
            ::editGroceryItem,
            ::deleteGroceryItem
        )

        binding.addItemButton.setOnClickListener {
            addGroceryItem()
        }
    }

    private fun addGroceryItem() {
        val newFragment = NewItemDialogFragment.newInstance(R.string.add_new_item_dialog_title, null)
        newFragment.show(supportFragmentManager, "newItem")
    }

    private fun editGroceryItem(position: Int) {
        Log.d("GoBuy", "edit")
        val newFragment = NewItemDialogFragment.newInstance(
            R.string.edit_item_dialog_title,
            position
        )
        newFragment.show(supportFragmentManager, "newItem")
    }

    private fun deleteGroceryItem(position: Int) {
        Log.d("GoBuy", "delete")
        viewModel.removeItem(position)
        binding.totalAmount = viewModel.getTotal().toString()
        // TODO: call the adapter from the binding object
        binding.rvGroceryList.adapter?.notifyDataSetChanged()
    }

    override fun onDialogPositiveClick(
        dialog: DialogFragment,
        item: GroceryItem,
        isEdit: Boolean,
        position: Int?
    ) {
        if (!isEdit) {
            viewModel.groceryListItems.add(item)
        } else {
            viewModel.updateItem(position!!, item)
            // TODO: call the adapter from the binding object
            binding.rvGroceryList.adapter?.notifyDataSetChanged()
        }

        // TODO: update the total amount and addItemButton with the binding
        binding.totalAmount = String.format("%.2f", viewModel.getTotal())
        Snackbar.make(binding.addItemButton, "Item Added Successfully", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // TODO: update the addItemButton with the binding
        Snackbar.make(binding.addItemButton, "Nothing Added", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }
}
