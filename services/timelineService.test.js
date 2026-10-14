import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import { getClinicalTimeline } from './timelineService';

vi.mock('./api', () => ({
    default: {
        get: vi.fn()
    }
}));

describe('timelineService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve buscar linha do tempo clínica', async () => {
        const response = { data: [{ id: 1, tipo: 'VITAL_SIGNS_CREATED' }] };

        api.get.mockResolvedValue(response);

        const result = await getClinicalTimeline({
            cpf: '12345678901',
            inicio: '2026-01-01',
            fim: '2026-01-31'
        });

        expect(api.get).toHaveBeenCalledWith('/api/linha-tempo/paciente/12345678901', {
            params: {
                inicio: '2026-01-01',
                fim: '2026-01-31'
            }
        });
        expect(result).toEqual(response.data);
    });
});
