package com.omanshuaman.tournamentsports

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CardviewActivity : AppCompatActivity() {
    private var mTextview: TextView? = null
    private var Id: String? = null
    private var mButton: Button? = null
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click_cardview)

        mTextview = findViewById(R.id.textview_recycler)
        mButton = findViewById(R.id.regiter)
        firebaseAuth = FirebaseAuth.getInstance()

        //get id of the group
        val intent = intent
        Id = intent.getStringExtra("tournamentId")

        //get current user
        val user = firebaseAuth!!.uid
        if (user != null) {
            //user is signed in stay here
            //set email of logged in user
            val ref = FirebaseDatabase.getInstance().getReference("Tournament").child("Groups")
            ref.child(Id!!).child("Participants").child(user)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        mButton!!.isEnabled = !dataSnapshot.exists()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

            val ref1 =
                FirebaseDatabase.getInstance().getReference("Tournament").child("Just Photos")
            ref1.orderByChild("id").equalTo(Id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children) {
                            val groupTitle = "" + ds.child("tournamentName").value
                            mTextview!!.text = groupTitle
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        } else {
            val ref1 =
                FirebaseDatabase.getInstance().getReference("Tournament").child("Just Photos")
            ref1.orderByChild("id").equalTo(Id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children) {
                            val groupTitle = "" + ds.child("tournamentName").value
                            mTextview!!.text = groupTitle
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }

        mButton!!.setOnClickListener {

            if (user != null) {
                val intent1 = Intent(this, ParticipantInfoActivity::class.java)
                intent1.putExtra("tournamentId", Id)
                startActivity(intent1)
            }
         else {
                val intent1 = Intent(this, SignInActivity::class.java)
                startActivity(intent1)
            }

        }
    }
}