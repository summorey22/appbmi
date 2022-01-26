package com.group4syetc.facetobmi

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.group4syetc.facetobmi.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer


class MainActivity : AppCompatActivity() {

    private var bm: Bitmap? = null
    private lateinit var button: Button
    private lateinit var button1: Button
    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button1)
        button1 = findViewById(R.id.button2)
        imageView = findViewById(R.id.ImageView1)

        if (findViewById<TextView>(R.id.bruh).text == "null") {
            findViewById<TextView>(R.id.bruh).isVisible = false
        }

        if (imageView?.drawable ==null) {
            button1.isVisible = false
        }

        button.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
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

        button1.setOnClickListener {
            val model = Model.newInstance(this)
            val drawable = imageView?.drawable as BitmapDrawable
            val bhai = drawable.bitmap
            bm = Bitmap.createScaledBitmap(bhai,128,128,false)

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)
            val btbff = convertBitmapToByteBuffer(bm!!)
            Log.d("bruhhhh", "bm: $btbff")
            inputFeature0.loadBuffer(btbff!!)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            Log.d("bruhhhhh", "op: ${outputFeature0.floatArray.toString()}")
            findViewById<TextView>(R.id.bruh).text = outputFeature0.toString()
//            val op_bm = getOutputImage(outputFeature0.buffer)
//            imageView!!.setImageBitmap(op_bm)
            // Releases model resources if no longer used.
            model.close()
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer? {
        val byteBuffer = ByteBuffer.allocate(4*128*128*3)
        byteBuffer.rewind()
        bitmap.copyPixelsToBuffer(byteBuffer)

        return byteBuffer
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

    private fun convertImageViewToBitmap(v: ImageView): Bitmap? {
        return (v.drawable as BitmapDrawable).bitmap
    }

//    private fun getOutputImage(output: ByteBuffer): Bitmap {
//        output.rewind() // Rewind the output buffer after running.
//        val outputHeight = 256
//        val outputWidth = 256
//        val bitmap = Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.ARGB_8888)
//        val pixels = IntArray(outputWidth * outputHeight) // Set your expected output's height and width
//        for (i in 0 until outputWidth * outputHeight) {
//            val a = 0xFF
//            val r: Float = output.float * 255.0f
//            val g: Float = output.float * 255.0f
//            val b: Float = output.float * 255.0f
//            pixels[i] = a shl 24 or (r.toInt() shl 16) or (g.toInt() shl 8) or b.toInt()
//        }
//        bitmap.setPixels(pixels, 0, outputWidth, 0, 0, outputWidth, outputHeight)
//
//        return bitmap
//    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageView?.setImageURI(data?.data)
            val drawable = imageView!!.drawable as BitmapDrawable
            val bhai: Bitmap = drawable.bitmap
            button1.isVisible = true
            bm = Bitmap.createScaledBitmap(bhai,128,128,false)
        }
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        //Permission code
        private const val PERMISSION_CODE = 1001
    }
}