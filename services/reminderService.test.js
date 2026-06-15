import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import {
    registerReminder,
    getReminder,
    activateReminder,
    deactivateReminder
} from './reminderService';

vi.mock('./api', () => ({
    default: {
        post: vi.fn(),
        get: vi.fn(),
        put: vi.fn()
    }
}));

describe('reminderService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve registrar lembrete', async () => {
        const data = { titulo: 'Tomar remédio' };
        const response = { data: { id: 1 } };

        api.post.mockResolvedValue(response);

        const result = await registerReminder('12345678901', data);

        expect(api.post).toHaveBeenCalledWith('/lembretes/registrar/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve buscar lembrete', async () => {
        const response = { data: [{ id: 1, titulo: 'Tomar remédio' }] };

        api.get.mockResolvedValue(response);

        const result = await getReminder('12345678901');

        expect(api.get).toHaveBeenCalledWith('/lembretes/getLembrete/12345678901');
        expect(result).toEqual(response.data);
    });

    it('deve ativar lembrete', async () => {
        const response = { data: { ativo: true } };

        api.put.mockResolvedValue(response);

        const result = await activateReminder('12345678901');

        expect(api.put).toHaveBeenCalledWith('/lembretes/ativar/12345678901');
        expect(result).toEqual(response.data);
    });

    it('deve desativar lembrete', async () => {
        const response = { data: { ativo: false } };

        api.put.mockResolvedValue(response);

        const result = await deactivateReminder('12345678901');

        expect(api.put).toHaveBeenCalledWith('/lembretes/desativar/12345678901');
        expect(result).toEqual(response.data);
    });
});