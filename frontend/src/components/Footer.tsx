import { Link } from 'react-router-dom';
import './styles/Footer.css';

export default function Footer() {
    return (
        <footer className="footer bg-dark text-light pt-5">
            <div className="container">
                <div className="row">

                    {/* Kolumna: O nas */}
                    <div className="col-12 col-md-4 mb-4">
                        <h5>O nas</h5>
                        <p className="small">
                            📚 Book Rental System to miejsce, w którym znajdziesz najlepsze książki na każdą okazję.
                            Wypożyczaj, czytaj i zwracaj — bez wychodzenia z domu!
                        </p>
                    </div>

                    {/* Kolumna: Szybkie linki */}
                    <div className="col-6 col-md-2 mb-4">
                        <h6>Szybkie linki</h6>
                        <ul className="list-unstyled">
                            <li><Link to="/" className="footer-link">Strona główna</Link></li>
                            <li><Link to="/books" className="footer-link">Książki</Link></li>
                            <li><Link to="/news" className="footer-link">Nowości</Link></li>
                            <li><Link to="/top10" className="footer-link">TOP 10</Link></li>
                        </ul>
                    </div>

                    {/* Kolumna: Obsługa klienta */}
                    <div className="col-6 col-md-3 mb-4">
                        <h6>Obsługa klienta</h6>
                        <ul className="list-unstyled">
                            <li><Link to="/contact" className="footer-link">Kontakt</Link></li>
                            <li><Link to="/faq" className="footer-link">FAQ</Link></li>
                            <li><Link to="/terms" className="footer-link">Regulamin</Link></li>
                            <li><Link to="/privacy" className="footer-link">Polityka prywatności</Link></li>
                        </ul>
                    </div>

                    {/* Kolumna: Social */}
                    <div className="col-12 col-md-3 mb-4">
                        <h6>Śledź nas</h6>
                        <div className="d-flex">
                            <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" className="me-3 social-icon">
                                <i className="bi bi-facebook" aria-label="Facebook" />
                            </a>
                            <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" className="me-3 social-icon">
                                <i className="bi bi-twitter" aria-label="Twitter" />
                            </a>
                            <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" className="me-3 social-icon">
                                <i className="bi bi-instagram" aria-label="Instagram" />
                            </a>
                        </div>
                    </div>

                </div>

                <hr className="border-secondary" />

                <div className="text-center pb-3 small">
                    © {new Date().getFullYear()} Book Rental System. Wszelkie prawa zastrzeżone.
                </div>
            </div>
        </footer>
    );
}
