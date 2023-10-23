package za.co.varsitycollege.st10088708.earlybirdies

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.github.dhaval2404.imagepicker.ImagePicker
import android.widget.*
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import za.co.varsitycollege.st10088708.earlybirdies.databinding.ActivityAddBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class Add : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var storage: FirebaseStorage
    private lateinit var clearButton: Button
    private lateinit var saveButton : Button
    private lateinit var binding: ActivityAddBinding
    private lateinit var database: DatabaseReference
    private lateinit var imageUri: Uri
    private lateinit var birdName: EditText
    private lateinit var birdLocation : EditText
    private lateinit var birdDescription : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val placeholderDrawable = resources.getDrawable(R.drawable.cameraicon)
        database = FirebaseDatabase.getInstance().getReference("Birds")
        storage = FirebaseStorage.getInstance()
        clearButton = findViewById(R.id.clearButton)
        saveButton = findViewById(R.id.saveButton)
        birdDescription = findViewById(R.id.uploadBirdDescription)
        birdLocation = findViewById(R.id.uploadBirdLocation)
        birdName = findViewById(R.id.uploadBirdName)

        binding.uploadImage.setOnClickListener {
            selectImage()
        }


        binding.saveButton.setOnClickListener {
            val birdName = binding.uploadBirdName.text.toString()
            val birdLocation = binding.uploadBirdLocation.text.toString()
            val birdDescription = binding.uploadBirdDescription.text.toString()

            val birds = BirdData(imageUri.toString(), birdName, birdLocation, birdDescription)

            database.child(birdName).setValue(birds)
                .addOnSuccessListener {
                    binding.uploadBirdName.text.clear()
                    binding.uploadBirdLocation.text.clear()
                    binding.uploadBirdDescription.text.clear()

                    Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()

                    // Start the birdsList activity
                    val intent = Intent(this, Gallery::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to save data: ${exception.message}", Toast.LENGTH_SHORT).show()
                    Log.e(ContentValues.TAG, "Error writing data to Firebase: ${exception.message}", exception)
                }
            uploadImage()
        }

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(applicationContext, Home::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.navigation_gallery -> {
                    startActivity(Intent(applicationContext, Gallery::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.navigation_trophy -> {
                    startActivity(Intent(applicationContext, Achievements::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.navigation_settings -> {
                    startActivity(Intent(applicationContext, Settings::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.navigation_add -> {
                    true
                }
                else -> false // Handle other cases here
            }


        }

        clearButton.setOnClickListener {
            birdName.text.clear()
            birdLocation.text.clear()
            birdDescription.text.clear()

            val imageView: ImageView = findViewById(R.id.uploadImage)
            imageView.setImageDrawable(placeholderDrawable)
        }


    }
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            binding.uploadImage.setImageURI(imageUri)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading")
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                binding.uploadImage.setImageURI(null)
                Toast.makeText(this@Add, "Successfully Uploaded", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
            .addOnFailureListener {
                if (progressDialog.isShowing) progressDialog.dismiss()

            }
    }



}