package za.co.varsitycollege.st10088708.earlybirdies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import za.co.varsitycollege.st10088708.earlybirdies.databinding.ActivityAchievementsBinding

class Achievements : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var collectionRef: DatabaseReference
    private lateinit var binding: ActivityAchievementsBinding
    private var totalItems: Int = 0
    private lateinit var totalItemsTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var image1: ImageView
    private lateinit var image2: ImageView
    private lateinit var image3: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

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

                    true
                }
                R.id.navigation_settings -> {
                    startActivity(Intent(applicationContext, Settings::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.navigation_add -> {
                    startActivity(Intent(applicationContext, Add::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                else -> false // Handle other cases here
            }
        }
        totalItemsTextView = findViewById(R.id.totalItemsTextView)
        progressBar = findViewById(R.id.progressBar)
        image1 = findViewById(R.id.bronze)
        image2 = findViewById(R.id.silver)
        image3 = findViewById(R.id.gold)

        // Set the initial opacity of images to low
        setLowOpacity()

        // Update the totalItemsTextView, progressBar, and image opacity with the initial value of totalItems
        updateTotalItems(totalItems)

        collectionRef = FirebaseDatabase.getInstance().getReference("Birds")
        collectionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get the count of entries in the database
                val count = dataSnapshot.childrenCount.toInt()

                // Update the totalItems value with the count
                updateTotalItems(count)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors
                Toast.makeText(applicationContext, "Failed to read data from database.", Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun updateTotalItems(count: Int) {
        // Limit the totalItems value to be within the range of 0 to 10

        totalItems = count.coerceIn(0, 10)
        getCountOfItems()

        // Update the totalItemsTextView with the updated value
        totalItemsTextView.text = "$totalItems out of 10"

        // Calculate the progress percentage
        val progress = (totalItems)

        // Update the progress bar with the calculated progress
        progressBar.progress = (progress * 10)
        progressBar.setProgress(progress, true)


        // Update the image opacity based on the totalItems value
        when (totalItems) {
            0-> setLowOpacity()  // For totalItems 0 and 1, set opacity to low
            in 1..2 -> setBronzeOpacity() // For totalItems 1 to 3, set opacity to default
            in 3..9 -> setSilverOpacity() // For totalItems 3 to 10, set opacity to default
            10 -> setDefaultOpacity() // For totalItems 10, set opacity to default
        }
    }

    private fun setLowOpacity() {
        image1.alpha = 0.3f
        image2.alpha = 0.3f
        image3.alpha = 0.3f
    }

    private fun setBronzeOpacity(){
        image1.alpha = 1.0f
        image2.alpha = 0.3f
        image3.alpha = 0.3f
    }

    private fun setSilverOpacity(){
        image1.alpha = 1.0f
        image2.alpha = 1.0f
        image3.alpha = 0.3f
    }

    private fun setDefaultOpacity() {
        image1.alpha = 1.0f
        image2.alpha = 1.0f
        image3.alpha = 1.0f
    }

    private lateinit var databaseReference: DatabaseReference

    private fun getCountOfItems() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Birds")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val itemCount = dataSnapshot.childrenCount
                totalItems = itemCount.toInt()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }




}
