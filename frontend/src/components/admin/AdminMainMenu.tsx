import { Link, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';

export default function AdminMainMenu() {
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
                <Link to="/admin" className="navbar-brand">📚 Book Rental System</Link>
                <div className="d-flex ms-auto">
                    {isLoggedIn ? (
                        <>
                            <Link
                                to="/admin/book-inventory"
                                className="btn btn-link text-light me-3"
                            >
                                Book Inventory
                            </Link>
                            <Link
                                to="/admin/book-promotions"
                                className="btn btn-link text-light me-3"
                            >
                                Promotions
                            </Link>
                            <Link
                                to="/admin/transactions"
                                className="btn btn-link text-light me-3"
                            >
                                Transactions
                            </Link>
                            <Link
                                to="/admin/users"
                                className="btn btn-link text-light me-3"
                            >
                                Users
                            </Link>
                            <button
                                onClick={handleLogout}
                                className="btn btn-outline-light"
                            >
                                Logout
                            </button>
                        </>
                    ) : (
                        <Link to="/login" className="btn btn-primary">
                            Login
                        </Link>
                    )}
                </div>
            </div>
        </nav>
    );
}
