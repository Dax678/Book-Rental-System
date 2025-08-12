import AdminMainMenu from "../../components/admin/AdminMainMenu.tsx";
import {Link} from "react-router-dom";
import {useEffect, useMemo, useState} from "react";
import {useAuth} from "../../../auth/AuthContext.tsx";

// ===== Types =====
type Book = {
    id: number
    title: string
    author: string
    available: boolean
    rentalPrice: number
    purchasePrice: number
    rentalPromoPrice?: number
    purchasePromoPrice?: number
}

// ===== Helpers =====
const BASE = import.meta.env.VITE_API_URL // e.g. http://localhost:8080/api
const ENDPOINT = `${BASE}/books`    // adjust if your API path differs

const pageSizes = [10, 20, 50]

const sortOptions = [
    { value: 'title,asc', label: 'Title ↑' },
    { value: 'title,desc', label: 'Title ↓' },
    { value: 'author,asc', label: 'Author ↑' },
    { value: 'author,desc', label: 'Author ↓' },
    { value: 'rentalPrice,asc', label: 'Rental price ↑' },
    { value: 'rentalPrice,desc', label: 'Rental price ↓' },
    { value: 'purchasePrice,asc', label: 'Purchase price ↑' },
    { value: 'purchasePrice,desc', label: 'Purchase price ↓' },
]

function formatMoney(v?: number) {
    if (v == null) return '-'
    return new Intl.NumberFormat(undefined, { style: 'currency', currency: 'PLN' }).format(v)
}

function PriceWithPromo({ base, promo }: { base: number; promo: number | null | undefined }) {
    if (promo == null) return <span>{formatMoney(base)}</span>
    return (
        <span>
      <span className="text-muted text-decoration-line-through me-1">{formatMoney(base)}</span>
      <strong>{formatMoney(promo)}</strong>
    </span>
    )
}

