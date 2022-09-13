package com.omanshuaman.tournamentsports.adapters

import com.omanshuaman.tournamentsports.models.DataModel
import androidx.recyclerview.widget.RecyclerView
import com.omanshuaman.tournamentsports.adapters.ItemAdapter.ItemViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.omanshuaman.tournamentsports.R
import com.omanshuaman.tournamentsports.adapters.NestedAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.ArrayList

class ItemAdapter(private val mList: List<DataModel>) : RecyclerView.Adapter<ItemViewHolder>() {
    private var list: List<String> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = mList[position]
        holder.mTextView.text = model.itemText
        val isExpandable = model.isExpandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE
        if (isExpandable) {
            holder.mArrowImage.setImageResource(R.drawable.arrow_up)
        } else {
            holder.mArrowImage.setImageResource(R.drawable.arrow_down)
        }
        val adapter = NestedAdapter(list)
        holder.nestedRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.nestedRecyclerView.setHasFixedSize(true)
        holder.nestedRecyclerView.adapter = adapter
        holder.linearLayout.setOnClickListener {
            model.isExpandable = !model.isExpandable
            list = model.nestedList
            notifyItemChanged(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val linearLayout: LinearLayout
        private val expandableLayout: RelativeLayout
        private val mTextView: TextView
        private val mArrowImage: ImageView
        private val nestedRecyclerView: RecyclerView

        init {
            linearLayout = itemView.findViewById(R.id.linear_layout)
            expandableLayout = itemView.findViewById(R.id.expandable_layout1)
            mTextView = itemView.findViewById(R.id.itemTv)
            mArrowImage = itemView.findViewById(R.id.arro_imageview)
            nestedRecyclerView = itemView.findViewById(R.id.child_rv)
        }
    }
}