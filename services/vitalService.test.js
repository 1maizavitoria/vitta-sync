import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import {
    registerVitalSigns,
    getVitalSigns,
    editVitalSigns,
    deleteVitalSigns
} from './vitalService';

vi.mock('./api', () => ({
    default: {
        post: vi.fn(),
        get: vi.fn(),
        put: vi.fn(),
        delete: vi.fn()
    }
}));

describe('vitalService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve cadastrar sinais vitais', async () => {
        const data = { temperatura: 36.5 };
        const response = { data: { id: 1 } };

        api.post.mockResolvedValue(response);

        const result = await registerVitalSigns('12345678901', data);

        expect(api.post).toHaveBeenCalledWith('/sinaisvitais/cadastrar/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve buscar sinais vitais', async () => {
        const response = { data: [{ id: 1, temperatura: 36.5 }] };

        api.get.mockResolvedValue(response);

        const result = await getVitalSigns('12345678901');

        expect(api.get).toHaveBeenCalledWith('/sinaisvitais/getSinaisVitais/12345678901');
        expect(result).toEqual(response.data);
    });

    it('deve editar sinais vitais', async () => {
        const data = { temperatura: 37.2 };
        const response = { data: { atualizado: true } };

        api.put.mockResolvedValue(response);

        const result = await editVitalSigns(10, '12345678901', data);

        expect(api.put).toHaveBeenCalledWith('/sinaisvitais/editar/10/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve deletar sinais vitais', async () => {
        const response = { data: { deletado: true } };

        api.delete.mockResolvedValue(response);

        const result = await deleteVitalSigns(10, '12345678901');

        expect(api.delete).toHaveBeenCalledWith('/sinaisvitais/deletar/10/12345678901');
        expect(result).toEqual(response.data);
    });
});