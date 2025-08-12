import {useEffect, useState} from 'react';
import './styles/BooksSearch.css';

interface Book {
    id: number;
    coverUrl: string;
    title: string;
    author: string;
    price: string;
}

export default function BooksSearch() {
    const [books, setBooks] = useState<Book[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string|null>(null);

    const [page, setPage] = useState<number>(1);
    const [totalPages, setTotalPages] = useState<number>(1);

    const pageSize = 8;

    useEffect(() => {
        const fetchBooks = async () => {
            setLoading(true);
            setError(null);
            try {
                const res = await fetch(`/api/books?page=${page}&limit=${pageSize}`);
                if (!res.ok) throw new Error(`HTTP ${res.status}`);
                const json = await res.json() as {
                    items: Book[];
                    totalPages: number;
                    page: number;
                };
                setBooks(json.items);
                setTotalPages(json.totalPages);
            } catch (err: any) {
                setError('Nie udało się pobrać książek.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchBooks();
    }, [page]);

    const handlePageClick = (newPage: number) => {
        if (newPage >= 1 && newPage <= totalPages && newPage !== page) {
            setPage(newPage);
        }
    };

    return (
        <main className="books-page container py-5">
            <h1 className="mb-4 text-center">Książki</h1>

            {loading && <p className="text-center">Ładowanie...</p>}
            {error && <p className="text-center text-danger">{error}</p>}

            <div className="row">
                {!loading && books.map(book => (
                    <div key={book.id} className="col-12 col-sm-6 col-md-4 col-lg-3 mb-4">
                        <div className="card h-100 book-card shadow-sm">
                            <img
                                src={book.coverUrl}
                                className="card-img-top"
                                alt={`Okładka: ${book.title}`}
                            />
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title">{book.title}</h5>
                                <p className="card-text text-muted mb-2">{book.author}</p>
                                <p className="card-text fw-bold mb-4">{book.price}</p>
                                <button className="btn btn-primary mt-auto">
                                    Dodaj do koszyka
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {/* Pagination */}
            {totalPages > 1 && (
                <nav>
                    <ul className="pagination justify-content-center">
                        <li className={`page-item${page === 1 ? ' disabled' : ''}`}>
                            <button
                                className="page-link"
                                onClick={() => handlePageClick(page - 1)}
                                aria-label="Poprzednia"
                            >
                                &laquo;
                            </button>
                        </li>
                        {Array.from({ length: totalPages }, (_, i) => i + 1).map(p => (
                            <li key={p} className={`page-item${p === page ? ' active' : ''}`}>
                                <button className="page-link" onClick={() => handlePageClick(p)}>
                                    {p}
                                </button>
                            </li>
                        ))}
                        <li className={`page-item${page === totalPages ? ' disabled' : ''}`}>
                            <button
                                className="page-link"
                                onClick={() => handlePageClick(page + 1)}
                                aria-label="Następna"
                            >
                                &raquo;
                            </button>
                        </li>
                    </ul>
                </nav>
            )}
        </main>
    );
}
