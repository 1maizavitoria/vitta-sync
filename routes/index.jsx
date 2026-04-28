import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AlertProvider } from "../context/AlertProvider";
import Login from "../pages/Login";
import Register from "../pages/Register";
import MainLayout from "../components/laytout/MainLayout";
import Landing from "../pages/Landing";
import PrivateRoute from "../utils/auth/PrivateRoute";
import Dashboard from "../pages/Dashboard";
import AuthLayout from "../components/laytout/AuthLayout";
import HealthTracker from "../pages/HealthTracker";
import Reports from "../pages/Reports";

export default function AppRoutes() {
    return (
        <AlertProvider>
            <BrowserRouter>
                <Routes>
                    {/* PÚBLICO (sem sidebar) */}
                    <Route element={<AuthLayout />}>
                        <Route path="/" element={<Landing />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                    </Route>

                    {/* PRIVADO (com sidebar futuramente) */}
                    <Route element={<MainLayout />}>
                        <Route
                            path="/dashboard"
                            element={
                                <PrivateRoute>
                                    <Dashboard />
                                </PrivateRoute>
                            }
                        />
                        <Route
                            path="/health-tracker"
                            element={
                                <PrivateRoute>
                                    <HealthTracker />
                                </PrivateRoute>
                            }
                        />
                        <Route
                            path="/reports"
                            element={
                                <PrivateRoute>
                                    <Reports />
                                </PrivateRoute>
                            }
                        />
                    </Route>

                </Routes>
            </BrowserRouter>
        </AlertProvider>
    );
}