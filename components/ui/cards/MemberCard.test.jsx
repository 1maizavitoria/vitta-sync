import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import MemberCard from './MemberCard';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { LanguageProvider } from '../../../src/i18n';

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

function renderWithProviders(component) {
    return render(
        <ThemeProvider theme={theme}>
            <LanguageProvider>
                {component}
            </LanguageProvider>
        </ThemeProvider>
    );
}

afterEach(() => {
    cleanup();
});

const link = {
    nome: 'Lucas Silva',
    email: 'lucas@email.com',
    conselho: 'CRM 12345',
    criadoEm: '2026-06-15T12:00:00'
};

const typeStyle = {
    label: 'Paciente',
    background: '#eeeeee',
    color: '#616161'
};

describe('MemberCard', () => {
    it('deve renderizar informações do membro', () => {
        renderWithProviders(
            <MemberCard
                link={link}
                typeStyle={typeStyle}
            />
        );

        expect(screen.getByText('Lucas Silva')).toBeInTheDocument();
        expect(screen.getByText('lucas@email.com')).toBeInTheDocument();
        expect(screen.getByText('CRM 12345')).toBeInTheDocument();
        expect(screen.getByText('Paciente')).toBeInTheDocument();
        expect(screen.getByText(/Vinculado em/)).toBeInTheDocument();
    });

    it('deve renderizar iniciais do nome', () => {
        renderWithProviders(
            <MemberCard
                link={link}
                typeStyle={typeStyle}
            />
        );

        expect(screen.getByText('LS')).toBeInTheDocument();
    });

    it('deve chamar onRemove ao clicar no botão de remover', () => {
        const onRemove = vi.fn();

        renderWithProviders(
            <MemberCard
                link={link}
                typeStyle={typeStyle}
                onRemove={onRemove}
            />
        );

        fireEvent.click(screen.getByRole('button'));

        expect(onRemove).toHaveBeenCalledTimes(1);
    });

    it('não deve renderizar botão de remover quando hideRemove for true', () => {
        renderWithProviders(
            <MemberCard
                link={link}
                typeStyle={typeStyle}
                onRemove={() => { }}
                hideRemove
            />
        );

        expect(screen.queryByRole('button')).not.toBeInTheDocument();
    });
});