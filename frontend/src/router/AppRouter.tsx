import { Routes, Route } from 'react-router-dom';
import Home from '../pages/Home';
import Login from '../pages/Login';
import Register from "../pages/Register.tsx";
import Books from "../pages/Books.tsx";
import PrivateRoute from "../../auth/PrivateRoute.tsx";
import BookPromotionsAdminPage from "../pages/admin/BookPromotionsAdminPage.tsx";
import AdminHomePage from "../pages/admin/AdminHomePage.tsx";
import BookInventoryAdminPage from "../pages/admin/BookInventoryAdminPage.tsx";
import TransactionsAdminPage from "../pages/admin/TransactionsAdminPage.tsx";
import UsersAdminPage from "../pages/admin/UsersAdminPage.tsx";

export default function AppRouter() {
    return (
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/books" element={<Books />} />
                <Route path="/admin" element={
                    <PrivateRoute role="ADMIN">
                        <AdminHomePage />
                    </PrivateRoute>
                } />
                <Route path="/admin/book-inventory" element={
                    <PrivateRoute role="ADMIN">
                        <BookInventoryAdminPage />
                    </PrivateRoute>
                } />
                <Route path="/admin/book-promotions" element={
                    <PrivateRoute role="ADMIN">
                        <BookPromotionsAdminPage />
                    </PrivateRoute>
                } />
                <Route path="/admin/transactions" element={
                    <PrivateRoute role="ADMIN">
                        <TransactionsAdminPage />
                    </PrivateRoute>
                } />
                <Route path="/admin/users" element={
                    <PrivateRoute role="ADMIN">
                        <UsersAdminPage />
                    </PrivateRoute>
                } />
                <Route path="*" element={<div>404</div>} />
            </Routes>
    );
}
