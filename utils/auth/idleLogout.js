import { logout } from "../../services/authService";

let timeout;

const TEMPO_INATIVIDADE = 5 * 60 * 1000; // 5 minutos
//const TEMPO_INATIVIDADE = 1 * 10 * 1000; // 10 sec

function handleLogout() {
    console.log("Inatividade detectada → logout");

    logout().catch(() => {
        // mesmo se falhar, continua logout local
    });

    localStorage.removeItem("token");
    window.location.href = "/login";
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