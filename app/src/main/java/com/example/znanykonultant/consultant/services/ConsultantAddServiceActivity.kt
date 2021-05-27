package com.example.znanykonultant.consultant.services

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.znanykonultant.R
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.ConsultantService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.security.Provider
import kotlin.properties.Delegates

class ConsultantAddServiceActivity : AppCompatActivity() {

    private lateinit var type: EditText
    private lateinit var desc: EditText
    private lateinit var price: EditText
    private var id by Delegates.notNull<Int>()
    val consultantUid = FirebaseAuth.getInstance().uid
    var data : ConsultantService? = null
    val database = Firebase.database
    val consultantRef = database.getReference("consultants/$consultantUid/consultantService")
    var services : MutableList<ConsultantService> = mutableListOf()
    lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultant_add_service)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                data = dataSnapshot.child(consultantUid!!).getValue(ConsultantService::class.java)

                dataSnapshot.children.mapNotNullTo(services) {
                    it.getValue(ConsultantService::class.java)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        consultantRef.addValueEventListener(postListener)

        type = findViewById(R.id.service_type2)
        desc = findViewById(R.id.service_desc2)
        price = findViewById(R.id.service_price2)

        findViewById<Button>(R.id.add).setOnClickListener {
            var new_type = type.text
            var new_desc = desc.text
            var new_price: String = price.text.toString()

            val reference = consultantRef.push()
            val service = ConsultantService(new_price.toDouble(), new_desc.toString(), new_type.toString())
            reference.setValue(service)

            val intent = Intent(this, ConsultantMainPageActivity::class.java)
            startActivity(intent)
        }

        storage = Firebase.storage
    }
}