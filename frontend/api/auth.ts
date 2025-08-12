export type LoginDto = { email: string; password: string }
export type AuthResponse = { token: string; expiresIn: number; roles: string[] }

const BASE = import.meta.env.VITE_API_URL

export async function login(dto: LoginDto): Promise<AuthResponse> {
    const res = await fetch(`${BASE}/auth/login`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(dto)
    })
    if (!res.ok) {
        throw new Error(`Login failed: ${res.status}`)
    }
    return res.json()
}
