package com.example.shardingjdbc.encrypt

import org.apache.shardingsphere.encrypt.spi.QueryAssistedEncryptAlgorithm

/**
 * solve No implementation class load from SPI `org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm` with type `assistedTest`.
 */
class TestAssistedEncryptAlgorithm: QueryAssistedEncryptAlgorithm {

    override fun init() {}

    override fun encrypt(plaintext: Any?): String {
        return "encryptValue"
    }

    override fun decrypt(ciphertext: String?): Any {
        return "decryptValue"
    }

    override fun queryAssistedEncrypt(plaintext: String?): String {
        return "assistedEncryptValue"
    }

    override fun getType(): String {
        return "assistedTest"
    }
}