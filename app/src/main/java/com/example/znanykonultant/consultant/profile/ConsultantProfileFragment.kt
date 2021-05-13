package com.example.znanykonultant.consultant.profile

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.ConsultantService


class ConsultantProfileFragment : Fragment() {

    lateinit var name: EditText
    lateinit var surname: EditText
    lateinit var email: EditText
    lateinit var phone: EditText
    lateinit var desc: EditText
    lateinit var url: EditText
    lateinit var address: EditText
    lateinit var photo: ImageView
    lateinit var servicesRecycler: RecyclerView
    private var imageUri: Uri? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_consultant_profile, container, false)

        var consultant: ConsultantService = ConsultantService(120, "bardzo dlugie konsultacje", "dlugie")
        var consultant2: ConsultantService = ConsultantService(150, "kons", "rozmowa")
        var servicesList = arrayListOf(consultant)
        servicesList.add(consultant2)
        servicesRecycler = view.findViewById<RecyclerView>(R.id.consultant_services)
        servicesRecycler.layoutManager = LinearLayoutManager(view.context)
        servicesRecycler.adapter = ConsultantServicesAdapter(view.context, servicesList)
        servicesRecycler.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

        name = view.findViewById(R.id.consultant_name)
        surname = view.findViewById(R.id.consultant_surname)
        email = view.findViewById(R.id.consultant_email)
        phone = view.findViewById(R.id.consultant_phone)
        desc = view.findViewById(R.id.consultant_description)
        url = view.findViewById(R.id.consultant_url)
        address = view.findViewById(R.id.consultant_address)
        photo  = view.findViewById(R.id.photo)
        setPersonalInfo()

        view.findViewById<Button>(R.id.save).setOnClickListener { save(it) }

        view.findViewById<ImageView>(R.id.photo).setOnClickListener{
            newPhoto(it)
        }

        return view
    }

    private fun save(view: View){
        // TODO save changed data to db
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
            // TODO save in db uri
            // TODO change in db picture type to string
        }
    }

    private fun setPersonalInfo(){
        // TODO get user data from db and display them
    }
}