package com.example.waygofrontend.data.firebase

import com.example.waygofrontend.data.model.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class FirebaseAuthHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // --- Auth operations (Task-based, caller handles coroutine) ---

    fun signIn(email: String, password: String): com.google.android.gms.tasks.Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, password)

    fun signUp(email: String, password: String, displayName: String?): com.google.android.gms.tasks.Task<AuthResult> =
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { result ->
            displayName?.let { name ->
                result.user?.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(name).build()
                )
            }
        }

    fun signOut() {
        auth.signOut()
    }

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    // --- Map FirebaseUser -> User model ---
    fun toUserModel(firebaseUser: FirebaseUser?): User? {
        if (firebaseUser == null) return null
        return User(
            id = firebaseUser.uid,
            firebaseUid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName,
            photoUrl = firebaseUser.photoUrl?.toString(),
            syncedAt = System.currentTimeMillis(),
            createdAt = System.currentTimeMillis()
        )
    }
}
