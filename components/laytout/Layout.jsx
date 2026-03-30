import { Outlet } from "react-router-dom";
import AlertUI from "../ui/Alert";
import { useAlert } from "../../hooks/useAlert";
// import Navbar from "./Navbar"; // opcional

export default function Layout() {
    const { alert } = useAlert();

    return (
        <>
            {/* <Navbar /> */}

            {alert && (
                <AlertUI
                    type={alert.type}
                    message={alert.message}
                />
            )}

            <Outlet />
        </>
    );
}