package com.example.znanykonultant.consultant.services

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.ConsultantService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class ConsultantServicesFragment : Fragment() {

    lateinit var servicesRecycler: RecyclerView
    val consultantUid = FirebaseAuth.getInstance().uid
    var data : ConsultantService? = null
    val database = Firebase.database
    val consultantRef = database.getReference("consultants/$consultantUid/consultantService")
    var consultantServices : MutableList<ConsultantService> = mutableListOf()
    lateinit var storage: FirebaseStorage

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_consultant_services, container, false)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                consultantServices.clear()

                dataSnapshot.children.mapNotNullTo(consultantServices) {
                    it.getValue(ConsultantService::class.java)
                }
                servicesRecycler = view.findViewById(R.id.consultant_services)
                servicesRecycler.layoutManager = LinearLayoutManager(view.context)
                servicesRecycler.adapter = ConsultantServicesAdapter(view.context, consultantServices)
                servicesRecycler.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        consultantRef.addValueEventListener(postListener)

        view.findViewById<Button>(R.id.button).setOnClickListener { add(it) }

        storage = Firebase.storage
        return view
    }

    private fun add(view: View){
        val intent = Intent(activity, ConsultantAddServiceActivity::class.java)
        startActivity(intent)
    }
}