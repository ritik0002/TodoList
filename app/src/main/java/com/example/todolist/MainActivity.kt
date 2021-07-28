package com.example.todolist


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.AccountActivity
import com.example.todolist.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login.*
import com.google.firebase.auth.FirebaseAuth as FirebaseAuth


class MainActivity : AppCompatActivity() {

    private var email: EditText? = null
    private var password: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener:FirebaseAuth.AuthStateListener?=null
    var database = FirebaseDatabase.getInstance().reference

    var numb = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance()


        loginBtn.setOnClickListener {
            val txt_email = emailField.getText().toString()
            val txt_password = passwordField.getText().toString()
            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                Toast.makeText(this@MainActivity, "Empty Credentials!", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(txt_email, txt_password)
            }
        }
        registerBtn.setOnClickListener{
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

    }
    private fun loginUser(email: String, password: String) {
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@MainActivity,AccountActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                Toast.makeText(
                    this@MainActivity,
                    "Sign In Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@MainActivity,
                    "Sign In Errors",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    fun test(){
        println("hello");
    }
}
