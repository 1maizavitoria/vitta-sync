import { useEffect } from "react";
import { CssBaseline } from "@mui/material";
import AppRoutes from "../routes";
import { initIdleLogout } from "../utils/auth/idleLogout";
import { LanguageProvider } from "./i18n";
import { VittaThemeProvider } from "./theme/ThemeModeProvider";


function App() {
  useEffect(() => {
    initIdleLogout();
  }, []);

  return (
    <VittaThemeProvider>
      <LanguageProvider>
        <CssBaseline />
        <AppRoutes />
      </LanguageProvider>
    </VittaThemeProvider>
  );
}

export default App;
