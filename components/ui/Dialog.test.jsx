import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import DialogUI from './Dialog';

afterEach(() => {
    cleanup();
});

describe('DialogUI', () => {
    it('deve renderizar título e conteúdo quando aberto', () => {
        render(
            <DialogUI
                open
                title="Confirmar ação"
                onClose={() => { }}
            >
                Conteúdo do diálogo
            </DialogUI>
        );

        expect(screen.getByText('Confirmar ação')).toBeInTheDocument();
        expect(screen.getByText('Conteúdo do diálogo')).toBeInTheDocument();
    });

    it('não deve renderizar conteúdo quando fechado', () => {
        render(
            <DialogUI
                open={false}
                title="Confirmar ação"
                onClose={() => { }}
            >
                Conteúdo do diálogo
            </DialogUI>
        );

        expect(screen.queryByText('Confirmar ação')).not.toBeInTheDocument();
        expect(screen.queryByText('Conteúdo do diálogo')).not.toBeInTheDocument();
    });

    it('deve chamar onClose ao clicar em cancelar', () => {
        const onClose = vi.fn();

        render(
            <DialogUI
                open
                title="Confirmar ação"
                onClose={onClose}
            >
                Conteúdo do diálogo
            </DialogUI>
        );

        fireEvent.click(screen.getByRole('button', { name: /cancelar/i }));

        expect(onClose).toHaveBeenCalledTimes(1);
    });

    it('deve chamar onConfirm ao clicar em confirmar', () => {
        const onConfirm = vi.fn();

        render(
            <DialogUI
                open
                title="Confirmar ação"
                onClose={() => { }}
                onConfirm={onConfirm}
                confirmText="Salvar"
            >
                Conteúdo do diálogo
            </DialogUI>
        );

        fireEvent.click(screen.getByRole('button', { name: /salvar/i }));

        expect(onConfirm).toHaveBeenCalledTimes(1);
    });

    it('não deve renderizar cancelar quando disabledClose for true', () => {
        render(
            <DialogUI
                open
                title="Confirmar ação"
                onClose={() => { }}
                disabledClose
            >
                Conteúdo do diálogo
            </DialogUI>
        );

        expect(screen.queryByRole('button', { name: /cancelar/i }))
            .not.toBeInTheDocument();
    });

    it('não deve renderizar confirmar quando disabledConfirm for true', () => {
        render(
            <DialogUI
                open
                title="Confirmar ação"
                onClose={() => { }}
                onConfirm={() => { }}
                disabledConfirm
            >
                Conteúdo do diálogo
            </DialogUI>
        );

        expect(screen.queryByRole('button', { name: /confirmar/i }))
            .not.toBeInTheDocument();
    });
});