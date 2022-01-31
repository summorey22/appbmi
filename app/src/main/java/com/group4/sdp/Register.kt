package com.group4.sdp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        findViewById<ImageButton>(R.id.back).setOnClickListener {
            this.finish()
        }


        findViewById<TextView>(R.id.regsignin).setOnClickListener(){

            val intent = Intent(this,Login::class.java)
            startActivity(intent)

        }

        register()
    }

    private fun register() {

        val emailya: EditText = findViewById(R.id.regemail)
        val fname: EditText = findViewById(R.id.regfirstnm)
        val pass: EditText = findViewById(R.id.regpass)
        val text3: EditText = findViewById(R.id.middlenm)
        val text4: EditText = findViewById(R.id.reglasnm)

        findViewById<FloatingActionButton>(R.id.submitregcard).setOnClickListener() { view ->
            when {
                TextUtils.isEmpty(emailya.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(fname.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(text3.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter middle name", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(text4.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(pass.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                }
                else -> {

                    auth.createUserWithEmailAndPassword(emailya.text.toString(),pass.text.toString())
                        .addOnCompleteListener{

                            if(it.isSuccessful){

                                val currentUser = auth.currentUser
                                val currentUserDb = databaseReference?.child((currentUser?.uid!!))
                                currentUserDb?.child("firstname")?.setValue(fname.text.toString())
                                currentUserDb?.child("middlename")?.setValue(text3.text.toString())
                                currentUserDb?.child("lastname")?.setValue(text4.text.toString())

                                Toast.makeText(this,"Registration Succesful", Toast.LENGTH_SHORT).show()
                                finish()

                            }
                            else{
                                Toast.makeText(this,"Registration Failed", Toast.LENGTH_SHORT).show()
                            }
                        }

                }
            }
        }
    }
}