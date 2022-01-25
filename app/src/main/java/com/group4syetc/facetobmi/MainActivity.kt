package com.group4syetc.facetobmi

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.group4syetc.facetobmi.databinding.ActivityMainBinding
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import com.group4syetc.facetobmi.ml.Model1
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder


class MainActivity : AppCompatActivity() {

    private lateinit var bm: Bitmap
    private lateinit var button: Button
    private lateinit var button1: Button
    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button1)
        button1 = findViewById(R.id.button2)
        imageView = findViewById(R.id.ImageView1)

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
            val model = Model1.newInstance(this)

// Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)
            val btbff = convertBitmapToByteBuffer(bm)
            inputFeature0.loadBuffer(btbff!!)

// Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

// Releases model resources if no longer used.
            model.close()
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer? {
        val  BATCH_SIZE = 1
        val PIXEL_SIZE = 3
        val inputSize = 128

        val byteBuffer: ByteBuffer
        byteBuffer = ByteBuffer.allocateDirect(
            1 * BATCH_SIZE * inputSize * inputSize * PIXEL_SIZE
        )
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(inputSize * inputSize)
        bitmap.getPixels(
            intValues, 0, bitmap.width, 0, 0,
            bitmap.width, bitmap.height
        )
        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val `val` = intValues[pixel++]
                byteBuffer.putFloat(
                    (((`val` shr 16 and 0xFF) - 1.0) / 1.0).toFloat()
                )
                byteBuffer.putFloat(
                    (((`val` shr 8 and 0xFF) - 1.0) / 1.0).toFloat()
                )
                byteBuffer.putFloat((((`val` and 0xFF) - 1.0) / 1.0).toFloat())
            }
        }

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
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
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

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageView?.setImageURI(data?.data)
            val drawable = imageView!!.drawable as BitmapDrawable
            bm = drawable.bitmap
        }
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        //Permission code
        private const val PERMISSION_CODE = 1001
    }
}