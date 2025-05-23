package com.jk.cashcontrol.presentation.login

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CaptureFailure
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jk.cashcontrol.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.log

class GoogleSignInUtils {

    companion object {
        fun doGoogleSignIn(
            context: Context,
            scope : CoroutineScope,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
            onSuccess : (FirebaseUser) -> Unit,
            onFailure: (Exception) -> Unit
        ) {

            val credentialManager = CredentialManager.create(context)

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions(context))
                .build()

            try {
                scope.launch {

                    val result = credentialManager.getCredential(context,request)

                    when(result.credential) {
                        is CustomCredential -> {

                            if(result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)

                                val googleTokenId = googleIdTokenCredential.idToken

                                val authCredential = GoogleAuthProvider.getCredential(googleTokenId, null)

                                val user = Firebase.auth.signInWithCredential(authCredential).await().user

                                if(user != null) {
                                    onSuccess(user)
                                }
                                else {
                                    onFailure(Exception("Firebase user is null"))
                                }

//                                user?.let {
//                                    if(it.isAnonymous.not()) {
//                                        login.invoke()
//                                    }
//                                }
                            }
                        }
                        else -> {}
                    }
                }

            } catch (e : NoCredentialException) {
                launcher?.launch(getIntent())
            } catch (e : GetCredentialException) {
                onFailure(e)
            }

        }

        fun getCredentialOptions(context: Context) : CredentialOption {
            return GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .build()

        }

        fun getIntent() : Intent {
            return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }
        }
    }
}