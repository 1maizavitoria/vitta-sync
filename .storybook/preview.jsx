import { CssBaseline } from "@mui/material";

import { LanguageProvider } from "../src/i18n";
import { VittaThemeProvider } from "../src/theme/ThemeModeProvider";
import "../src/index.css";

export const globalTypes = {
    theme: {
        description: "Tema visual do VittaSync",
        defaultValue: "light",
        toolbar: {
            icon: "paintbrush",
            items: [
                { value: "light", title: "Claro" },
                { value: "dark", title: "Escuro" }
            ],
            dynamicTitle: true
        }
    },
    accessibilityMode: {
        description: "Modo acessível do VittaSync",
        defaultValue: "standard",
        toolbar: {
            icon: "accessibility",
            items: [
                { value: "standard", title: "Padrão" },
                { value: "accessible", title: "Acessível" }
            ],
            dynamicTitle: true
        }
    }
};

export const decorators = [
    (Story, context) => {
        const themeMode = context.globals.theme === "dark" ? "dark" : "light";
        const accessibilityMode = context.globals.accessibilityMode === "accessible";
        const providerKey = `${themeMode}-${accessibilityMode}`;

        localStorage.setItem("vitta-theme-mode", themeMode);
        localStorage.setItem("vitta-accessibility-mode", String(accessibilityMode));

        return (
            <VittaThemeProvider key={providerKey}>
                <LanguageProvider>
                    <CssBaseline />
                    <Story />
                </LanguageProvider>
            </VittaThemeProvider>
        );
    }
];

export const parameters = {
    controls: {
        matchers: {
            color: /(background|color)$/i,
            date: /Date$/i
        }
    },
    a11y: {
        test: "todo"
    }
};
