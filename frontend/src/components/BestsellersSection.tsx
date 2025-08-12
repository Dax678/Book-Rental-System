import './styles/BestsellersSection.css';
import Book from './../assets/czystykod-cover-1.jpg'

interface Book {
    id: number;
    coverUrl: string;
    title: string;
    author: string;
    price: string;
}

const books: Book[] = [
    { id: 1, coverUrl: Book, title: 'Czysty Kod', author: 'Robert C. Martin', price: '79,99 zł' },
    { id: 2, coverUrl: Book, title: 'Czysty Kod', author: 'Robert C. Martin', price: '79,99 zł' },
    { id: 3, coverUrl: Book, title: 'Czysty Kod', author: 'Robert C. Martin', price: '79,99 zł' },
    { id: 4, coverUrl: Book, title: 'Czysty Kod', author: 'Robert C. Martin', price: '79,99 zł' },
    { id: 5, coverUrl: Book, title: 'Czysty Kod', author: 'Robert C. Martin', price: '79,99 zł' },
    { id: 6, coverUrl: Book, title: 'Czysty Kod', author: 'Robert C. Martin', price: '79,99 zł' },
    { id: 7, coverUrl: Book, title: 'Czysty Kod', author: 'Robert C. Martin', price: '79,99 zł' },
    { id: 8, coverUrl: Book, title: 'Czysty Kod', author: 'Robert C. Martin', price: '79,99 zł' },
];

export default function BestsellersSection() {
    return (
        <section className="bestsellers-section py-5">
            <div className="container">
                <h2 className="mb-4 text-center">Bestsellery</h2>
                <div className="row">
                    {books.map(book => (
                        <div key={book.id} className="col-12 col-sm-6 col-md-3 mb-4">
                            <div className="card h-100 shadow-sm">
                                <img
                                    src={book.coverUrl}
                                    className="card-img-top"
                                    alt={`Okładka ${book.title}`}
                                />
                                <div className="card-body d-flex flex-column">
                                    <h5 className="card-title">{book.title}</h5>
                                    <p className="card-text text-muted mb-2">{book.author}</p>
                                    <p className="card-text fw-bold mb-4">{book.price}</p>
                                    <button className="btn btn-success mt-auto">
                                        Dodaj do koszyka
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
}
