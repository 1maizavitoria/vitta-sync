import { describe, it, expect, afterEach } from 'vitest';
import { render, screen, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import PasswordTooltip from './Tooltip';

afterEach(() => {
    cleanup();
});

describe('PasswordTooltip', () => {
    it('deve renderizar todas as regras de senha', () => {
        render(
            <PasswordTooltip
                rules={{
                    minLength: true,
                    upperCase: false,
                    lowerCase: true,
                    number: false,
                    specialChar: true
                }}
            />
        );

        expect(screen.getByText(/8 caracteres/i)).toBeInTheDocument();
        expect(screen.getByText(/Letra maiúscula/i)).toBeInTheDocument();
        expect(screen.getByText(/Letra minúscula/i)).toBeInTheDocument();
        expect(screen.getByText(/Número/i)).toBeInTheDocument();
        expect(screen.getByText(/Caractere especial/i)).toBeInTheDocument();
    });

    it('deve usar cor de válido para regra verdadeira', () => {
        render(
            <PasswordTooltip
                rules={{
                    minLength: true,
                    upperCase: false,
                    lowerCase: false,
                    number: false,
                    specialChar: false
                }}
            />
        );

        expect(screen.getByText(/8 caracteres/i).style.color)
            .toBe('lightgreen');
    });

    it('deve usar cor de inválido para regra falsa', () => {
        render(
            <PasswordTooltip
                rules={{
                    minLength: false,
                    upperCase: false,
                    lowerCase: false,
                    number: false,
                    specialChar: false
                }}
            />
        );

        expect(screen.getByText(/8 caracteres/i).style.color)
            .toBe('rgb(255, 107, 107)');
    });
});