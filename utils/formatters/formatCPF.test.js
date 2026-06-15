import { describe, it, expect } from 'vitest';
import { formatCPF, isValidCpf } from './formatCPF';

describe('formatCPF', () => {
    it('deve formatar CPF completo', () => {
        expect(formatCPF('12345678901'))
            .toBe('123.456.789-01');
    });

    it('deve formatar CPF parcialmente digitado', () => {
        expect(formatCPF('1234'))
            .toBe('123.4');
    });

    it('deve remover letras e símbolos', () => {
        expect(formatCPF('123a456b78901'))
            .toBe('123.456.789-01');
    });

    it('deve limitar a 11 dígitos', () => {
        expect(formatCPF('123456789012345'))
            .toBe('123.456.789-01');
    });
});

describe('isValidCpf', () => {
    it('deve aceitar CPF válido sem máscara', () => {
        expect(isValidCpf('52998224725'))
            .toBe(true);
    });

    it('deve aceitar CPF válido com máscara', () => {
        expect(isValidCpf('529.982.247-25'))
            .toBe(true);
    });

    it('deve rejeitar CPF com todos os números iguais', () => {
        expect(isValidCpf('11111111111'))
            .toBe(false);
    });

    it('deve rejeitar CPF com tamanho inválido', () => {
        expect(isValidCpf('123'))
            .toBe(false);
    });

    it('deve rejeitar CPF com dígito verificador incorreto', () => {
        expect(isValidCpf('52998224724'))
            .toBe(false);
    });
});