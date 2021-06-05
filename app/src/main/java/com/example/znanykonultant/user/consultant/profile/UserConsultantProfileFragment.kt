package com.example.znanykonultant.user.consultant.profile

import android.net.Uri
import android.os.Bundle
import android.text.InputType
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
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Consultant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso


class UserConsultantProfileFragment(uid: String?) : Fragment() {

    lateinit var name: EditText
    lateinit var surname: EditText
    lateinit var email: EditText
    lateinit var phone: EditText
    lateinit var desc: EditText
    lateinit var url: EditText
    lateinit var photo: ImageView
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

        name = view.findViewById(R.id.consultant_name)
        surname = view.findViewById(R.id.consultant_surname)
        email = view.findViewById(R.id.consultant_email)
        phone = view.findViewById(R.id.consultant_phone)
        desc = view.findViewById(R.id.consultant_description)
        url = view.findViewById(R.id.consultant_url)
        photo  = view.findViewById(R.id.photo)

        storage = Firebase.storage

        name.setReadOnly(true)
        surname.setReadOnly(true)
        email.setReadOnly(true)
        phone.setReadOnly(true)
        desc.setReadOnly(true)
        url.setReadOnly(true)

        var saveButton : Button = view.findViewById(R.id.save)
        var visitorsButton : Button = view.findViewById(R.id.visitorsButton)
        var userConsultantProfileLayout : ConstraintLayout = view.findViewById(R.id.userConsultantProfileLayout)
        userConsultantProfileLayout.removeView(saveButton);
        userConsultantProfileLayout.removeView(visitorsButton);

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

fun EditText.setReadOnly(value: Boolean, inputType: Int = InputType.TYPE_NULL) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.inputType = inputType
}