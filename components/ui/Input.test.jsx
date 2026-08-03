import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import InputUI from './Input';

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

describe('InputUI', () => {
    it('deve renderizar input com label', () => {
        renderWithTheme(
            <InputUI
                label="Nome"
                value=""
                onChange={() => { }}
            />
        );

        expect(screen.getByLabelText('Nome')).toBeInTheDocument();
    });

    it('deve chamar onChange ao digitar', () => {
        const onChange = vi.fn();

        renderWithTheme(
            <InputUI
                label="Nome"
                value=""
                onChange={onChange}
            />
        );

        fireEvent.change(screen.getByLabelText('Nome'), {
            target: { value: 'Lucas' }
        });

        expect(onChange).toHaveBeenCalled();
    });

    it('deve aplicar limite de caracteres', () => {
        renderWithTheme(
            <InputUI
                label="CPF"
                value=""
                onChange={() => { }}
                limit={11}
            />
        );

        expect(screen.getByLabelText('CPF'))
            .toHaveAttribute('maxlength', '11');
    });

    it('deve renderizar placeholder', () => {
        renderWithTheme(
            <InputUI
                label="E-mail"
                value=""
                onChange={() => { }}
                placeholder="Digite seu e-mail"
            />
        );

        expect(screen.getByPlaceholderText('Digite seu e-mail'))
            .toBeInTheDocument();
    });

    it('deve alternar visibilidade da senha', () => {
        renderWithTheme(
            <InputUI
                label="Senha"
                type="password"
                value="Senha123"
                onChange={() => { }}
                showPasswordToggle
            />
        );

        const input = screen.getByLabelText('Senha');

        expect(input).toHaveAttribute('type', 'password');

        fireEvent.click(screen.getByRole('button'));

        expect(input).toHaveAttribute('type', 'text');

        fireEvent.click(screen.getByRole('button'));

        expect(input).toHaveAttribute('type', 'password');
    });
});