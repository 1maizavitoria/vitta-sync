import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import {
    registerHabits,
    editHabits,
    deleteHabits,
    getHabits
} from './habitService';

vi.mock('./api', () => ({
    default: {
        post: vi.fn(),
        put: vi.fn(),
        delete: vi.fn(),
        get: vi.fn()
    }
}));

describe('habitService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve cadastrar hábitos', async () => {
        const data = { agua: true };
        const response = { data: { id: 1 } };

        api.post.mockResolvedValue(response);

        const result = await registerHabits('12345678901', data);

        expect(api.post).toHaveBeenCalledWith('/habitos/cadastrar/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve editar hábitos', async () => {
        const data = { agua: false };
        const response = { data: { atualizado: true } };

        api.put.mockResolvedValue(response);

        const result = await editHabits(10, '12345678901', data);

        expect(api.put).toHaveBeenCalledWith('/habitos/editar/10/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve deletar hábitos', async () => {
        const response = { data: { deletado: true } };

        api.delete.mockResolvedValue(response);

        const result = await deleteHabits(10, '12345678901');

        expect(api.delete).toHaveBeenCalledWith('/habitos/deletar/10/12345678901');
        expect(result).toEqual(response.data);
    });

    it('deve buscar hábitos', async () => {
        const response = { data: [{ id: 1, agua: true }] };

        api.get.mockResolvedValue(response);

        const result = await getHabits('12345678901');

        expect(api.get).toHaveBeenCalledWith('/habitos/getHabitos/12345678901');
        expect(result).toEqual(response.data);
    });
});