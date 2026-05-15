import api from "./api";

export async function logout() {
    return await api.post("/sessao/logout");
}