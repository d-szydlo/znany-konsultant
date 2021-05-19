package com.example.znanykonultant.consultant.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.znanykonultant.R
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import kotlin.properties.Delegates

class  ConsultantServiceEdit : AppCompatActivity() {

    private lateinit var type: EditText
    private lateinit var desc: EditText
    private lateinit var price: EditText
    private var id by Delegates.notNull<Int>()

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
            price.setText(extras.getInt("price").toString())
            id = extras.getInt("id")
        }

        findViewById<Button>(R.id.save_changes).setOnClickListener {
            // TODO save changes to db
            val intent = Intent(this, ConsultantMainPageActivity::class.java)
            startActivity(intent)
        }

    }


}