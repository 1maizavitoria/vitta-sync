import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import {
    getEmergencyContacts,
    createEmergencyContact,
    editEmergencyContact,
    deleteEmergencyContact
} from './contactEmergencyService';

vi.mock('./api', () => ({
    default: {
        get: vi.fn(),
        post: vi.fn(),
        put: vi.fn(),
        delete: vi.fn()
    }
}));

describe('contactEmergencyService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve buscar contatos de emergência', async () => {
        const response = { data: [{ id: 1, nome: 'Maria' }] };

        api.get.mockResolvedValue(response);

        const result = await getEmergencyContacts('12345678901');

        expect(api.get).toHaveBeenCalledWith('/contatoemergencia/listar/12345678901');
        expect(result).toEqual(response.data);
    });

    it('deve cadastrar contato de emergência', async () => {
        const data = { nome: 'Maria', telefone: '11999994444' };
        const response = { data: { id: 1 } };

        api.post.mockResolvedValue(response);

        const result = await createEmergencyContact('12345678901', data);

        expect(api.post).toHaveBeenCalledWith('/contatoemergencia/cadastrar/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve editar contato de emergência', async () => {
        const data = { nome: 'Maria Silva' };
        const response = { data: { atualizado: true } };

        api.put.mockResolvedValue(response);

        const result = await editEmergencyContact(10, '12345678901', data);

        expect(api.put).toHaveBeenCalledWith('/contatoemergencia/editar/10/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve deletar contato de emergência', async () => {
        const response = { data: { deletado: true } };

        api.delete.mockResolvedValue(response);

        const result = await deleteEmergencyContact(10, '12345678901');

        expect(api.delete).toHaveBeenCalledWith('/contatoemergencia/deletar/10/12345678901');
        expect(result).toEqual(response.data);
    });
});