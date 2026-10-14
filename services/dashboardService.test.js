import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import { getDashboard } from './dashboardService';

vi.mock('./api', () => ({
    default: {
        get: vi.fn()
    }
}));

describe('dashboardService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve buscar dashboard sem categorias', async () => {
        const response = { data: { cards: [] } };

        api.get.mockResolvedValue(response);

        const result = await getDashboard({
            cpf: '12345678901',
            inicio: '2026-01-01',
            fim: '2026-01-31'
        });

        expect(api.get).toHaveBeenCalledWith('/dashboard/pacientes/12345678901', {
            params: {
                inicio: '2026-01-01',
                fim: '2026-01-31'
            }
        });
        expect(result).toEqual(response.data);
    });

    it('deve buscar dashboard com categorias', async () => {
        const response = { data: { cards: [] } };

        api.get.mockResolvedValue(response);

        const result = await getDashboard({
            cpf: '12345678901',
            inicio: '2026-01-01',
            fim: '2026-01-31',
            categorias: 'vitals,habits'
        });

        expect(api.get).toHaveBeenCalledWith('/dashboard/pacientes/12345678901', {
            params: {
                inicio: '2026-01-01',
                fim: '2026-01-31',
                categorias: 'vitals,habits'
            }
        });
        expect(result).toEqual(response.data);
    });
});
