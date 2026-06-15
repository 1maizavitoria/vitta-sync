import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import ButtonUI from './Button';

afterEach(() => {
    cleanup();
});

describe('ButtonUI', () => {
    it('deve renderizar o texto do botão', () => {
        render(<ButtonUI>Salvar</ButtonUI>);

        expect(screen.getByRole('button', { name: /salvar/i }))
            .toBeInTheDocument();
    });

    it('deve chamar onClick ao clicar', () => {
        const onClick = vi.fn();

        render(<ButtonUI onClick={onClick}>Salvar</ButtonUI>);

        fireEvent.click(screen.getByRole('button', { name: /salvar/i }));

        expect(onClick).toHaveBeenCalledTimes(1);
    });

    it('deve repassar props para o botão', () => {
        render(<ButtonUI disabled>Salvar</ButtonUI>);

        expect(screen.getByRole('button', { name: /salvar/i }))
            .toBeDisabled();
    });
});