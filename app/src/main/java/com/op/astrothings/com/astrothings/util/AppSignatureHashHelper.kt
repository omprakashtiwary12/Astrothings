package com.op.astrothings.com.astrothings.util

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class AppSignatureHashHelper(context: Context?) :
    ContextWrapper(context) {// Get all package details
 
    val appSignatures: ArrayList<String>
        get() {
            val appSignaturesHashs = ArrayList<String>()
            try {
                // Get all package details
                val packageName = packageName
                val packageManager = packageManager
                val signatures = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                ).signatures
                if (signatures != null) {
                    for (signature in signatures) {
                        val hash = hash(packageName, signature.toCharsString())
                        if (hash != null) {
                            appSignaturesHashs.add(String.format("%s", hash))
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Package not found")
            }
            return appSignaturesHashs
        }

    companion object {
        val TAG = AppSignatureHashHelper::class.java.simpleName
        private const val HASH_TYPE = "SHA-256"
        const val NUM_HASHED_BYTES = 9
        const val NUM_BASE64_CHAR = 11
        private fun hash(packageName: String, signature: String): String? {
            val appInfo = "$packageName $signature"
            try {
                val messageDigest = MessageDigest.getInstance(HASH_TYPE)
                messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
                var hashSignature = messageDigest.digest()

                // truncated into NUM_HASHED_BYTES
                hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
                // encode into Base64
                var base64Hash =
                    Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
                base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)
                return base64Hash
            } catch (e: NoSuchAlgorithmException) {
                Timber.tag(TAG).e(e, "No Such Algorithm Exception")
            }
            return null
        }
    }
}