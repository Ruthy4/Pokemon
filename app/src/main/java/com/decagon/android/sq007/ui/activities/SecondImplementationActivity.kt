package com.decagon.android.sq007.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.decagon.android.sq007.R
import kotlinx.android.synthetic.main.activity_second_implementation.*

class SecondImplementationActivity : AppCompatActivity() {
    private val REQUEST_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_implementation)

        val selectImage = findViewById<ImageView>(R.id.select_imageIV)
        selectImage.setOnClickListener {
            uploadImageFromGallery()
        }

        checkForPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, "Access storage", REQUEST_CODE)
    }

    // method to check for user permission
    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this, permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission, name, requestCode
                )

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    // function to check the result from permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else {
                uploadImageFromGallery()
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
        }
        when (requestCode) {
            REQUEST_CODE -> innerCheck("calls")
        }
    }

    // build the permission and show the permission dialog
    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@SecondImplementationActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //
    private fun uploadImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            select_imageIV.setImageURI(data?.data)
        }
    }
}
