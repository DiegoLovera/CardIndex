package com.diegolovera.cardindex.security

import android.util.Base64
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtility {
    private const val characterEncoding = "UTF-8"
    private const val cipherTransformation = "AES/CBC/PKCS5Padding"
    private const val aesEncryptionAlgorithm = "AES"
    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun decrypt(
        cipherToText: ByteArray,
        key: ByteArray,
        initialVector: ByteArray
    ): ByteArray {
        var cipherText = cipherToText
        val cipher = Cipher.getInstance(cipherTransformation)
        val secretKeySpec =
            SecretKeySpec(key,
                aesEncryptionAlgorithm
            )
        val ivParameterSpec =
            IvParameterSpec(initialVector)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        cipherText = cipher.doFinal(cipherText)
        return cipherText
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun encrypt(
        plainToText: ByteArray,
        key: ByteArray,
        initialVector: ByteArray
    ): ByteArray {
        var plainText = plainToText
        val cipher = Cipher.getInstance(cipherTransformation)
        val secretKeySpec =
            SecretKeySpec(key,
                aesEncryptionAlgorithm
            )
        val ivParameterSpec =
            IvParameterSpec(initialVector)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        plainText = cipher.doFinal(plainText)
        return plainText
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getKeyBytes(key: String): ByteArray {
        val keyBytes = ByteArray(16)
        val parameterKeyBytes = key.toByteArray(charset(characterEncoding))
        System.arraycopy(
            parameterKeyBytes,
            0,
            keyBytes,
            0,
            Math.min(parameterKeyBytes.size, keyBytes.size)
        )
        return keyBytes
    }

    @Throws(
        UnsupportedEncodingException::class,
        InvalidKeyException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun encrypt(plainText: String, key: String): String {
        val plainTextBytes = plainText.toByteArray(charset(characterEncoding))
        val keyBytes =
            getKeyBytes(key)
        return Base64.encodeToString(
            encrypt(
                plainTextBytes,
                keyBytes,
                keyBytes
            ),
            Base64.DEFAULT
        )
    }

    @Throws(GeneralSecurityException::class, IOException::class)
    fun decrypt(encryptedText: String?, key: String): String {
        val cipheredBytes =
            Base64.decode(encryptedText, Base64.DEFAULT)
        val keyBytes =
            getKeyBytes(key)
        return String(
            decrypt(
                cipheredBytes,
                keyBytes,
                keyBytes
            ), charset(characterEncoding))
    }
}