import { Navigate } from "react-router-dom";
import { useEffect } from "react";
import { isTokenExpired } from "./auth";

export default function PrivateRoute({ children }) {
    const token = localStorage.getItem("token");

    useEffect(() => {
        const interval = setInterval(() => {
            const token = localStorage.getItem("token");

            if (token && isTokenExpired(token)) {
                localStorage.removeItem("token");
                window.location.href = "/";
            }
        }, 5000);

        return () => clearInterval(interval);
    }, []);

    if (!token || isTokenExpired(token)) {
        localStorage.removeItem("token");
        return <Navigate to="/" />;
    }

    return children;
}