package com.group4.sdp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CheckBmi : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_bmi)

        val T1 = findViewById<EditText>(R.id.t1)
        val T2 = findViewById<EditText>(R.id.t2)
        val T3 = findViewById<EditText>(R.id.t3)
        val T4 = findViewById<EditText>(R.id.t4)
        val B1 = findViewById<Button>(R.id.b1)

        db=openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null)

        B1.setOnClickListener {
            if (T1.text.toString()!="" && T2.text.toString() != "" && T3.text.toString() != "" && T4.text.toString() != "")
            {
                db.execSQL("INSERT INTO usage VALUES('"+T1.text.toString()+
                        "','"+T4.text.toString()+"');")
                
            }
            else
            {
                Toast.makeText(this, "Fill all required spaces", Toast.LENGTH_SHORT).show()
            }
        }
    }

}