import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import {
    createUser,
    getUserByCpf,
    editUser,
    deleteUser,
    login,
    validadeCode,
    validadeCodePassword,
    changePassword
} from './userService';

vi.mock('./api', () => ({
    default: {
        post: vi.fn(),
        get: vi.fn(),
        put: vi.fn(),
        delete: vi.fn()
    }
}));

describe('userService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve cadastrar usuário', async () => {
        const data = { nome: 'Lucas' };
        const response = { data: { id: 1, nome: 'Lucas' } };

        api.post.mockResolvedValue(response);

        const result = await createUser(data);

        expect(api.post).toHaveBeenCalledWith('/usuario/cadastrar', data);
        expect(result).toEqual(response.data);
    });

    it('deve buscar usuário por CPF', async () => {
        const data = { CPF: '12345678901' };
        const response = { data: { nome: 'Lucas' } };

        api.get.mockResolvedValue(response);

        const result = await getUserByCpf(data);

        expect(api.get).toHaveBeenCalledWith('/usuario/getUsuario/12345678901');
        expect(result).toEqual(response.data);
    });

    it('deve editar usuário', async () => {
        const cpf = '12345678901';
        const data = { nome: 'Lucas Editado' };
        const response = { data: { sucesso: true } };

        api.put.mockResolvedValue(response);

        const result = await editUser(cpf, data);

        expect(api.put).toHaveBeenCalledWith('/usuario/editar/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve deletar usuário', async () => {
        const response = { data: { sucesso: true } };

        api.delete.mockResolvedValue(response);

        const result = await deleteUser('12345678901');

        expect(api.delete).toHaveBeenCalledWith('/usuario/deletar/12345678901');
        expect(result).toEqual(response.data);
    });

    it('deve fazer login', async () => {
        const data = { email: 'usuario@email.com', senha: 'Senha123@' };
        const response = { data: { token: 'abc123' } };

        api.post.mockResolvedValue(response);

        const result = await login(data);

        expect(api.post).toHaveBeenCalledWith('/auth/login', data);
        expect(result).toEqual(response.data);
    });

    it('deve validar código de login', async () => {
        const response = { data: { valido: true } };

        api.post.mockResolvedValue(response);

        const result = await validadeCode({ codigo: '123456' });

        expect(api.post).toHaveBeenCalledWith('/auth/validar-codigo-login?codigo=123456');
        expect(result).toEqual(response.data);
    });

    it('deve enviar código para redefinir senha', async () => {
        const data = { email: 'usuario@email.com' };
        const response = { data: { enviado: true } };

        api.post.mockResolvedValue(response);

        const result = await validadeCodePassword(data);

        expect(api.post).toHaveBeenCalledWith('/auth/enviar-codigo-redefinir-senha', data);
        expect(result).toEqual(response.data);
    });

    it('deve redefinir senha', async () => {
        const data = { codigo: '123456', novaSenha: 'NovaSenha123@' };
        const response = { data: { alterado: true } };

        api.post.mockResolvedValue(response);

        const result = await changePassword(data);

        expect(api.post).toHaveBeenCalledWith('/auth/redefinir-senha', data);
        expect(result).toEqual(response.data);
    });
});