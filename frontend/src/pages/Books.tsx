import MainMenu from '../components/MainMenu.tsx';
import Navbar from "../components/Navbar.tsx";
import Footer from "../components/Footer.tsx";
import BooksSearch from "../components/BooksSearch.tsx";

export default function Books() {
    return (
        <div>
            <MainMenu />
            <Navbar />
            <BooksSearch />
            <Footer />
        </div>
    );
}
