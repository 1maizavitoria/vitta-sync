import { useEffect } from "react";
import AppRoutes from "../routes";
import { initIdleLogout } from "../utils/auth/idleLogout";


function App() {
  useEffect(() => {
    initIdleLogout();
  }, []);
  return (
    <>
      <AppRoutes />
    </>
  );
}

export default App;