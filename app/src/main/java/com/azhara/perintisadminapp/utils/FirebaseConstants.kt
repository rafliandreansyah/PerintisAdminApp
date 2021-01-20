package com.azhara.perintisadminapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseConstants {
    val firebaseDb = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
}