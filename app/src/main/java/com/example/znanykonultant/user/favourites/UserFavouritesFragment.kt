package com.example.znanykonultant.user.favourites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.Favourite
import com.example.znanykonultant.user.consultant.profile.UserConsultantProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class UserFavouritesFragment : Fragment(), FavouritesConsultantAdapter.OnItemClickListener {

    lateinit var favouritesRecycler: RecyclerView
    private lateinit var listAdapter: FavouritesConsultantAdapter
    val userUid = FirebaseAuth.getInstance().uid
    val database = Firebase.database
    private val userRef = database.getReference("users")
    val consultantRef = database.getReference("consultants")
    var userFavourites : MutableList<Favourite> = mutableListOf()
    var userFavouritesConsultants : MutableList<Consultant> = mutableListOf()
    private var favoritesIds : MutableList<String> = mutableListOf()
    lateinit var storage: FirebaseStorage
    var data : Consultant? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_favourites, container, false)
        listAdapter = FavouritesConsultantAdapter(userFavouritesConsultants, favoritesIds, this)

        setDatabaseListener()
        initRecycler(view)
        return view
    }

    private fun initRecycler(view: View){
        val recyclerView = view.findViewById<RecyclerView>(R.id.favoritesRecycler)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = listAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
    }

    private fun setDatabaseListener(){

        val favoritesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userFavouritesConsultants = mutableListOf()
                for (key in favoritesIds) {
                    val favorite = dataSnapshot.child(key).getValue(Consultant::class.java)
                    if (favorite != null) {
                        userFavouritesConsultants.add(favorite)
                    }
                }
                listAdapter.updateData(userFavouritesConsultants, favoritesIds)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                favoritesIds = mutableListOf()

                dataSnapshot.child(userUid!!).child("favorites").children.mapNotNullTo(favoritesIds) {
                    it.key
                }
                consultantRef.addValueEventListener(favoritesListener)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }

        }

        userRef.addValueEventListener(postListener)
    }

    override fun onItemClick(consultant: Consultant, id : String) {

        val intent = Intent(activity, UserConsultantProfileActivity::class.java)
        intent.putExtra("consultant_uid", id)
        startActivity(intent)
    }
}