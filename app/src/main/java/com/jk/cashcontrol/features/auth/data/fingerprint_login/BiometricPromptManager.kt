package com.jk.cashcontrol.features.auth.data.fingerprint_login

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricPromptManager(private val activity: FragmentActivity) {

    fun showBiometricPromptIfAvailable(
        title: String,
        description: String,
        onSuccess: () -> Unit
    ) {

        val biometricManager = BiometricManager.from(activity)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .setConfirmationRequired(false)
            .build()

        when(biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BIOMETRIC_SUCCESS -> {
                val executor = ContextCompat.getMainExecutor(activity)

                val biometricPrompt = BiometricPrompt(
                    activity,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            onSuccess()
                        }

                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            Toast.makeText(activity,errString, Toast.LENGTH_SHORT).show()
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            Toast.makeText(activity,"Authentication Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                biometricPrompt.authenticate(promptInfo)
            }
            else -> {
                Toast.makeText(activity, "Fingerprint not available or not set up", Toast.LENGTH_SHORT).show()
            }
        }
    }
}