package com.example.todolist

import android.content.ContentValues.TAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_todo.view.*


class TodoAdapter(
    private val todos: MutableList<Todo>


) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    val db = Firebase.firestore
    val currentUser = FirebaseAuth.getInstance()!!.currentUser
    val myId = currentUser!!.uid;


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    fun addTodos(todo:Todo){
        todos.add(todo)
        notifyItemInserted(todos.size - 1)

    }

    fun addTodo(todo:Todo,id:String) {



        val ref: DocumentReference = db.collection("users").document(id).collection("task").document()
        val myId = ref.id;
        todo.id=myId;
        // Create a new user with a first and last name
        val user = hashMapOf(
            "title" to todo.title,
            "check" to  todo.isChecked,
            "id" to     todo.id
        )
// Add a new document with a generated ID
           ref.set(user)
        todos.add(todo)

        notifyItemInserted(todos.size - 1)
    }

    fun deleteDoneTodos(id:String) {


        for(element in todos) {
            if(element.isChecked==true){
                db.collection("users").document(id).collection("task").document(element.id)
                    .delete()
            }

        }
        todos.removeAll { todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }




    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {

        if(isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
            for(item in todos){
                if(item.isChecked==true){
                    db.collection("users").document(myId).collection("task").document(item.id).update("check", true)
                }
            }

        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            for(item in todos) {

                if (item.isChecked == false) {
                    db.collection("user").document(myId).collection("task").document(item.id).update("check", false)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int,) {
        val curTodo = todos[position]
        holder.itemView.apply {
            tvTodoTitle.text = curTodo.title
            cbDone.isChecked = curTodo.isChecked
            toggleStrikeThrough(tvTodoTitle, curTodo.isChecked)
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(tvTodoTitle, isChecked)
                curTodo.isChecked = !curTodo.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}


















