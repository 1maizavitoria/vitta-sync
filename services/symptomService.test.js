import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import {
    registerSymptom,
    editSymptom,
    deleteSymptom,
    getSymptom
} from './symptomService';

vi.mock('./api', () => ({
    default: {
        post: vi.fn(),
        put: vi.fn(),
        delete: vi.fn(),
        get: vi.fn()
    }
}));

describe('symptomService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve cadastrar sintoma', async () => {
        const data = { sintoma: 'Dor de cabeça' };
        const response = { data: { id: 1 } };

        api.post.mockResolvedValue(response);

        const result = await registerSymptom('12345678901', data);

        expect(api.post).toHaveBeenCalledWith('/diariosintomas/cadastrar/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve editar sintoma', async () => {
        const data = { sintoma: 'Náusea' };
        const response = { data: { atualizado: true } };

        api.put.mockResolvedValue(response);

        const result = await editSymptom(10, '12345678901', data);

        expect(api.put).toHaveBeenCalledWith('/diariosintomas/editar/10/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve deletar sintoma', async () => {
        const response = { data: { deletado: true } };

        api.delete.mockResolvedValue(response);

        const result = await deleteSymptom(10, '12345678901');

        expect(api.delete).toHaveBeenCalledWith('/diariosintomas/deletar/10/12345678901');
        expect(result).toEqual(response.data);
    });

    it('deve buscar sintomas', async () => {
        const response = { data: [{ id: 1, sintoma: 'Dor de cabeça' }] };

        api.get.mockResolvedValue(response);

        const result = await getSymptom('12345678901');

        expect(api.get).toHaveBeenCalledWith('/diariosintomas/getSintomas/12345678901');
        expect(result).toEqual(response.data);
    });
});