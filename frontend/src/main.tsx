import React from "react";
import AppRouter from "./router/AppRouter.tsx";
import {createRoot} from 'react-dom/client'
import {BrowserRouter} from "react-router-dom";
import {AuthProvider} from "../auth/AuthContext.tsx";
import 'bootstrap/dist/css/bootstrap.min.css';

createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <AuthProvider>
            <BrowserRouter>
                <AppRouter/>
            </BrowserRouter>
        </AuthProvider>
    </React.StrictMode>
)
