import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import type { ReactNode } from 'react'

type User = { username: string; roles: string[] }
type AuthCtx = {
    user: User | null
    initialized: boolean
    login: (token: string) => void
    logout: () => void
    getToken: () => string | null
}

const TOKEN_KEY = 'token'

const AuthContext = createContext<AuthCtx | undefined>(undefined)

function decodeJwt<T = any>(token: string): T | null {
    if (!token) return null
    const part = token.split('.')[1]
    if (!part) return null
    const base64 = part.replace(/-/g, '+').replace(/_/g, '/')
    const padded = base64.padEnd(base64.length + (4 - (base64.length % 4)) % 4, '=')
    try {
        const json = decodeURIComponent(
            atob(padded)
                .split('')
                .map(c => '%' + c.charCodeAt(0).toString(16).padStart(2, '0'))
                .join('')
        )
        return JSON.parse(json) as T
    } catch {
        return null
    }
}

function extractRoles(payload: any): string[] {
    if (!payload) return []

    const arr = (v: any) => Array.isArray(v) ? v : typeof v === 'string' ? v.split(/[,\s]+/) : []

    const fromAuthorities =
        Array.isArray(payload.authorities)
            ? payload.authorities.map((a: any) => (typeof a === 'string' ? a : a?.authority))
            : arr(payload.authorities)

    const fromRoles = arr(payload.roles)
    const fromScope = arr(payload.scope)
    const fromKeycloak = payload.realm_access?.roles ?? []

    const roles = [...fromAuthorities, ...fromRoles, ...fromScope, ...fromKeycloak]
        .filter(Boolean)
        .map((r: string) => r.replace(/^ROLE_/i, '').toUpperCase())

    return Array.from(new Set(roles))
}

function isExpired(payload: any): boolean {
    if (!payload?.exp) return false
    return payload.exp < Math.floor(Date.now() / 1000)
}

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<User | null>(null)
    const [initialized, setInitialized] = useState(false)

    const getToken = () => {
        try {
            return localStorage.getItem(TOKEN_KEY)
        } catch {
            return null
        }
    }

    const updateFromToken = (token: string | null) => {
        if (!token) {
            setUser(null)
            return
        }
        const payload = decodeJwt<any>(token)
        if (!payload || isExpired(payload)) {
            try {
                localStorage.removeItem(TOKEN_KEY)
            } catch {}
            setUser(null)
            return
        }

        const username = payload.sub ?? payload.username ?? ''
        const roles = extractRoles(payload)

        // console.groupCollapsed('[Auth] Zdekodowany JWT')
        // console.log('username:', username)
        // console.log('roles (normalized):', roles)
        // console.log('raw authorities:', payload.authorities)
        // console.log('raw roles:', payload.roles)
        // console.log('scope:', payload.scope)
        // console.log('realm_access.roles:', payload.realm_access?.roles)
        // console.log('exp (unix):', payload.exp, '=>', new Date(payload.exp * 1000).toISOString())
        // console.log('token (początek):', token.slice(0, 12) + '…')
        // console.groupEnd()

        setUser({ username: username, roles })
    }

    useEffect(() => {
        updateFromToken(getToken())
        setInitialized(true)
    }, [])

    const login = (token: string) => {
        try {
            localStorage.setItem(TOKEN_KEY, token)
        } catch {}
        updateFromToken(token)
    }

    const logout = () => {
        try {
            localStorage.removeItem(TOKEN_KEY)
        } catch {}
        setUser(null)
    }

    const value: AuthCtx = useMemo(
        () => ({ user, initialized, login, logout, getToken }),
        [user, initialized]
    )

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth(): AuthCtx {
    const ctx = useContext(AuthContext)
    if (!ctx) throw new Error('useAuth must be used within <AuthProvider>')
    return ctx
}
