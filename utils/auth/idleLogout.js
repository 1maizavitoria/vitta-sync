import { logout } from "../../services/authService";

let timeout;

const TEMPO_INATIVIDADE = 60 * 60 * 1000; // 5 min
//const TEMPO_INATIVIDADE = 1 * 10 * 1000; // 10 sec

function handleLogout() {
    console.log("Inatividade detectada → logout");

    logout().catch(() => {
        // mesmo se falhar, continua logout local
    });

    localStorage.removeItem("token");
    window.location.href = "/";
}

function resetTimer() {
    const token = localStorage.getItem("token");

    if (!token) return;

    clearTimeout(timeout);
    timeout = setTimeout(handleLogout, TEMPO_INATIVIDADE);
}

export function initIdleLogout() {
    console.log("Idle logout iniciado");

    const eventos = ["mousemove", "keydown", "click", "scroll"];

    eventos.forEach((event) => {
        window.addEventListener(event, resetTimer);
    });

    resetTimer();
}