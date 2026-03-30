import api from "./api";

export const createUser = async (data) => {
    const response = await api.post("/usuario/cadastrar", data);
    return response.data;
};

export const getUsers = async () => {
    const response = await api.get("/usuarios");
    return response.data;
};

export const login = async (data) => {
    const response = await api.post("/auth/login", data);
    return response.data;
}

export const validadeCode = async (data) => {
    const response = await api.post(
        `/auth/validar-codigo-login?codigo=${data.codigo}`
    );
    return response.data;
};

export const validadeCodePassword = async (data) => {
    const response = await api.post(
        `/auth/enviar-codigo-redefinir-senha?email=${data.email}`
    );
    return response.data;
};

export const changePassword = async (data) => {
    const response = await api.post(
        `/auth/redefinir-senha?codigo=${data.codigo}&novaSenha=${data.novaSenha}`
    );
    return response.data;
};
