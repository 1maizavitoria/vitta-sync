import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { MemoryRouter } from 'react-router-dom';
import LinkUI from './Link';

afterEach(() => {
    cleanup();
});

describe('LinkUI', () => {
    it('deve renderizar link interno quando receber to', () => {
        render(
            <MemoryRouter>
                <LinkUI to="/login">Entrar</LinkUI>
            </MemoryRouter>
        );

        expect(screen.getByRole('link', { name: 'Entrar' }))
            .toHaveAttribute('href', '/login');
    });

    it('deve renderizar link externo quando receber href', () => {
        render(<LinkUI href="https://example.com">Site</LinkUI>);

        expect(screen.getByRole('link', { name: 'Site' }))
            .toHaveAttribute('href', 'https://example.com');
    });

    it('deve renderizar botão quando receber onClick', () => {
        const onClick = vi.fn();

        render(<LinkUI onClick={onClick}>Clique aqui</LinkUI>);

        fireEvent.click(screen.getByRole('button', { name: 'Clique aqui' }));

        expect(onClick).toHaveBeenCalledTimes(1);
    });

    it('deve renderizar span quando não receber ação', () => {
        render(<LinkUI>Texto simples</LinkUI>);

        expect(screen.getByText('Texto simples').tagName).toBe('SPAN');
    });
});