package com.home.piperbike.auth

import com.home.piperbike.api.shared.dto.DtoSession
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

open class TokenAuthentication(token: String) : Authentication {
    private var _authorities = mutableListOf<GrantedAuthority>()
    private var _credentials: String = token
    private var _principal: DtoSession? = null
    private var _isAuthenticated: Boolean = false

    override fun getAuthorities(): MutableCollection<GrantedAuthority> = this._authorities

    override fun setAuthenticated(isAuthenticated: Boolean) {
        _isAuthenticated = isAuthenticated
    }

    override fun getName(): String = ""

    override fun getCredentials(): String = _credentials

    override fun getPrincipal(): DtoSession? = _principal

    override fun isAuthenticated(): Boolean = _isAuthenticated

    override fun getDetails(): Any? = null

    fun setPrincipal(session: DtoSession?) {
        this._principal = session
    }
}