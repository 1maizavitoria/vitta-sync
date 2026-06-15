import { describe, it, expect, vi, afterEach } from 'vitest';
import dayjs from 'dayjs';
import { getDateLimit, isUnder18 } from './dateValidator';

describe('dateValidator', () => {
    afterEach(() => {
        vi.useRealTimers();
    });

    it('deve retornar data limite de 18 anos atrás para responsável', () => {
        vi.setSystemTime(new Date('2026-06-15T12:00:00'));

        const result = getDateLimit('responsavel');

        expect(result.format('YYYY-MM-DD')).toBe('2008-06-15');
    });

    it('deve retornar data limite de 18 anos atrás para profissional de saúde', () => {
        vi.setSystemTime(new Date('2026-06-15T12:00:00'));

        const result = getDateLimit('saude');

        expect(result.format('YYYY-MM-DD')).toBe('2008-06-15');
    });

    it('deve retornar a data atual para outros tipos de usuário', () => {
        vi.setSystemTime(new Date('2026-06-15T12:00:00'));

        const result = getDateLimit('paciente');

        expect(result.format('YYYY-MM-DD')).toBe('2026-06-15');
    });

    it('deve identificar menor de 18 anos', () => {
        vi.setSystemTime(new Date('2026-06-15T12:00:00'));

        expect(isUnder18('2010-06-15')).toBe(true);
    });

    it('deve identificar pessoa com 18 anos ou mais', () => {
        vi.setSystemTime(new Date('2026-06-15T12:00:00'));

        expect(isUnder18('2008-06-15')).toBe(false);
    });
});