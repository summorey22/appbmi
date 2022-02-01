package com.group4.sdp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        window.statusBarColor =  ContextCompat.getColor(this, R.color.bluw)

        findViewById<LinearLayout>(R.id.recordlayout).isVisible = false
        findViewById<TextView>(R.id.nofound).isVisible = true

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val sharedNameValue = sharedPreferences.getString("user_name","Admin")

        findViewById<TextView>(R.id.textmain).text = "Hi! \n" + sharedNameValue.toString()

        findViewById<FloatingActionButton>(R.id.addbutton).setOnClickListener {
            val intent = Intent(this, CheckBmi::class.java)
            startActivity(intent)
        }

        db=openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS usage(srno VARCHAR,bmi VARCHAR)")

        setData()



    }

    private fun setData(){
        val c: Cursor = db.rawQuery("SELECT * FROM usage", null)

        if (c.count != 0)
        {
            findViewById<LinearLayout>(R.id.recordlayout).isVisible = true
            findViewById<TextView>(R.id.nofound).isVisible = false

            var srno = ArrayList<Any>()
            var bmi = ArrayList<Any>()

            while (c.moveToNext())
            {
                srno.add(c.getString(0))
                bmi.add(c.getString(1))
            }

            val recyclerview = findViewById<RecyclerView>(R.id.recyclerview1)

            recyclerview.layoutManager = GridLayoutManager(this, 3)
            val adapter = CustomAdapter(srno, bmi)
            recyclerview.adapter = adapter


        }
    }
}