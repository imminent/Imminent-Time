package com.imminentmeals.imminenttime.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.TextView
import com.imminentmeals.imminenttime.R
import com.imminentmeals.imminenttime.repository.TimeBudget
import kotlinx.android.synthetic.main.activity_time_budget.*
import kotlinx.android.synthetic.main.content_time_budget.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class TimeBudgetActivity : AppCompatActivity() {
    lateinit var viewModel: TimeBudgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_budget)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(TimeBudgetViewModel::class.java)

        fab.setOnClickListener(this::openAddBudget)
        list.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
            val timeBudgets
                get() = viewModel.budgets.value

            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
                    parent?.let { TimeBudgetViewHolder(TextView(parent.context)) }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
                (holder as? TimeBudgetViewHolder)?.label = timeBudgets?.getOrNull(position)?.label ?: ""
            }

            override fun getItemCount(): Int = timeBudgets?.size ?: 0

            inner class TimeBudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                private val labelView = itemView as TextView
                var label: CharSequence = ""
                    set(value) {
                        labelView.text = value
                    }
            }
        }
        viewModel.budgets.observe(this, Observer { list.adapter.notifyDataSetChanged() })
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
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_budget, null)
        val input = view.findViewById<EditText>(android.R.id.input)
        AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(R.string.button_add, { dialog, _ ->
                    launch(UI) {
                        viewModel.addTimeBudget(TimeBudget(
                                label = input.text.toString()
                        ))
                        dialog.dismiss()
                    }
                })
                .show()
                .also { dialog ->
                    with(dialog.getButton(AlertDialog.BUTTON_POSITIVE)) {
                        isEnabled = false
                        input?.addTextChangedListener(object : TextWatcher {
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
}
