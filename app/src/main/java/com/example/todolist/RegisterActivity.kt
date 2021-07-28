package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration);

        registerButton.setOnClickListener{
            val email=emailInput.text.toString()
            val password=passwordInput.text.toString()
            if(email.isEmpty()||password.isEmpty()){
                Toast.makeText(this,"Please enter text in email and/or password section!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(isValidEmail(email)==false){
                Toast.makeText(this,"Email is not in the right format!!!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            if(isValidPasswordFormat(password)==false){
                Toast.makeText(this,"Password is not in the correct format must be at least 5 characters and contain at least 1 lower case ,1 upper case character and 1 special character",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if(!it.isSuccessful){
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        return@addOnCompleteListener
                    }
                }

        }
    }
    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPasswordFormat(password: String): Boolean {
        val passwordREGEX = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=!.])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{5,}" +               //at least 8 characters
                "$");
        return passwordREGEX.matcher(password).matches()
    }

}