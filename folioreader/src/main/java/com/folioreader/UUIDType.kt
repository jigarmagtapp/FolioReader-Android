package com.olm.magtapp.util

import com.folioreader.Constants
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or


object UUIDType {
    private val UTF8 = Charset.forName("UTF-8")
    val NAMESPACE = UUID.fromString(Constants.UUID_EPUB_FILEPATH)
    fun nameUUIDFromNamespaceAndString(namespace: UUID, name: String): UUID {
        return nameUUIDFromNamespaceAndBytes(namespace, Objects.requireNonNull(name, "name == null").toByteArray(UTF8))
    }

    fun nameUUIDFromNamespaceAndBytes(namespace: UUID, name: ByteArray): UUID {
        val md: MessageDigest = try {
            MessageDigest.getInstance("SHA-1")
        } catch (nsae: NoSuchAlgorithmException) {
            throw InternalError("SHA-1 not supported")
        }
        md.update(toBytes(Objects.requireNonNull(namespace, "namespace is null")))
        md.update(Objects.requireNonNull(name, "name is null"))
        val sha1Bytes = md.digest()
        sha1Bytes[6] = sha1Bytes[6] and 0x0f /* clear version        */
        sha1Bytes[6] = sha1Bytes[6] or 0x50 /* set to version 5     */
        sha1Bytes[8] = sha1Bytes[8] and 0x3f /* clear variant        */
        sha1Bytes[8] = sha1Bytes[8] or 0x80.toByte() /* set to IETF variant  */
        return fromBytes(sha1Bytes)
    }

    private fun fromBytes(data: ByteArray): UUID {
        // Based on the private UUID(bytes[]) constructor
        var msb: Long = 0
        var lsb: Long = 0
        assert(data.size >= 16)
        for (i in 0..7) msb = msb shl 8 or ((data[i] and 0xff.toByte()).toLong())
        for (i in 8..15) lsb = lsb shl 8 or ((data[i] and 0xff.toByte()).toLong())
        return UUID(msb, lsb)
    }

    private fun toBytes(uuid: UUID): ByteArray {
        // inverted logic of fromBytes()
        val out = ByteArray(16)
        val msb = uuid.mostSignificantBits
        val lsb = uuid.leastSignificantBits
        for (i in 0..7) out[i] = (msb shr (7 - i) * 8 and 0xff).toByte()
        for (i in 8..15) out[i] = (lsb shr (15 - i) * 8 and 0xff).toByte()
        return out
    }
}