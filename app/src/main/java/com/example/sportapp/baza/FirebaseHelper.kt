package com.example.sportapp.baza

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

object FirebaseHelper {
    var username = mutableStateOf("")
    fun registerUser(email: String, nickname: String, password: String){
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            result ->
            Firebase.firestore.collection("users").document(result.user!!.uid).set(hashMapOf(result.user!!.uid to nickname))
            Log.d("Registration", "Registration succeeded")
        }.addOnFailureListener {
                result ->
            Log.d("Registration", "Registration failed")
        }
    }

    fun signInUser(email: String, password: String){
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                result ->
            Log.d("Login", "Login succeeded")
        }.addOnFailureListener {
                result ->
            Log.d("Login", "Login failed")
        }
    }

    fun getCurrentUser(): FirebaseUser?{
        return Firebase.auth.currentUser
    }

    fun getCurrentUsername(){
        Firebase.firestore.collection("users").document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            result ->

            username.value = result.data!![Firebase.auth.currentUser!!.uid].toString()
        }
    }
}