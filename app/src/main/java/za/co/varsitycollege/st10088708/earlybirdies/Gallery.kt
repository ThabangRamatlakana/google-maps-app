package za.co.varsitycollege.st10088708.earlybirdies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.ArrayList
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.zip.DataFormatException
import com.google.android.material.bottomnavigation.BottomNavigationView

class Gallery : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var birdRecyclerView: RecyclerView
    private lateinit var birdArrayList: ArrayList<BirdData>
    private lateinit var adapter: MyAdapter
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)


        birdRecyclerView = findViewById(R.id.recycleView)
        birdRecyclerView.layoutManager = LinearLayoutManager(this)
        birdRecyclerView.setHasFixedSize(true)

        birdArrayList = arrayListOf()
        adapter = MyAdapter(birdArrayList)
        birdRecyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : MyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val selectedBird = birdArrayList[position]

                val dialogBuilder = AlertDialog.Builder(this@Gallery)
                dialogBuilder.setTitle("Bird Details")
                    .setMessage("Name: ${selectedBird.birdName}\nLocation: ${selectedBird.birdLocation}\nDescription: ${selectedBird.birdDescription}")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        })


        dbRef = FirebaseDatabase.getInstance().getReference("Birds")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                birdArrayList.clear()
                for (sneakerSnapshot in snapshot.children) {
                    val sneaker = sneakerSnapshot.getValue(BirdData::class.java)
                    sneaker?.let { birdArrayList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
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


}