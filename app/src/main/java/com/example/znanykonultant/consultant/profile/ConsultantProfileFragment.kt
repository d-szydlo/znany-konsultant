package com.example.znanykonultant.consultant.profile

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
import androidx.fragment.app.Fragment
import com.example.znanykonultant.R
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import com.example.znanykonultant.entity.Consultant
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


class ConsultantProfileFragment : Fragment() {

    lateinit var name: EditText
    lateinit var surname: EditText
    lateinit var email: EditText
    lateinit var phone: EditText
    lateinit var desc: EditText
    lateinit var url: EditText
    lateinit var photo: ImageView
    private var imageUri: Uri? = null
    val consultantUid = FirebaseAuth.getInstance().uid
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

        view.findViewById<Button>(R.id.save).setOnClickListener { save(it) }

        view.findViewById<ImageView>(R.id.photo).setOnClickListener{
            newPhoto(it)
        }

        storage = Firebase.storage

        view.findViewById<Button>(R.id.visitorsButton).setOnClickListener {
            (activity as ConsultantMainPageActivity).setFragment(ConsultantVisitorsChartFragment())
        }
        return view
    }

    private fun save(view: View){
        var new_name = name.text
        var new_surname = surname.text
        var new_email = email.text
        var new_phone = phone.text
        var new_desc = desc.text
        var new_url = url.text

        val new_data = mutableMapOf<String, Any>()
        new_data["name"] = new_name.toString()
        new_data["surname"] = new_surname.toString()
        new_data["email"] = new_email.toString()
        new_data["phone"] = new_phone.toString()
        new_data["description"] = new_desc.toString()
        new_data["page"] = new_url.toString()
        updateData(new_data)
    }

    private fun newPhoto(view: View){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            var x = imageUri.toString()
            photo.setImageURI(Uri.parse(x))

            var storageRef = storage.reference
            try{
                var path = "profile_pictures/" + consultantUid.toString() + "/1.jpg"
                var ref = storageRef.child(path)
                var uploadTask = imageUri?.let { ref.putFile(it) }
                uploadTask?.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl
                }?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val new_data = mutableMapOf<String, Any>()
                        new_data["picture"] = downloadUri.toString()
                        updateData(new_data)
                    } else {}
                }
            } catch (e: FileNotFoundException){
                
            }

        }
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