// ===== Component =====
export default function BookInventoryAdminPage() {
    const { getToken } = useAuth()
    const [all, setAll] = useState<Book[]>([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    // UI state
    const [q, setQ] = useState('')
    const [onlyAvailable, setOnlyAvailable] = useState(false)
    const [sort, setSort] = useState('title,asc')
    const [page, setPage] = useState(0)
    const [size, setSize] = useState(20)

    // Load once
    useEffect(() => {
        let abort = new AbortController()
        ;(async () => {
            setLoading(true)
            setError(null)
            try {
                const token = getToken()
                const res = await fetch(ENDPOINT, {
                    signal: abort.signal,
                    headers: {
                        'Accept': 'application/json',
                        ...(token ? { Authorization: `Bearer ${token}` } : {}),
                    },
                })
                if (!res.ok) throw new Error(`Failed to load books: ${res.status}`)
                const raw: unknown = await res.json()
                if (!Array.isArray(raw)) throw new Error('API returned non-array result')
                setAll(raw as Book[])
            } catch (e: any) {
                if (e.name !== 'AbortError') setError(e.message || 'Unknown error')
            } finally {
                setLoading(false)
            }
        })()
        return () => abort.abort()
    }, [getToken])

    // Derived: filtered & sorted
    const filtered = useMemo(() => {
        const text = q.trim().toLowerCase()
        let arr = all
        if (text) {
            arr = arr.filter(
                b => b.title.toLowerCase().includes(text) || b.author.toLowerCase().includes(text)
            )
        }
        if (onlyAvailable) {
            arr = arr.filter(b => b.available)
        }
        // sort
        const [field, dir] = sort.split(',')
        const mul = dir === 'desc' ? -1 : 1
        arr = [...arr].sort((a: any, b: any) => {
            const va = a[field]; const vb = b[field]
            if (va == null && vb == null) return 0
            if (va == null) return 1
            if (vb == null) return -1
            if (typeof va === 'number' && typeof vb === 'number') return (va - vb) * mul
            return String(va).localeCompare(String(vb)) * mul
        })
        return arr
    }, [all, q, onlyAvailable, sort])

    // Pagination slice
    const totalElements = filtered.length
    const totalPages = Math.max(1, Math.ceil(totalElements / size))
    const pageSafe = Math.min(page, totalPages - 1)
    const start = pageSafe * size
    const end = start + size
    const pageItems = filtered.slice(start, end)

    const resetToFirstPage = () => setPage(0)

    const onDelete = async (id: number) => {
        if (!confirm('Delete this book? This action cannot be undone.')) return
        try {
            const token = getToken()
            const res = await fetch(`${ENDPOINT}/${id}`, {
                method: 'DELETE',
                headers: {
                    ...(token ? { Authorization: `Bearer ${token}` } : {}),
                },
            })
            if (!res.ok) throw new Error(`Delete failed: ${res.status}`)
            setAll(curr => curr.filter(b => b.id !== id))
        } catch (e: any) {
            alert(e.message ?? 'Delete failed')
        }
    }

    return (
        <div className="container py-4">
            <div className="mb-3">
                <AdminMainMenu />
            </div>

            <div className="d-flex align-items-center justify-content-between mb-3">
                <div>
                    <h3 className="mb-0">Book Inventory</h3>
                    <small className="text-muted">Manage the catalogue, stock levels and pricing.</small>
                </div>
                <div className="d-flex gap-2">
                    <Link to="/admin/book-inventory/new" className="btn btn-primary">+ Add Book</Link>
                    <button
                        className="btn btn-outline-secondary"
                        onClick={() => {
                            setQ(''); setOnlyAvailable(false); setSort('title,asc'); setSize(20); setPage(0)
                        }}
                    >
                        Reset
                    </button>
                </div>
            </div>

            {/* Filters */}
            <div className="card mb-3 shadow-sm">
                <div className="card-body">
                    <div className="row g-2 align-items-end">
                        <div className="col-md-5">
                            <label className="form-label">Search</label>
                            <input
                                className="form-control"
                                placeholder="Title or Author…"
                                value={q}
                                onChange={e => { setQ(e.target.value); resetToFirstPage() }}
                            />
                        </div>
                        <div className="col-md-3">
                            <label className="form-label">Sort</label>
                            <select
                                className="form-select"
                                value={sort}
                                onChange={e => { setSort(e.target.value); resetToFirstPage() }}
                            >
                                {sortOptions.map(o => (
                                    <option key={o.value} value={o.value}>{o.label}</option>
                                ))}
                            </select>
                        </div>
                        <div className="col-md-2">
                            <label className="form-label d-block">Availability</label>
                            <div className="form-check">
                                <input
                                    id="avail"
                                    className="form-check-input"
                                    type="checkbox"
                                    checked={onlyAvailable}
                                    onChange={e => { setOnlyAvailable(e.target.checked); resetToFirstPage() }}
                                />
                                <label className="form-check-label" htmlFor="avail">Only available</label>
                            </div>
                        </div>
                        <div className="col-md-2">
                            <label className="form-label">Page size</label>
                            <select
                                className="form-select"
                                value={size}
                                onChange={e => { setSize(Number(e.target.value)); resetToFirstPage() }}
                            >
                                {pageSizes.map(ps => <option key={ps} value={ps}>{ps}</option>)}
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            {/* Table */}
            <div className="card shadow-sm">
                <div className="table-responsive">
                    <table className="table table-hover align-middle mb-0">
                        <thead className="table-light">
                        <tr>
                            <th style={{width: '30%'}}>Title</th>
                            <th style={{width: '24%'}}>Author</th>
                            <th className="text-center">Available</th>
                            <th className="text-end">Rental</th>
                            <th className="text-end">Purchase</th>
                            <th style={{width: '18%'}} className="text-end">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {loading && (
                            <tr><td colSpan={6} className="text-center py-4">Loading…</td></tr>
                        )}
                        {error && !loading && (
                            <tr><td colSpan={6} className="text-center text-danger py-4">{error}</td></tr>
                        )}
                        {!loading && !error && pageItems.length === 0 && (
                            <tr><td colSpan={6} className="text-center text-muted py-4">No books found.</td></tr>
                        )}
                        {!loading && !error && pageItems.map(b => (
                            <tr key={b.id}>
                                <td className="fw-semibold">{b.title}</td>
                                <td>{b.author}</td>
                                <td className="text-center">
                                    {b.available ? <span className="badge bg-success">Yes</span> : <span className="badge bg-secondary">No</span>}
                                </td>
                                <td className="text-end">
                                    <PriceWithPromo base={b.rentalPrice} promo={b.rentalPromoPrice} />
                                </td>
                                <td className="text-end">
                                    <PriceWithPromo base={b.purchasePrice} promo={b.purchasePromoPrice} />
                                </td>
                                <td className="text-end">
                                    <div className="btn-group btn-group-sm">
                                        <Link to={`/admin/book-inventory/${b.id}`} className="btn btn-outline-secondary">View</Link>
                                        <Link to={`/admin/book-inventory/${b.id}/edit`} className="btn btn-outline-primary">Edit</Link>
                                        <button className="btn btn-outline-danger" onClick={() => onDelete(b.id)}>Delete</button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>

                {/* Pagination */}
                <div className="card-footer d-flex align-items-center justify-content-between">
                    <small className="text-muted">
                        Showing {pageItems.length} of {totalElements} (page {pageSafe + 1}/{totalPages})
                    </small>
                    <div className="btn-group">
                        <button
                            className="btn btn-outline-secondary btn-sm"
                            disabled={pageSafe === 0}
                            onClick={() => setPage(p => Math.max(0, p - 1))}
                        >
                            ‹ Prev
                        </button>
                        <button
                            className="btn btn-outline-secondary btn-sm"
                            disabled={pageSafe >= totalPages - 1}
                            onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                        >
                            Next ›
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}