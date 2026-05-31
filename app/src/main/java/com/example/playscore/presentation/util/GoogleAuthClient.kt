package com.example.playscore.presentation.util

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.example.playscore.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject

class GoogleAuthClient @Inject constructor() {

    suspend fun getGoogleIdToken(context: Context): String {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = try {
            credentialManager.getCredential(
                context = context,
                request = request
            )
        } catch (e: NoCredentialException) {
            throw IllegalStateException("No Google account was selected.")
        }

        val credential = result.credential

        val googleIdTokenCredential = GoogleIdTokenCredential
            .createFrom(credential.data)

        return googleIdTokenCredential.idToken
    }
}
