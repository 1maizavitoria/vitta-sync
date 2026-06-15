import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import { logout } from './authService';

//utilitário do Vitest para mocks.
vi.mock('./api', () => ({
    default: {
        post: vi.fn()
    }
}));

//agrupa testes.
describe('authService', () => {
    //executa antes de cada teste
    beforeEach(() => {
        //utilitário do Vitest para mocks.
        vi.clearAllMocks();
    });

    //define um teste.
    it('deve chamar a rota de logout', async () => {
        const response = { data: { message: 'logout realizado' } };

        api.post.mockResolvedValue(response);

        const result = await logout();

        expect(api.post).toHaveBeenCalledWith('/sessao/logout');
        expect(result).toBe(response);
    });
});