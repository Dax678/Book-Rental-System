import AdminMainMenu from "../../components/admin/AdminMainMenu.tsx";
import {Link} from "react-router-dom";
import {useAuth} from "../../../auth/AuthContext.tsx";

type Section = {
    path: string
    title: string
    description: string
}

const sections: Section[] = [
    {
        path: '/admin/book-inventory',
        title: 'Book Inventory',
        description: 'Manage catalogue, pricing, availability, and metadata.'
    },
    {
        path: '/admin/book-promotions',
        title: 'Promotions',
        description: 'Create, edit, and schedule rental promotions.'
    },
    {
        path: '/admin/transactions',
        title: 'Transactions',
        description: 'View rentals, returns, and operation history.'
    },
    {
        path: '/admin/users',
        title: 'Users',
        description: 'Manage accounts, roles, and permissions.'
    }
]

export default function AdminHomePage() {
    const { user } = useAuth()

    return (
        <div className="container py-4">
                <AdminMainMenu />

            <div className="card mb-4 shadow-sm">
                <div className="card-body">
                    <h4 className="card-title mb-1">
                        Welcome, {user?.username || 'Administrator'}!
                    </h4>
                    <p className="card-text text-muted mb-0">
                        This is the administration panel of the Book Rental System. Choose a section below to get started.
                    </p>
                </div>
            </div>

            <div className="card shadow-sm">
                <div className="card-header">
                    <strong>Panel Sections</strong>
                </div>
                <div className="table-responsive">
                    <table className="table table-hover align-middle mb-0">
                        <thead className="table-light">
                        <tr>
                            <th style={{ width: '28%' }}>Section</th>
                            <th>Description</th>
                            <th style={{ width: '12%' }} className="text-end">Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        {sections.map(s => (
                            <tr key={s.path}>
                                <td>
                                    <Link to={s.path} className="text-decoration-none">
                                        {s.title}
                                    </Link>
                                </td>
                                <td className="text-muted">{s.description}</td>
                                <td className="text-end">
                                    <Link to={s.path} className="btn btn-sm btn-primary">
                                        Open
                                    </Link>
                                </td>
                            </tr>
                        ))}
                        {sections.length === 0 && (
                            <tr>
                                <td colSpan={3} className="text-center text-muted py-4">
                                    No sections to display.
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    )
}