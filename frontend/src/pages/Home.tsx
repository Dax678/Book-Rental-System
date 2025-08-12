import MainMenu from '../components/MainMenu.tsx';
import Navbar from "../components/Navbar.tsx";
import BestsellersSection from "../components/BestsellersSection.tsx";
import AdBannerSection from "../components/AdBannerSection.tsx";
import Footer from "../components/Footer.tsx";

export default function Home() {
    return (
        <div>
            <MainMenu />
            <Navbar />
            <AdBannerSection />
            <BestsellersSection />
            <Footer />
        </div>
    );
}
