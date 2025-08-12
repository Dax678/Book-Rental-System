import {type ReactNode } from 'react'
import {Navigate, useLocation} from 'react-router-dom'
import {useAuth} from './AuthContext'

type Role = 'ADMIN' | 'USER'

interface Props {
    children: ReactNode;
    role?: Role
}

const hasRole = (roles: string[] = [], required?: Role) => {
    if (!required) return true
    return roles.some(r => r.toUpperCase().replace(/^ROLE_/, '') === required)
}

export default function PrivateRoute({children, role}: Props) {
    const {user, initialized} = useAuth()
    const location = useLocation()

    if (!initialized) return null // lub spinner
    if (!user) return <Navigate to="/login" state={{from: location}} replace/>
    if (!hasRole(user.roles, role)) return <Navigate to="/forbidden" replace/>

    return <>{children}</>
}
