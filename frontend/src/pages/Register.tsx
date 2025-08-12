import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {api} from "../services/api.ts";

export default function Register() {
    const [email, setEmail] = useState('');
    const [fullName, setFullName] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const isValidEmail = (email: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (!isValidEmail(email)) {
            setError('Nieprawidłowy adres e-mail.');
            return;
        }

        if (password.length < 6) {
            setError('Hasło musi mieć co najmniej 6 znaków.');
            return;
        }

        if (password !== confirmPassword) {
            setError('Hasła się nie zgadzają.');
            return;
        }

        try {
            setLoading(true);
            await api.post('/auth/signup', {
                fullName,
                email,
                password,
            });
            navigate('/login');
        } catch (err: any) {
            setError('Rejestracja nie powiodła się. Spróbuj ponownie.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-5 d-flex justify-content-center">
            <div className="card shadow p-4" style={{ maxWidth: '420px', width: '100%' }}>
                <h3 className="mb-4 text-center">Rejestracja</h3>

                {error && <div className="alert alert-danger">{error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="fullName" className="form-label">Imię i nazwisko</label>
                        <input
                            type="text"
                            className="form-control"
                            id="fullName"
                            value={fullName}
                            onChange={(e) => setFullName(e.target.value)}
                            required
                        />
                    </div>

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
                        <input
                            type={showPassword ? 'text' : 'password'}
                            className="form-control"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            minLength={6}
                        />
                    </div>

                    <div className="mb-3">
                        <label htmlFor="confirmPassword" className="form-label">Powtórz hasło</label>
                        <input
                            type={showPassword ? 'text' : 'password'}
                            className="form-control"
                            id="confirmPassword"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </div>

                    <div className="form-check mb-3">
                        <input
                            type="checkbox"
                            className="form-check-input"
                            id="showPassword"
                            checked={showPassword}
                            onChange={(e) => setShowPassword(e.target.checked)}
                        />
                        <label className="form-check-label" htmlFor="showPassword">
                            Pokaż hasło
                        </label>
                    </div>

                    <button
                        type="submit"
                        className="btn btn-success w-100"
                        disabled={loading}
                    >
                        {loading ? (
                            <>
                                <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                                Rejestracja...
                            </>
                        ) : (
                            'Zarejestruj się'
                        )}
                    </button>
                </form>

                <div className="mt-3 text-center">
                    Masz już konto? <a href="/login">Zaloguj się</a>
                </div>
            </div>
        </div>
    );
}
