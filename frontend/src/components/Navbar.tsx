import { Link } from 'react-router-dom';
import './styles/Navbar.css';

export default function Navbar() {
    return (
        <nav className="navbar navbar-expand-lg custom-navbar px-4 py-3">
            <div className="container-fluid">
                <ul className="navbar-nav mx-auto">
                    <li className="nav-item">
                        <Link to="/books" className="nav-link">
                            Książki
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link to="/news" className="nav-link">
                            Nowości
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link to="/top10" className="nav-link">
                            TOP 10
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link to="/premieres" className="nav-link">
                            Kalendarz premier
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link to="/school-supplies" className="nav-link">
                            Artykuły szkolne
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link to="/promotions" className="nav-link">
                            Promocje
                        </Link>
                    </li>
                </ul>
            </div>
        </nav>
    );
}
