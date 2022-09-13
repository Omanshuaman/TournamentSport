package com.omanshuaman.tournamentsports

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.ExpandableListView.OnGroupExpandListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.omanshuaman.tournamentsports.adapters.MyExpandableListAdapter

class FormFillActivity : AppCompatActivity() {
    var groupList: MutableList<String>? = null
    var childList: MutableList<String>? = null
    var mobileCollection: MutableMap<String, List<String>?>? = null
    var expandableListView: ExpandableListView? = null
    var expandableListAdapter: ExpandableListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_fill)
        createGroupList()
        createCollection()
        val editText = findViewById<EditText>(R.id.edittext)
        expandableListView = findViewById(R.id.elvMobiles)
        expandableListAdapter = MyExpandableListAdapter(this, groupList!!, mobileCollection!!)
        expandableListView!!.setAdapter(expandableListAdapter)
        expandableListView!!.setOnGroupExpandListener(object : OnGroupExpandListener {
            var lastExpandedPosition = -1
            override fun onGroupExpand(i: Int) {
                if (lastExpandedPosition != -1 && i != lastExpandedPosition) {
                    expandableListView!!.collapseGroup(lastExpandedPosition)
                }
                lastExpandedPosition = i
            }
        })
        expandableListView!!.setOnChildClickListener(OnChildClickListener { expandableListView: ExpandableListView?, view: View?, i: Int, i1: Int, l: Long ->
            val selected = expandableListAdapter!!.getChild(i, i1).toString()
            Toast.makeText(applicationContext, "Selected: $selected", Toast.LENGTH_SHORT)
                .show()
            editText.append(selected)
            true
        })
    }

    private fun createCollection() {
        val samsungModels = arrayOf(
            "Samsung Galaxy M21", "Samsung Galaxy F41",
            "Samsung Galaxy M51", "Samsung Galaxy A50s"
        )
        val googleModels = arrayOf(
            "Pixel 4 XL", "Pixel 3a", "Pixel 3 XL", "Pixel 3a XL",
            "Pixel 2", "Pixel 3"
        )
        val redmiModels = arrayOf("Redmi 9i", "Redmi Note 9 Pro Max", "Redmi Note 9 Pro")
        val vivoModels = arrayOf("Vivo V20", "Vivo S1 Pro", "Vivo Y91i", "Vivo Y12")
        val nokiaModels = arrayOf("Nokia 5.3", "Nokia 2.3", "Nokia 3.1 Plus")
        val motorolaModels = arrayOf("Motorola One Fusion+", "Motorola E7 Plus", "Motorola G9")
        mobileCollection = HashMap()
        for (group in groupList!!) {
            if (group == "Samsung") {
                loadChild(samsungModels)
            } else if (group == "Google") loadChild(googleModels) else if (group == "Redmi") loadChild(
                redmiModels
            ) else if (group == "Vivo") loadChild(vivoModels) else if (group == "Nokia") loadChild(
                nokiaModels
            ) else loadChild(motorolaModels)
            mobileCollection!![group] = childList
        }
    }

    private fun loadChild(mobileModels: Array<String>) {
        childList = ArrayList()
        for (model in mobileModels) {
            childList!!.add(model)
        }
    }

    private fun createGroupList() {
        groupList = ArrayList()
        groupList!!.add("Samsung")
        groupList!!.add("Google")
        groupList!!.add("Redmi")
        groupList!!.add("Vivo")
        groupList!!.add("Nokia")
        groupList!!.add("Motorola")
    }
}