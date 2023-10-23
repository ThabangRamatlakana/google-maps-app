package za.co.varsitycollege.st10088708.earlybirdies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class Settings : AppCompatActivity() {

    private lateinit var saveButton : Button

    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val distanceEditText = findViewById<EditText>(R.id.distanceEditText)
        val unitTextView = findViewById<TextView>(R.id.unitTextView)
        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            saveDistance()
        }


        distanceEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for this example
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check which radio button is selected and update the unit accordingly
                val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
                val selectedRadioButtonId = radioGroup.checkedRadioButtonId

                val unit = when (selectedRadioButtonId) {
                    R.id.radioButtonOption1 -> "m"
                    R.id.radioButtonOption2 -> "km"
                    else -> "" // Handle other cases if needed
                }

                unitTextView.text = unit
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed for this example
            }
        })


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
    }

    fun saveDistance() {
        val distanceEditText = findViewById<EditText>(R.id.distanceEditText)
        val distanceValue = distanceEditText.text.toString()

        // Check which radio button is selected and get the corresponding unit
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        val unit = when (selectedRadioButtonId) {
            R.id.radioButtonOption1 -> "m"
            R.id.radioButtonOption2 -> "km"
            else -> "" // Handle other cases if needed
        }

        // Save the distanceValue and unit to SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("distanceValue", distanceValue)
        editor.putString("distanceUnit", unit)
        editor.apply()

        // Display a message to indicate that the distance is saved
        Toast.makeText(this, "Distance saved successfully: $distanceValue $unit", Toast.LENGTH_SHORT).show()

        // Redirect to the Home activity or perform any other desired action
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

}