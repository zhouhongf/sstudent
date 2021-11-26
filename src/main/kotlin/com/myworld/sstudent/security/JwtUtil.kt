package com.myworld.sstudent.security


import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.crypto.MacProvider
import org.apache.logging.log4j.LogManager
import java.io.Serializable
import java.security.PrivateKey
import java.security.PublicKey
import java.util.*


object JwtUtil : Serializable {
    private val log = LogManager.getRootLogger()

    const val HEADER_AUTH = "Authorization"
    const val TOKEN_PREFIX = "Bearer"
    const val TOKEN_TYPE = "tokenType"
    const val HEADER_SERVICE = "serviceName"
    const val HEADER_ROLE = "role"
    const val HEADER_WID = "wid"
    const val EXPIRE_TIME = "expireTime"
    const val GROUP = "group"
    const val GROUP_NAME = "xinheJingrong"

    @JvmStatic
    fun parseToken(token: String, publicKey: PublicKey): Map<String, Any> {
        val map: Map<String, Any> = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).body
        log.info("解析出来的token body是{}", map.toString())
        return map
    }
}
