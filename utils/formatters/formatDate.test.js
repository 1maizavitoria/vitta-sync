import { describe, it, expect } from 'vitest';
import { daysMap, formatDate } from './formatDate';

describe('daysMap', () => {
    it('deve retornar os dias da semana traduzidos', () => {
        const days = daysMap();

        expect(days.MONDAY).toBe('Segunda-feira');
        expect(days.WEDNESDAY).toBe('Quarta-feira');
        expect(days.SUNDAY).toBe('Domingo');
    });
});

describe('formatDate', () => {
    it('deve retornar string vazia quando não receber data', () => {
        expect(formatDate('')).toBe('');
        expect(formatDate(null)).toBe('');
        expect(formatDate(undefined)).toBe('');
    });

    it('deve formatar data no padrão brasileiro', () => {
        expect(formatDate('2024-01-15T12:00:00')).toBe('15/01/2024');
    });
});