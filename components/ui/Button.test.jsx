import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import ButtonUI from './Button';

const theme = createTheme({
    palette: {
        mode: 'light'
    },
    vitta: {
        borderStrong: '#0f766e'
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

describe('ButtonUI', () => {
    it('deve renderizar o texto do botão', () => {
        renderWithTheme(<ButtonUI>Salvar</ButtonUI>);

        expect(screen.getByRole('button', { name: /salvar/i }))
            .toBeInTheDocument();
    });

    it('deve chamar onClick ao clicar', () => {
        const onClick = vi.fn();

        renderWithTheme(<ButtonUI onClick={onClick}>Salvar</ButtonUI>);

        fireEvent.click(screen.getByRole('button', { name: /salvar/i }));

        expect(onClick).toHaveBeenCalledTimes(1);
    });

    it('deve repassar props para o botão', () => {
        renderWithTheme(<ButtonUI disabled>Salvar</ButtonUI>);

        expect(screen.getByRole('button', { name: /salvar/i }))
            .toBeDisabled();
    });
});