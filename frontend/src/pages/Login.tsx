import React, { useState } from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import {login} from "../../api/auth.ts";
import {useAuth} from "../../auth/AuthContext.tsx";

export default function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [rememberMe, setRememberMe] = useState(true);

    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const { login: setAuth } = useAuth()
    const navigate = useNavigate()
    const location = useLocation() as any
    const from = location.state?.from?.pathname || '/admin'

    const isValidEmail = (email: string) => {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (!isValidEmail(email)) {
            setError('Nieprawidłowy adres e-mail.');
            return;
        }

        setLoading(true);
        try {
            const res = await login({ email, password });
            setAuth(res.token)

            if (rememberMe) {
                localStorage.setItem('token', res.token);
            } else {
                sessionStorage.setItem('token', res.token);
            }

            navigate(from, { replace: true })
        } catch (err: any) {
            setError('Nieprawidłowy email lub hasło.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-5 d-flex justify-content-center">
            <div className="card shadow p-4" style={{ maxWidth: '420px', width: '100%' }}>
                <h3 className="mb-4 text-center">Logowanie</h3>

                {error && <div className="alert alert-danger">{error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="email" className="form-label">Adres e-mail</label>
                        <input
                            type="email"
                            className={`form-control ${email && !isValidEmail(email) ? 'is-invalid' : ''}`}
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                        {!isValidEmail(email) && email && (
                            <div className="invalid-feedback">Wprowadź poprawny adres e-mail.</div>
                        )}
                    </div>

                    <div className="mb-3">
                        <label htmlFor="password" className="form-label">Hasło</label>
                        <div className="input-group">
                            <input
                                type={showPassword ? 'text' : 'password'}
                                className="form-control"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                            <button
                                type="button"
                                className="btn btn-outline-secondary"
                                onClick={() => setShowPassword(!showPassword)}
                            >
                                {showPassword ? 'Ukryj' : 'Pokaż'}
                            </button>
                        </div>
                    </div>

                    <div className="form-check mb-3">
                        <input
                            className="form-check-input"
                            type="checkbox"
                            id="rememberMe"
                            checked={rememberMe}
                            onChange={(e) => setRememberMe(e.target.checked)}
                        />
                        <label className="form-check-label" htmlFor="rememberMe">
                            Zapamiętaj mnie
                        </label>
                    </div>

                    <button
                        type="submit"
                        className="btn btn-primary w-100"
                        disabled={loading}
                    >
                        {loading ? (
                            <>
                                <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                                Logowanie...
                            </>
                        ) : (
                            'Zaloguj się'
                        )}
                    </button>
                </form>

                <div className="mt-3 text-center">
                    Nie masz konta? <a href="/register">Zarejestruj się</a>
                </div>
            </div>
        </div>
    );
}
