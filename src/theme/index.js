import { createTheme } from "@mui/material/styles";

const palette = {
    brand: {
        50: "#f0fdf4",
        100: "#dcfce7",
        200: "#bbf7d0",
        300: "#86efac",
        400: "#4ade80",
        500: "#22c55e",
        600: "#16a34a",
        700: "#15803d",
        800: "#166534",
        900: "#14532d"
    },
    teal: "#0f766e",
    blue: "#0ea5e9",
    blueDark: "#0369a1",
    success: "#16a34a",
    warning: "#d97706",
    danger: "#dc2626"
};

export function createVittaTheme(mode = "light") {
    const isDark = mode === "dark";

    return createTheme({
        palette: {
            mode,
            primary: {
                main: palette.brand[600],
                light: palette.brand[400],
                dark: palette.brand[800],
                contrastText: "#ffffff"
            },
            secondary: {
                main: palette.blue,
                contrastText: "#ffffff"
            },
            success: {
                main: palette.success
            },
            warning: {
                main: palette.warning
            },
            error: {
                main: palette.danger
            },
            background: {
                default: isDark ? "#071a12" : "#f7fbf6",
                paper: isDark ? "#102617" : "#ffffff"
            },
            text: {
                primary: isDark ? "#ecfdf3" : "#102014",
                secondary: isDark ? "#b8d8c1" : "#5a6b5d"
            },
            divider: isDark ? "rgba(220, 252, 231, 0.14)" : "rgba(22, 163, 74, 0.14)"
        },
        typography: {
            fontFamily: "'Inter', 'Manrope', 'Segoe UI', sans-serif",
            h1: {
                fontWeight: 800,
                letterSpacing: 0
            },
            h2: {
                fontWeight: 800,
                letterSpacing: 0
            },
            h3: {
                fontWeight: 750,
                letterSpacing: 0
            },
            button: {
                fontWeight: 700,
                textTransform: "none"
            }
        },
        shape: {
            borderRadius: 8
        },
        components: {
            MuiCssBaseline: {
                styleOverrides: {
                    body: {
                        backgroundColor: isDark ? "#071a1c" : "#f6faf9"
                    }
                }
            },
            MuiPaper: {
                styleOverrides: {
                    root: {
                        backgroundImage: "none"
                    }
                }
            },
            MuiButton: {
                defaultProps: {
                    disableElevation: true
                },
                styleOverrides: {
                    root: {
                        borderRadius: 8,
                        minHeight: 40
                    }
                }
            },
            MuiTextField: {
                defaultProps: {
                    size: "small"
                }
            }
        },
        vitta: {
            palette,
            surface: isDark ? "#102617" : "#ffffff",
            surfaceSoft: isDark ? "#0b1f12" : "#f0fdf4",
            surfaceMuted: isDark ? "#0f2418" : "#f7fbf6",
            textStrong: isDark ? "#ecfdf3" : "#102014",
            border: isDark ? "rgba(220, 252, 231, 0.14)" : "rgba(22, 163, 74, 0.14)",
            borderStrong: isDark ? "rgba(220, 252, 231, 0.22)" : "rgba(22, 163, 74, 0.22)",
            shadow: isDark ? "0 18px 40px rgba(0, 0, 0, 0.28)" : "0 18px 40px rgba(20, 83, 45, 0.08)",
            pageBackground: isDark
                ? "radial-gradient(circle at top left, rgba(22, 163, 74, 0.16), transparent 28rem), #071a12"
                : "radial-gradient(circle at top left, rgba(22, 163, 74, 0.1), transparent 28rem), #f7fbf6",
            panelBackground: isDark
                ? "linear-gradient(135deg, rgba(16, 38, 23, 0.96) 0%, rgba(8, 31, 35, 0.96) 100%)"
                : "linear-gradient(135deg, rgba(240, 253, 244, 0.96) 0%, rgba(239, 246, 255, 0.96) 100%)",
            heroOverlay: isDark
                ? "linear-gradient(180deg, rgba(7, 26, 18, 0.18), rgba(7, 26, 18, 0.9))"
                : "linear-gradient(180deg, rgba(247, 251, 246, 0.2), rgba(247, 251, 246, 0.92))"
        }
    });
}
