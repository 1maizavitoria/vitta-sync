import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import {
    getLinks,
    removeLink,
    generateLinkCode,
    joinWithCode,
    sendInviteEmail,
    getAvailablePatients,
    getLinksByPatientId
} from './linkService';

vi.mock('./api', () => ({
    default: {
        get: vi.fn(),
        post: vi.fn(),
        delete: vi.fn()
    }
}));

describe('linkService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve buscar vínculos', async () => {
        const response = { data: [{ id: 1 }] };

        api.get.mockResolvedValue(response);

        const result = await getLinks();

        expect(api.get).toHaveBeenCalledWith('/vinculos');
        expect(result).toEqual(response.data);
    });

    it('deve remover vínculo', async () => {
        const response = { data: { removido: true } };

        api.delete.mockResolvedValue(response);

        const result = await removeLink(10);

        expect(api.delete).toHaveBeenCalledWith('/vinculos/10');
        expect(result).toEqual(response.data);
    });

    it('deve gerar código de vínculo', async () => {
        const response = { data: { codigo: 'ABC123' } };

        api.post.mockResolvedValue(response);

        const result = await generateLinkCode(20);

        expect(api.post).toHaveBeenCalledWith('/vinculos/gerar', {
            pacienteId: 20
        });
        expect(result).toEqual(response.data);
    });

    it('deve entrar com código e função', async () => {
        const response = { data: { vinculado: true } };

        api.post.mockResolvedValue(response);

        const result = await joinWithCode('ABC123', 'cuidador');

        expect(api.post).toHaveBeenCalledWith('/vinculos/entrar', {
            codigo: 'ABC123',
            funcao: 'cuidador'
        });
        expect(result).toEqual(response.data);
    });

    it('deve enviar convite por e-mail', async () => {
        const response = { data: { enviado: true } };

        api.post.mockResolvedValue(response);

        const result = await sendInviteEmail('usuario@email.com', 'ABC123');

        expect(api.post).toHaveBeenCalledWith('/vinculos/enviar-email', {
            email: 'usuario@email.com',
            codigo: 'ABC123'
        });
        expect(result).toEqual(response.data);
    });

    it('deve buscar pacientes disponíveis', async () => {
        const response = { data: [{ id: 1, nome: 'Paciente' }] };

        api.get.mockResolvedValue(response);

        const result = await getAvailablePatients();

        expect(api.get).toHaveBeenCalledWith('/vinculos/pacientes');
        expect(result).toEqual(response.data);
    });

    it('deve buscar vínculos por paciente', async () => {
        const response = { data: [{ id: 1, pacienteId: 20 }] };

        api.get.mockResolvedValue(response);

        const result = await getLinksByPatientId(20);

        expect(api.get).toHaveBeenCalledWith('/vinculos/paciente/20');
        expect(result).toEqual(response.data);
    });
});