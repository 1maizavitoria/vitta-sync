import { describe, it, expect } from 'vitest';
import {
    hasMinLength,
    hasUppercase,
    hasLowercase,
    hasNumber,
    hasSpecialChar,
    validatePassword
} from './passwordValidator';

describe('passwordValidator', () => {
    it('deve validar comprimento mínimo', () => {
        expect(hasMinLength('12345678')).toBe(true);
        expect(hasMinLength('1234567')).toBe(false);
    });

    it('deve validar letra maiúscula', () => {
        expect(hasUppercase('Abc')).toBe(true);
        expect(hasUppercase('abc')).toBe(false);
    });

    it('deve validar letra minúscula', () => {
        expect(hasLowercase('AbC')).toBe(true);
        expect(hasLowercase('ABC')).toBe(false);
    });

    it('deve validar número', () => {
        expect(hasNumber('abc1')).toBe(true);
        expect(hasNumber('abc')).toBe(false);
    });

    it('deve validar caractere especial', () => {
        expect(hasSpecialChar('abc@')).toBe(true);
        expect(hasSpecialChar('abc')).toBe(false);
    });

    it('deve retornar senha válida', () => {
        const result = validatePassword('Abc123@#');

        expect(result.isValid).toBe(true);
    });

    it('deve retornar senha inválida', () => {
        const result = validatePassword('abc123');

        expect(result.isValid).toBe(false);
        expect(result.upperCase).toBe(false);
        expect(result.specialChar).toBe(false);
    });
});