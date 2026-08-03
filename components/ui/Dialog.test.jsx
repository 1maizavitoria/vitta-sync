import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import DialogUI from './Dialog';

const theme = createTheme({
    palette: {
        mode: 'light'
    },
    vitta: {
        border: '#d1d5db',
        borderStrong: '#0f766e',
        shadow: '0 12px 24px rgba(0, 0, 0, 0.12)'
    }
});

function renderWithTheme(component) {
    return render(
        <ThemeProvider theme={theme}>
            {component}
        </ThemeProvider>
    );
}

afterEach(() => {
    cleanup();
});

describe('DialogUI', () => {
    it('deve renderizar título e conteúdo quando aberto', () => {
        renderWithTheme(
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
        renderWithTheme(
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

        renderWithTheme(
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

        renderWithTheme(
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
        renderWithTheme(
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
        renderWithTheme(
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