package com.heybys.oddments.security.oauth2.user

class FacebookOAuth2UserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {

    override fun getId(): String {
        return attributes["id"] as? String ?: ""
    }

    override fun getName(): String? {
        return attributes["name"] as? String
    }

    override fun getEmail(): String? {
        return attributes["email"] as? String
    }

    override fun getImageUrl(): String? {
        val picture = attributes["picture"] as? Map<String, Any> ?: return null
        val data = picture["data"] as? Map<String, Any> ?: return null
        return data["url"] as? String
    }
}
