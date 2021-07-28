package com.example.todolist

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class AccountActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private var mAuth: FirebaseAuth? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoAdapter = TodoAdapter(mutableListOf())
        val db = Firebase.firestore
        val mutableList: MutableList<Todo> = arrayListOf()
        mAuth = FirebaseAuth.getInstance();

        val currentUser = mAuth!!.currentUser
        if (currentUser == null) {
            // No user is signed in
        } else {
            // User logged in
            val ref: DocumentReference = db.collection("users").document()
            val myId = ref.id;

            println("the user id is "+ myId);
            println("the current user id "+ currentUser.uid)
        }


             db.collection("users").document(currentUser!!.uid).collection("task").orderBy("title",Query.Direction.DESCENDING)
                 .get()
                 .addOnSuccessListener { result ->
                     for (document in result) {
                         var todo = Todo(
                             document.get("title").toString(),
                             document.get("check") as Boolean,
                             document.id
                         );
                         todoAdapter.addTodos(todo)

                     }

                 }



        rvTodoItems.adapter = todoAdapter
        rvTodoItems.layoutManager = LinearLayoutManager(this)

        btnAddTodo.setOnClickListener {
            val todoTitle = etTodoTitle.text.toString()
            if (todoTitle.isNotEmpty()) {

                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo,currentUser!!.uid)
                etTodoTitle.text.clear()
            }
        }
        btnDeleteDoneTodos.setOnClickListener {
            todoAdapter.deleteDoneTodos(currentUser!!.uid)
        }
    }
}