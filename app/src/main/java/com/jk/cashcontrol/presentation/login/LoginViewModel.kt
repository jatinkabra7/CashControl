package com.jk.cashcontrol.presentation.login

import android.content.Context
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.User
import com.jk.cashcontrol.domain.repository.TransactionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

class LoginViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    val user = MutableLiveData<User>(null)

    fun handleGoogleSignIn(
        context: Context,
        navigateToHome : () -> Unit
    ) {
        viewModelScope.launch {
            googleSignIn(context).collect { result ->
                result.fold(
                    onSuccess = {authResult ->
                        val currentUser = authResult.user

                        if(currentUser != null) {
                            user.value = User(
                                id = currentUser.uid,
                                name = currentUser.displayName!!,
                                imageUrl = currentUser.photoUrl.toString(),
                                email = currentUser.email!!
                            )

                            insertUser()

                            Toast.makeText(context,"Logged In", Toast.LENGTH_SHORT).show()
                            navigateToHome()
                        }
                    },
                    onFailure = {e ->
                        Toast.makeText(context,"Something went wrong : ${e.message}", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    private fun googleSignIn(context: Context) : Flow<Result<AuthResult>> {

        val firebaseAuth = FirebaseAuth.getInstance()

        return callbackFlow {
            try {
                val credentialManager = CredentialManager.create(context)

                val ranNonce: String = UUID.randomUUID().toString()
                val bytes: ByteArray = ranNonce.toByteArray()
                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
                val digest: ByteArray = md.digest(bytes)
                val hashedNonce: String = digest.fold("") { str, it -> str + "%02x".format(it) }

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .setNonce(hashedNonce)
                    .setAutoSelectEnabled(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(context,request)
                val credential = result.credential

                if(credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                    val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                    trySend(Result.success(authResult))
                } else {
                    throw RuntimeException("Received an invalid credential type")
                }
            } catch (e : GetCredentialCancellationException) {
                trySend(Result.failure(Exception("Sign-in was cancelled")))
            } catch (e : Exception) {
                trySend(Result.failure(e))
            }

            awaitClose {  }
        }
    }

    fun insertUser() {
        viewModelScope.launch {
            repository.insertUser()
        }
    }
}