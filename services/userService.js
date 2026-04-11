import api from "./api";

export const createUser = async (data) => {
    const response = await api.post("/usuario/cadastrar", data);
    return response.data;
};

export const getUserByCpf = async (data) => {
    const response = await api.get(`/usuario/getUsuario/${data.CPF}`);
    return response.data;
};

export const editUser = async (cpf, data) => {
    const response = await api.put(`/usuario/editar/${cpf}`, data);
    return response.data;
};

export const deleteUser = async (cpf) => {
    const response = await api.delete(`/usuario/deletar/${cpf}`);
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
