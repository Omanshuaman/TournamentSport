package com.omanshuaman.tournamentsports.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import coil.annotation.ExperimentalCoilApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.omanshuaman.tournamentsports.ComposeActivity
import com.omanshuaman.tournamentsports.R
import com.omanshuaman.tournamentsports.models.Upload
import com.squareup.picasso.Picasso


class AdapterCard(context: Context, uploads: List<Upload?>?) :
    RecyclerView.Adapter<AdapterCard.MyHolder>() {
    private val mContext: Context = context
    private val mUploads: List<Upload?> = uploads as List<Upload?>

    //view holder classA
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView
        var image: ImageView

        init {
            name = itemView.findViewById(R.id.txtPlaceName)
            image = itemView.findViewById(R.id.cardImageview)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        //inflate layout row_post.xml
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.place_layout, parent, false)

        view.layoutParams = ViewGroup.LayoutParams(
            (parent.width * 0.90).toInt(),
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        return MyHolder(view)
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalCoilApi::class)
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        //get data

        val model = mUploads[position]
        val id = model?.Id
        val image = model?.imageUrl
        val name: String? = mUploads[position]?.tournamentName
        Picasso.get().load(image).fit().centerCrop().into(holder.image)

        //set data
        holder.name.text = name

        //handle group click
        holder.itemView.setOnClickListener {
            //open group chat

            val intent = Intent(mContext, ComposeActivity::class.java)
            val extras = Bundle()
            extras.putString("tournamentId", id)
            extras.putString("EXTRA_PASSWORD", image)
            intent.putExtras(extras)
            mContext.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return mUploads.size
    }
}