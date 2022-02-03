package com.group4.sdp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CheckBmi : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase
    private lateinit var mImageView: ImageView
    private var mUri: Uri? = null
    private lateinit var texbmi: TextView
    private lateinit var tv: TextView

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_bmi)

        texbmi = findViewById(R.id.bmitext)
        tv = findViewById(R.id.tvb)
        texbmi.isVisible = false


        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        window.setBackgroundDrawableResource(com.google.android.material.R.color.mtrl_btn_transparent_bg_color)

        val width = dm.widthPixels
        val height = dm.heightPixels

        window.setLayout((width*.95).toInt(), (height*.7).toInt())

        val params = window.attributes
        params.gravity = Gravity.BOTTOM
        params.verticalMargin = 0.15F
        params.x = 0
        params.y = -20

        window.attributes = params

        db=openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null)

        mImageView = findViewById(R.id.iview)

        val check = findViewById<FloatingActionButton>(R.id.check)
        val select = findViewById<FloatingActionButton>(R.id.select)

        select.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, Companion.PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {

        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            tv.isVisible = false
            mImageView.setImageURI(data?.data)
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }


}