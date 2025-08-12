import './styles/AdBannerSection.css';
import AdBanner from './../assets/example-banner-1.png'

interface Banner {
    id: number;
    imageUrl: string;
    linkUrl: string;
    alt: string;
}

const banners: Banner[] = [
    { id: 1, imageUrl: AdBanner, linkUrl: '/promo/1', alt: 'Promocja 1' },
    { id: 2, imageUrl: AdBanner, linkUrl: '/promo/2', alt: 'Promocja 2' },
    { id: 3, imageUrl: AdBanner, linkUrl: '/promo/3', alt: 'Promocja 3' },
];

export default function AdBannerSection() {
    return (
        <section className="ad-banner-section my-5">
            <div
                id="adCarousel"
                className="carousel slide"
                data-bs-ride="carousel"
                data-bs-interval="5000"
            >
                <div className="carousel-inner">
                    {banners.map((banner, idx) => (
                        <div
                            key={banner.id}
                            className={`carousel-item${idx === 0 ? ' active' : ''}`}
                        >
                            <a href={banner.linkUrl}>
                                <img
                                    src={banner.imageUrl}
                                    className="d-block w-100"
                                    alt={banner.alt}
                                />
                            </a>
                        </div>
                    ))}
                </div>
                <button
                    className="carousel-control-prev"
                    type="button"
                    data-bs-target="#adCarousel"
                    data-bs-slide="prev"
                >
                    <span className="carousel-control-prev-icon" aria-hidden="true" />
                    <span className="visually-hidden">Poprzedni</span>
                </button>
                <button
                    className="carousel-control-next"
                    type="button"
                    data-bs-target="#adCarousel"
                    data-bs-slide="next"
                >
                    <span className="carousel-control-next-icon" aria-hidden="true" />
                    <span className="visually-hidden">Następny</span>
                </button>
            </div>
        </section>
    );
}
