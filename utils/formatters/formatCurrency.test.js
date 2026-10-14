import { describe, it, expect } from 'vitest';
import { formatCurrency } from './formatCurrency';

describe('formatCurrency', () => {
    it('deve formatar moeda brasileira por padrão', () => {
        expect(formatCurrency(1234.5)).toBe('R$ 1.234,50');
    });

    it('deve formatar outra moeda', () => {
        expect(formatCurrency(1234.5, 'en-US', 'USD')).toBe('$1,234.50');
    });

    it('deve respeitar opções customizadas', () => {
        expect(formatCurrency(1234.5, 'pt-BR', 'BRL', {
            maximumFractionDigits: 0
        })).toBe('R$ 1.235');
    });
});
