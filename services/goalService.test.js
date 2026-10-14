import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import {
    getGoals,
    createGoal,
    updateGoal,
    deleteGoal,
    completeGoal,
    updateGoalValue
} from './goalService';

vi.mock('./api', () => ({
    default: {
        get: vi.fn(),
        post: vi.fn(),
        put: vi.fn(),
        delete: vi.fn()
    }
}));

describe('goalService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve buscar metas', async () => {
        const response = { data: [{ id: 1, titulo: 'Beber água' }] };

        api.get.mockResolvedValue(response);

        const result = await getGoals('12345678901');

        expect(api.get).toHaveBeenCalledWith('/metaacompanhamento/getMetas/12345678901');
        expect(result).toEqual(response.data);
    });

    it('deve cadastrar meta', async () => {
        const data = { titulo: 'Caminhar', valorAlvo: 30 };
        const response = { data: { id: 1, ...data } };

        api.post.mockResolvedValue(response);

        const result = await createGoal('12345678901', data);

        expect(api.post).toHaveBeenCalledWith('/metaacompanhamento/cadastrar/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve editar meta', async () => {
        const data = { titulo: 'Caminhar mais', valorAlvo: 45 };
        const response = { data: { atualizado: true } };

        api.put.mockResolvedValue(response);

        const result = await updateGoal(10, '12345678901', data);

        expect(api.put).toHaveBeenCalledWith('/metaacompanhamento/editar/10/12345678901', data);
        expect(result).toEqual(response.data);
    });

    it('deve deletar meta', async () => {
        api.delete.mockResolvedValue();

        const result = await deleteGoal(10, '12345678901');

        expect(api.delete).toHaveBeenCalledWith('/metaacompanhamento/deletar/10/12345678901');
        expect(result).toBeUndefined();
    });

    it('deve concluir meta', async () => {
        const response = { data: { concluida: true } };

        api.post.mockResolvedValue(response);

        const result = await completeGoal(10, '12345678901');

        expect(api.post).toHaveBeenCalledWith('/metaacompanhamento/concluir/10/12345678901');
        expect(result).toEqual(response.data);
    });

    it('deve atualizar valor atual da meta', async () => {
        const response = { data: { valorAtual: 5 } };

        api.put.mockResolvedValue(response);

        const result = await updateGoalValue(10, '12345678901', 5);

        expect(api.put).toHaveBeenCalledWith(
            '/metaacompanhamento/atualizar-valor/10/12345678901',
            { valorAtual: 5 }
        );
        expect(result).toEqual(response.data);
    });
});
