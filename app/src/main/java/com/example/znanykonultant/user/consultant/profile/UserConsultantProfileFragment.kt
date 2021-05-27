package com.example.znanykonultant.user.consultant.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.consultant.profile.ConsultantServicesAdapter
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.ConsultantService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.FileNotFoundException


class UserConsultantProfileFragment(uid: String?) : Fragment() {

    lateinit var name: EditText
    lateinit var surname: EditText
    lateinit var email: EditText
    lateinit var phone: EditText
    lateinit var desc: EditText
    lateinit var url: EditText
    lateinit var photo: ImageView
    lateinit var servicesRecycler: RecyclerView
    private var imageUri: Uri? = null
    val consultantUid = uid
    var data : Consultant? = null
    val database = Firebase.database
    val consultantRef = database.getReference("consultants")
    var consultants : MutableList<Consultant> = mutableListOf()
    lateinit var storage: FirebaseStorage


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_consultant_profile, container, false)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                data = dataSnapshot.child(consultantUid!!).getValue(Consultant::class.java)
                setPersonalInfo()

                dataSnapshot.children.mapNotNullTo(consultants) {
                    it.getValue(Consultant::class.java)
                }
                Log.i("firebase", data.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        consultantRef.addValueEventListener(postListener)

        var consultant: ConsultantService = ConsultantService(120.0, "bardzo dlugie konsultacje", "dlugie")
        var consultant2: ConsultantService = ConsultantService(150.0, "kons", "rozmowa")
        var servicesList = arrayListOf(consultant)
        servicesList.add(consultant2)

        servicesRecycler = view.findViewById<RecyclerView>(R.id.consultant_services)
        servicesRecycler.layoutManager = LinearLayoutManager(view.context)
        servicesRecycler.adapter = ConsultantServicesAdapter(view.context, servicesList)
        servicesRecycler.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

        servicesRecycler.addOnLayoutChangeListener(View.OnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                servicesRecycler.postDelayed(Runnable {
                    servicesRecycler.smoothScrollToPosition(
                        (servicesRecycler.adapter as ConsultantServicesAdapter).itemCount - 1
                    )
                }, 100)
            }
        })

        name = view.findViewById(R.id.consultant_name)
        surname = view.findViewById(R.id.consultant_surname)
        email = view.findViewById(R.id.consultant_email)
        phone = view.findViewById(R.id.consultant_phone)
        desc = view.findViewById(R.id.consultant_description)
        url = view.findViewById(R.id.consultant_url)
        photo  = view.findViewById(R.id.photo)

        storage = Firebase.storage

        name.inputType = 0
        surname.inputType = 0
        email.inputType = 0
        phone.inputType = 0
        desc.inputType = 0
        url.inputType = 0

        var saveButton : Button = view.findViewById(R.id.save)
        var userConsultantProfileLayout : ConstraintLayout = view.findViewById(R.id.userConsultantProfileLayout)
        userConsultantProfileLayout.removeView(saveButton);

        return view
    }

    private fun setPersonalInfo(){
        name.setText(data?.name)
        surname.setText(data?.surname)
        email.setText(data?.email)
        phone.setText(data?.phone)
        desc.setText(data?.description)
        url.setText(data?.page)
        try {
            Picasso.get().load(data?.picture).into(photo);
        }catch(e: IllegalArgumentException){}
        //address.setText(data?.city + data?.street + data?.houseNumber)
    }

    private fun updateData(consultantUpdate : MutableMap<String, Any>) {
        if (consultantUid != null) {
            consultantRef.child(consultantUid).updateChildren(consultantUpdate)
        }
    }
}