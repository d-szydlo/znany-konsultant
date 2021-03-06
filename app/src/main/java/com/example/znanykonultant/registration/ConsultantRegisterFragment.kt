package com.example.znanykonultant.registration

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import com.example.znanykonultant.dao.ConsultantDAO
import com.example.znanykonultant.databinding.FragmentConsultantRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class ConsultantRegisterFragment : Fragment() {

    lateinit var consultantDAO : ConsultantDAO
    lateinit var mAuth: FirebaseAuth
    lateinit var binding: FragmentConsultantRegisterBinding
    private var categories : MutableList<String> = mutableListOf()
    private var arrayChecked = booleanArrayOf(false, false, false, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mAuth = FirebaseAuth.getInstance()
        binding = FragmentConsultantRegisterBinding.inflate(inflater, container, false)
        binding.registerConsultantButton.setOnClickListener { createNewAccount() }
        binding.consultantCategoryButton.setOnClickListener { showDialog() }
        return binding.root
    }

    private fun createNewAccount() {
        val name: String = binding.name.text.toString()
        val surname: String = binding.surname.text.toString()
        val email: String = binding.email.text.toString()
        val phone: String = binding.phone.text.toString()
        val city: String = binding.city.text.toString()
        val pass1: String = binding.pass1.text.toString()
        val pass2: String = binding.pass2.text.toString()
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname)
            && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(city)
            && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pass1)
            && !TextUtils.isEmpty(pass2) && TextUtils.equals(pass1, pass2)
            && categories.isNotEmpty()
        ) {
            mAuth.createUserWithEmailAndPassword(email, pass1)
                .addOnSuccessListener {
                    consultantDAO  = ConsultantDAO()
                    consultantDAO.addConsultant(
                        mAuth.uid.toString(), name, surname, email, "", phone, city, categories = categories
                    )
                    verifyEmail()
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
        val intent = Intent(this.activity, ConsultantMainPageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun showDialog(){
        lateinit var dialog : AlertDialog

        val arrayCategories = arrayOf("IT", "finanse i rachunkowo????", "marketing", "biznes")

        val builder = AlertDialog.Builder(this.context)

        builder.setTitle("Wybierz kategorie w zakresie kt??rych udzielasz konsultacji")

        builder.setMultiChoiceItems(arrayCategories, arrayChecked) { dialog, which, isChecked ->
            arrayChecked[which] = isChecked
        }

        builder.setPositiveButton("OK") { _, _ ->
            categories = mutableListOf()
            for (i in arrayCategories.indices) {
                val checked = arrayChecked[i]
                if (checked) {
                   categories.add(arrayCategories[i])
                }
            }
        }
        dialog = builder.create()
        dialog.show()
    }
}