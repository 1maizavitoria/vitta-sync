import api from "./api";

export const createUser = async (data) => {
    const response = await api.post("/usuarios", data);
    return response.data;
};

export const getUsers = async () => {
    const response = await api.get("/usuarios");
    return response.data;
};
