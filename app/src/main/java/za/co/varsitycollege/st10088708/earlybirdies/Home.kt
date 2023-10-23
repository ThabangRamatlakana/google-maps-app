package za.co.varsitycollege.st10088708.earlybirdies

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import za.co.varsitycollege.st10088708.earlybirdies.databinding.ActivityHomeBinding
import java.io.IOException

class Home : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityHomeBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var bottomNavigationView: BottomNavigationView

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // Initialize the Places API client
        Places.initialize(applicationContext, getString(R.string.api_key))
        placesClient = Places.createClient(this)

        // Initialize map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Set up search button click listener
        val searchButton: Button = findViewById(R.id.searchButton)
        searchButton.setOnClickListener {
            searchLocation()
        }

        // Retrieve the saved distance and unit from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val savedDistanceValue = sharedPreferences.getString("distanceValue", "")
        val savedDistanceUnit = sharedPreferences.getString("distanceUnit", "")

        // Display the saved distance and unit as text in the TextView
        val distanceTextView = findViewById<TextView>(R.id.textViewDistance)
        distanceTextView.text = "Distance: $savedDistanceValue $savedDistanceUnit"

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
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
                    startActivity(Intent(applicationContext, Add::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                else -> false // Handle other cases here
            }
        }



    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Customize map settings
        mMap.uiSettings.isZoomControlsEnabled = true

        // Set up map markers and current location
        setUpMap()
    }


    private fun setUpMap() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }

        // Enable location on the map
        mMap.isMyLocationEnabled = true

        // Clear old markers before adding a new one
        mMap.clear()

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            location?.let {
                val currentLatLong = LatLng(location.latitude, location.longitude)

                // Log the received location for debugging
                Log.d("Location", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")

                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
            } ?: run {
                // Handle the case where location is null
                Log.e("Location", "Last known location is null")
            }
        }.addOnFailureListener { e ->
            // Handle any failure to get location
            Log.e("Location", "Error getting last known location: ${e.message}", e)
        }
    }


    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker) = false

    private fun searchLocation() {
        val location = binding.searchBar.text.toString().trim()
        var addressList: List<Address>? = null

        if (location.isEmpty()) {
            showToast("Provide search details")
            return
        }

        val geoCoder = Geocoder(this)
        try {
            addressList = geoCoder.getFromLocationName(location, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (addressList != null && addressList.isNotEmpty()) {
            val address = addressList[0]
            val latLng = LatLng(address.latitude, address.longitude)
            mMap.addMarker(MarkerOptions().position(latLng).title(location))
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        } else {
            showToast("Location not found")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }




}