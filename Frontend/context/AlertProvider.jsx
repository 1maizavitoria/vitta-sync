import { useRef, useState } from "react";
import { AlertContext } from "../context/AlertContext";

export function AlertProvider({ children }) {
    const [alert, setAlert] = useState(null);
    const timerRef = useRef(null);

    const showAlert = (type, message) => {
        setAlert({ type, message });

        if (timerRef.current) {
            clearTimeout(timerRef.current);
        }

        timerRef.current = setTimeout(() => {
            setAlert(null);
        }, 5000);
    };

    return (
        <AlertContext.Provider value={{ alert, showAlert }}>
            {children}
        </AlertContext.Provider>
    );
}