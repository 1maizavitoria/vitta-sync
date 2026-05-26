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
import PatientHub from "../pages/PatientHub";
import JoinLink from "../pages/JoinLink";
import { PatientProvider } from "../context/PatientContext";
import Documents from "../pages/Documents";
import Activity from "../pages/Activity";

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
                        <Route path="/entrar" element={<JoinLink />}
                        />
                    </Route>
                    <Route
                        element={
                            <PatientProvider>
                                <MainLayout />
                            </PatientProvider>
                        }
                    >
                        <Route
                            path="/dashboard"
                            element={
                                <PrivateRoute>
                                    <PatientHub />
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

                        <Route
                            path="/links"
                            element={
                                <PrivateRoute>
                                    <PatientHub />
                                </PrivateRoute>
                            }
                        />
                        <Route
                            path="/documents"
                            element={
                                <PrivateRoute>
                                    <Documents />
                                </PrivateRoute>
                            }
                        />
                        <Route
                            path="/activity"
                            element={
                                <PrivateRoute>
                                    <Activity />
                                </PrivateRoute>
                            }
                        />
                    </Route>


                </Routes>
            </BrowserRouter>
        </AlertProvider>
    );
}