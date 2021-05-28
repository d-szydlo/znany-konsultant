package com.example.znanykonultant.consultant.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.znanykonultant.R
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

class  ConsultantServiceEdit : AppCompatActivity() {

    private lateinit var type: EditText
    private lateinit var desc: EditText
    private lateinit var price: EditText
    private lateinit var id: String
    val consultantUid = FirebaseAuth.getInstance().uid
    val database = Firebase.database
    val consultantRef = database.getReference("consultants/$consultantUid/consultantService")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultant_service_edit)

        type = findViewById(R.id.service_type)
        desc = findViewById(R.id.service_desc)
        price = findViewById(R.id.service_price)

        val extras = intent.extras
        if (extras != null) {
            type.setText(extras.getString("type"))
            desc.setText(extras.getString("desc"))
            price.setText(extras.getDouble("price").toString())
            id = extras.getString("key").toString()
        }

        findViewById<Button>(R.id.save_changes).setOnClickListener {
            var new_type = type.text.toString()
            var new_desc = desc.text.toString()
            var new_price = price.text.toString().toDouble()
            val new_data = mutableMapOf<String, Any>()
            new_data["type"] = new_type
            new_data["description"] = new_desc
            new_data["cost"] = new_price
            if (consultantUid != null) {
                Log.i("iddd", id)
                consultantRef.child(id).updateChildren(new_data)
            }

            val intent = Intent(this, ConsultantMainPageActivity::class.java)
            startActivity(intent)
        }

    }


}