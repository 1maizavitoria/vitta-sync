import { describe, it, expect } from 'vitest';
import { formatPhone, isValidPhone } from './formatPhone';

describe('formatPhone', () => {
    it('deve formatar telefone fixo com 10 dígitos', () => {
        expect(formatPhone('1133334444')).toBe('(11) 3333-4444');
    });

    it('deve formatar celular com 11 dígitos', () => {
        expect(formatPhone('11999994444')).toBe('(11) 99999-4444');
    });

    it('deve remover letras e símbolos', () => {
        expect(formatPhone('(11) 99999-4444')).toBe('(11) 99999-4444');
    });

    it('deve formatar telefone parcialmente digitado', () => {
        expect(formatPhone('1199')).toBe('(11) 99');
    });
});

describe('isValidPhone', () => {
    it('deve aceitar telefone com 10 dígitos', () => {
        expect(isValidPhone('1133334444')).toBe(true);
    });

    it('deve aceitar telefone com 11 dígitos', () => {
        expect(isValidPhone('11999994444')).toBe(true);
    });

    it('deve rejeitar telefone com tamanho inválido', () => {
        expect(isValidPhone('11999')).toBe(false);
    });

    it('deve rejeitar telefone com máscara', () => {
        expect(isValidPhone('(11) 99999-4444')).toBe(false);
    });
});