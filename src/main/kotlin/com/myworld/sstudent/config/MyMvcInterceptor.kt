package com.myworld.sstudent.config

import com.myworld.sstudent.security.JwtUtil
import com.myworld.sstudent.security.TokenPublicKeyGenerator
import com.myworld.sstudent.common.SimpleUser
import com.myworld.sstudent.common.UserContextHolder
import org.apache.commons.lang.StringUtils
import org.apache.logging.log4j.LogManager
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList


class MyMvcInterceptor : HandlerInterceptor {
    private val log = LogManager.getRootLogger()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val enu: Enumeration<*> = request.headerNames
        while (enu.hasMoreElements()) {
            val paraName = enu.nextElement() as String
            // log.info("【request中的Header参数】" + paraName + "值为:" + request.getHeader(paraName))
        }
        val params: Enumeration<*> = request.parameterNames
        while (params.hasMoreElements()) {
            val one = params.nextElement() as String
            // log.info("【request中的Parameter参数】" + one + "值为:" + request.getParameter(one))
        }
        // 从request header中提取出jwt,
        // 通过feignClient向auth鉴权后，或者使用公钥解开token中的部分内容
        // 将认证通过的用户信息，写入Sstudent模块的用户上下文UserContextHolder中，方便后续的用户信息提取
        val token = request.getHeader(JwtUtil.HEADER_AUTH)
        if (token.isNullOrEmpty()) {
            return false
        }
        // 使用公钥解析token后并进行token验证
        val publicKey = TokenPublicKeyGenerator.publicKey
        val map = JwtUtil.parseToken(token, publicKey)
        val wid = map[JwtUtil.HEADER_WID] as String
        val roles = map[JwtUtil.HEADER_ROLE] as ArrayList<*>

        // 根据获取的信息填写simpleUser内容
        val simpleUser = SimpleUser(wid = wid, playerType = roles.toHashSet(), token = token)
        UserContextHolder.setUserContext(simpleUser)
        return true
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any?, modelAndView: ModelAndView?) {}

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any?, ex: Exception?) {
        UserContextHolder.shutdown()
    }
}
