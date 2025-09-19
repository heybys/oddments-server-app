package com.heybys.oddments.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.SerializationUtils
import java.util.Base64

object CookieUtils {

    fun getCookie(request: HttpServletRequest, name: String): Cookie? {
        val cookies = request.cookies ?: return null
        return cookies.find { it.name == name }
    }

    fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        val cookie =
            Cookie(name, value).apply {
                path = "/"
                isHttpOnly = true
                setMaxAge(maxAge)
            }
        response.addCookie(cookie)
    }

    fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
        val cookies = request.cookies ?: return
        cookies
            .filter { it.name == name }
            .forEach { cookie ->
                cookie.apply {
                    value = ""
                    path = "/"
                    maxAge = 0
                }
                response.addCookie(cookie)
            }
    }

    fun serialize(obj: Any): String {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj))
    }

    fun <T> deserialize(cookie: Cookie, clazz: Class<T>): T {
        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.value)))
    }
}
