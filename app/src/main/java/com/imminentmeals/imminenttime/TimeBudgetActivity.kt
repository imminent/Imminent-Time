package com.imminentmeals.imminenttime

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText

import kotlinx.android.synthetic.main.activity_time_budget.*

class TimeBudgetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_budget)
        setSupportActionBar(toolbar)

        fab.setOnClickListener(this::openAddBudget)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_time_budget, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openAddBudget(@Suppress("UNUSED_PARAMETER") ignored: View) {
        AlertDialog.Builder(this)
                .setView(R.layout.dialog_add_budget)
                .setPositiveButton(R.string.button_add, this::add)
                .show()
                .also { dialog ->
                    with(dialog.getButton(AlertDialog.BUTTON_POSITIVE)) {
                        isEnabled = false
                        dialog.findViewById<EditText>(android.R.id.input)?.addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(text: Editable?) {
                                isEnabled = text.isNullOrBlank().not()
                            }

                            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
                            }

                            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {

                            }
                        })
                    }
                }
    }

    private fun add(dialog: DialogInterface, @Suppress("UNUSED_PARAMETER") ignored: Int) {
        dialog.dismiss()
    }
}
