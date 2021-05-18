package com.example.znanykonultant.registration

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.znanykonultant.dao.UserDAO
import com.example.znanykonultant.databinding.FragmentUserRegisterBinding
import com.example.znanykonultant.user.UserMainPageActivity
import com.google.firebase.auth.FirebaseAuth

class UserRegisterFragment : Fragment() {
    lateinit var userDAO : UserDAO
    lateinit var mAuth: FirebaseAuth
    lateinit var binding: FragmentUserRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mAuth = FirebaseAuth.getInstance()
        binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        binding.registerUserButton.setOnClickListener { createNewAccount() }
        return binding.root
    }

    private fun createNewAccount() {
        val name: String = binding.name.text.toString()
        val surname: String = binding.surname.text.toString()
        val email: String = binding.email.text.toString()
        val phone: String = binding.phone.text.toString()
        val pass1: String = binding.pass1.text.toString()
        val pass2: String = binding.pass2.text.toString()
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname)
            && !TextUtils.isEmpty(email)
            && !TextUtils.isEmpty(pass1)  && !TextUtils.isEmpty(pass2) && TextUtils.equals(pass1, pass2)) { //TODO separate info when passwords are not the same
            mAuth.createUserWithEmailAndPassword(email, pass1)
                .addOnSuccessListener {
                    userDAO  = UserDAO()
                    userDAO.addPerson(
                        mAuth.uid.toString(), name, surname, email, phone
                    )
                    verifyEmail() // TODO add user data to database
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
        val mUser = mAuth.currentUser
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
        val intent = Intent(this.activity, UserMainPageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}