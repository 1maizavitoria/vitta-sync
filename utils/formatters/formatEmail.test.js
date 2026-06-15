import { describe, it, expect } from 'vitest';
import { isValidEmail } from './formatEmail';

describe('isValidEmail', () => {
    it('deve aceitar e-mail válido', () => {
        expect(isValidEmail('usuario@email.com')).toBe(true);
    });

    it('deve rejeitar e-mail sem arroba', () => {
        expect(isValidEmail('usuarioemail.com')).toBe(false);
    });

    it('deve rejeitar e-mail sem domínio', () => {
        expect(isValidEmail('usuario@')).toBe(false);
    });

    it('deve rejeitar e-mail sem ponto no domínio', () => {
        expect(isValidEmail('usuario@email')).toBe(false);
    });

    it('deve rejeitar e-mail com espaços', () => {
        expect(isValidEmail('usuario @email.com')).toBe(false);
    });
});