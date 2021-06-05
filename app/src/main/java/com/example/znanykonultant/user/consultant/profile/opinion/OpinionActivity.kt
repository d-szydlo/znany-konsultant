package com.example.znanykonultant.user.consultant.profile.opinion

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.znanykonultant.chat.LatestMessageRow
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import com.example.znanykonultant.databinding.ActivityOpinionBinding
import com.example.znanykonultant.entity.Messages
import com.example.znanykonultant.entity.Opinion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Math.round
import kotlin.math.roundToInt


class OpinionActivity : AppCompatActivity() {
    lateinit var binding: ActivityOpinionBinding
    lateinit var consultantUid : String
    private val userId = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpinionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        consultantUid = this.intent.getStringExtra("consultant_uid").toString()

        Firebase.database.getReference().child("opinions").child(userId.toString()).child(consultantUid).get().addOnSuccessListener{
            if(it.value != null){
                var opinion = it.getValue(Opinion::class.java)
                binding.title.text = Editable.Factory.getInstance().newEditable(opinion?.title)
                binding.rating.rating = opinion?.rating!!
                binding.descriptiveOpinion.text = Editable.Factory.getInstance().newEditable(opinion.description)
            }
        }
    }

    fun saveOpinion(view: View) {
        updateConsultantAverage()
        val rating = binding.rating.rating
        val title = binding.title.text.toString()
        val description = binding.descriptiveOpinion.text.toString()
        val consultantId = consultantUid
        val reference = FirebaseDatabase.getInstance().getReference(
            "opinions/$userId/$consultantUid"
        )
        val consultantReference = FirebaseDatabase.getInstance().getReference(
            "opinions/$consultantUid/$userId"
        )

        val opinion = Opinion(rating, title, description, consultantId!!, userId!!)
        consultantReference.setValue(opinion)
        reference.setValue(opinion)

        val resultIntent = Intent()
        resultIntent.putExtra("consultant_uid", consultantUid)
        setResult(RESULT_OK, resultIntent)
        finish()
    }


    fun deleteOpinion(view: View) {

    }

    fun updateConsultantAverage(){
        val consultantReference = FirebaseDatabase.getInstance().getReference("opinions").child(consultantUid)
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
                if(count != 0){
                    avg /= count
                    avg = (avg * 100.0f).roundToInt().toFloat() / 100f
                    ratingReference.setValue(avg)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // TODO
            }
        })
    }
}