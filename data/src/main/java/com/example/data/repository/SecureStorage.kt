package com.example.data.repository

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class SecureStorage @Inject constructor(
    private val context: Context
) {
    private val cryptoManager = CryptoManager()

    fun getAccessToken(): String? {
        return try {
            val sharedPrefs = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
            val encryptedToken = sharedPrefs.getString("access_token", null)
            encryptedToken?.let { cryptoManager.decrypt(it) }
        } catch (e: Exception) {
            null
        }
    }

    fun saveAccessToken(token: String) {
        try {
            val sharedPrefs = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
            val encryptedToken = cryptoManager.encrypt(token)
            sharedPrefs.edit().putString("access_token", encryptedToken).apply()
        } catch (e: Exception) {
            // Log error
        }
    }

    fun clearAccessToken() {
        val sharedPrefs = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().remove("access_token").apply()
    }
}

class CryptoManager {
    private val cipher = Cipher.getInstance("AES/GCM/NoPadding")

    fun encrypt(plaintext: String): String {
        val key = getOrCreateKey()
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plaintext.toByteArray())
        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }

    fun decrypt(ciphertext: String): String {
        val key = getOrCreateKey()
        val data = Base64.decode(ciphertext, Base64.DEFAULT)
        val iv = data.copyOfRange(0, 12)
        val encrypted = data.copyOfRange(12, data.size)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted)
    }

    private fun getOrCreateKey(): SecretKey {
        // Simplified - use Android KeyStore in production
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        if (!keyStore.containsAlias("github_token_key")) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keySpec = KeyGenParameterSpec.Builder(
                "github_token_key",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).apply {
                setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                setUserAuthenticationRequired(false)
                setRandomizedEncryptionRequired(true)
            }.build()
            keyGenerator.init(keySpec)
            keyGenerator.generateKey()
        }

        return keyStore.getKey("github_token_key", null) as SecretKey
    }
}