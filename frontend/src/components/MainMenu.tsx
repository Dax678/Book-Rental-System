import { Link, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';

export default function MainMenu() {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');
        setIsLoggedIn(!!token);
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');
        setIsLoggedIn(false);
        navigate('/login');
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark px-4">
            <div className="container-fluid">
                <Link to="/" className="navbar-brand">📚 Book Rental System</Link>
                <div className="d-flex ms-auto">
                    {isLoggedIn ? (
                        <>
                            <Link
                                to="/my-list"
                                className="btn btn-link text-light me-3"
                            >
                                Moja lista
                            </Link>
                            <Link
                                to="/profile"
                                className="btn btn-link text-light me-3"
                            >
                                Mój profil
                            </Link>
                            <button
                                onClick={handleLogout}
                                className="btn btn-outline-light"
                            >
                                Wyloguj
                            </button>
                        </>
                    ) : (
                        <Link to="/login" className="btn btn-primary">
                            Logowanie
                        </Link>
                    )}
                </div>
            </div>
        </nav>
    );
}
