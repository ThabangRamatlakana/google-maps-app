package za.co.varsitycollege.st10088708.earlybirdies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import za.co.varsitycollege.st10088708.earlybirdies.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_sign_up)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            val name = binding.signupName.text.toString()
            val surname = binding.signupSurname.text.toString()
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirm.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty())
            {
                if(password == confirmPassword)
                {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener()
                    {
                        if(it.isSuccessful)
                        {
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        }
                        else
                        {
                            Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else
                {
                    Toast.makeText(this,"Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this,"Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginRedirectText.setOnClickListener()
        {
            val loginIntent = Intent(this, MainActivity:: class.java)
            startActivity(loginIntent)
        }
    }
}