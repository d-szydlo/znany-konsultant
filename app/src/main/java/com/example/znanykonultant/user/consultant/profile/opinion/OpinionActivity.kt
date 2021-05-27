package com.example.znanykonultant.user.consultant.profile.opinion

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.znanykonultant.chat.LatestMessageRow
import com.example.znanykonultant.databinding.ActivityOpinionBinding
import com.example.znanykonultant.entity.Messages
import com.example.znanykonultant.entity.Opinion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Math.round
import kotlin.math.roundToInt


class OpinionActivity : AppCompatActivity() {
    lateinit var binding: ActivityOpinionBinding
    lateinit var consultantUid : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpinionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        consultantUid = this.intent.getStringExtra("consultant_uid").toString()
    }

    fun saveOpinion(view: View) {
        updateConsultantAverage()
        val rating = binding.rating.rating
        val title = binding.title.text.toString()
        val description = binding.descriptiveOpinion.text.toString()
        val consultantId = consultantUid
        val userId = FirebaseAuth.getInstance().uid

        val reference = FirebaseDatabase.getInstance().getReference(
            "opinions/$userId/$consultantUid"
        ).push()
        val consultantReference = FirebaseDatabase.getInstance().getReference(
            "opinions/$consultantUid/"
        ).push()

        val opinion = Opinion(reference.key!!, rating, title, description, consultantId!!, userId!!)
        reference.setValue(opinion)
        consultantReference.setValue(opinion)

        val resultIntent = Intent()
        resultIntent.putExtra("consultant_uid", consultantUid)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    fun updateConsultantAverage(){
        val consultantReference = FirebaseDatabase.getInstance().getReference("opinions/$consultantUid/")
        val ratingReference = FirebaseDatabase.getInstance().getReference(
            "consultants/$consultantUid/averageRating"
        )
        consultantReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var avg  = 0f
                var count  = 0
                for (opinionSnapshot in dataSnapshot.children) {
                    var opinion = opinionSnapshot.getValue(Opinion::class.java) ?: return
                    avg += opinion.rating
                    count += 1
                }
                avg /= count
                avg = (avg * 100.0f).roundToInt().toFloat() / 100f
                ratingReference.setValue(avg)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // TODO
            }
        })
    }
}