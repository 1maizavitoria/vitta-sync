/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useMemo, useState } from "react";
import { ThemeProvider } from "@mui/material/styles";

import { createVittaTheme } from ".";

const ThemeModeContext = createContext(null);

const storageKey = "vitta-theme-mode";
const defaultMode = "light";

function normalizeMode(mode) {
    return mode === "dark" ? "dark" : "light";
}

export function VittaThemeProvider({ children }) {
    const [mode, setModeState] = useState(() =>
        normalizeMode(localStorage.getItem(storageKey) || defaultMode)
    );

    const theme = useMemo(() => createVittaTheme(mode), [mode]);

    const value = useMemo(() => ({
        mode,
        setMode: (nextMode) => {
            const normalizedMode = normalizeMode(nextMode);
            localStorage.setItem(storageKey, normalizedMode);
            setModeState(normalizedMode);
        },
        toggleMode: () => {
            setModeState((currentMode) => {
                const nextMode = currentMode === "dark" ? "light" : "dark";
                localStorage.setItem(storageKey, nextMode);
                return nextMode;
            });
        }
    }), [mode]);

    return (
        <ThemeModeContext.Provider value={value}>
            <ThemeProvider theme={theme}>
                {children}
            </ThemeProvider>
        </ThemeModeContext.Provider>
    );
}

export function useThemeMode() {
    const context = useContext(ThemeModeContext);

    if (!context) {
        throw new Error("useThemeMode must be used inside VittaThemeProvider");
    }

    return context;
}
