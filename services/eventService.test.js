import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import {
    getPatientEvents,
    getUnreadEventsCount,
    markEventsAsRead
} from './eventService';

vi.mock('./api', () => ({
    default: {
        get: vi.fn(),
        put: vi.fn()
    }
}));

describe('eventService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve buscar eventos do paciente', async () => {
        const response = { data: [{ id: 1, titulo: 'Consulta' }] };

        api.get.mockResolvedValue(response);

        const result = await getPatientEvents(10);

        expect(api.get).toHaveBeenCalledWith('/eventos/paciente/10');
        expect(result).toEqual(response.data);
    });

    it('deve buscar quantidade de eventos não visualizados', async () => {
        const response = { data: 3 };

        api.get.mockResolvedValue(response);

        const result = await getUnreadEventsCount(10);

        expect(api.get).toHaveBeenCalledWith('/eventos/paciente/10/nao-visualizados');
        expect(result).toBe(3);
    });

    it('deve marcar eventos como visualizados', async () => {
        api.put.mockResolvedValue();

        const result = await markEventsAsRead(10);

        expect(api.put).toHaveBeenCalledWith('/eventos/paciente/10/visualizar');
        expect(result).toBeUndefined();
    });
});