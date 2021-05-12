package com.example.znanykonultant.registration

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import com.example.znanykonultant.databinding.FragmentConsultantRegisterBinding
import com.example.znanykonultant.login.LoginActivity
import com.example.znanykonultant.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ConsultantRegisterFragment : Fragment() {

    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mAuth: FirebaseAuth
    lateinit var binding: FragmentConsultantRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        binding = FragmentConsultantRegisterBinding.inflate(inflater, container, false)
        binding.registerConsultantButton.setOnClickListener { createNewAccount() }
        return binding.root
    }

    private fun createNewAccount() {
        var name: String? = binding.name.text.toString()
        var surname: String? = binding.surname.text.toString()
        var email: String? = binding.email.text.toString()
        var phone: String? = binding.phone.text.toString()
        var city: String? = binding.city.text.toString()
        var pass1: String? = binding.pass1.text.toString()
        var pass2: String? = binding.pass2.text.toString()
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname)
            && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(city)
            && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pass1)
            && !TextUtils.isEmpty(pass2) && TextUtils.equals(pass1, pass2)) { //TODO separate info when passwords are not the same
            mAuth.createUserWithEmailAndPassword(email!!, pass1!!)
                .addOnSuccessListener {
                    val userId = mAuth.currentUser!!.uid
                    verifyEmail();
                    val currentUserDb = mDatabaseReference!!.child(userId)
                    currentUserDb.child("type").setValue("consultant")
                    updateUserInfoAndUI()
                }.addOnFailureListener {  e ->
                    Toast.makeText(
                        this.activity, e.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(this.activity, "Podaj wszystkie dane!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyEmail() {
        val mUser = mAuth.currentUser;
        mUser!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this.activity,
                        "Verification email sent to " + mUser.getEmail(),
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this.activity,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUserInfoAndUI() {
        val intent = Intent(this.activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}