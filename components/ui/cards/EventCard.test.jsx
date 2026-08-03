import { describe, it, expect, afterEach } from 'vitest';
import { render, screen, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { LanguageProvider } from '../../../src/i18n';
import EventCard from './EventCard';

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

describe('EventCard', () => {
    it('deve renderizar informações do evento', () => {
        renderWithProviders(
            <EventCard
                event={{
                    titulo: 'Consulta marcada',
                    descricao: 'Consulta com cardiologista',
                    prioridade: 'alta',
                    usuarioNome: 'Lucas',
                    usuarioTipo: 'Paciente',
                    criadoEm: '2026-06-15T12:00:00'
                }}
            />
        );

        expect(screen.getByText('Consulta marcada')).toBeInTheDocument();
        expect(screen.getByText('Consulta com cardiologista')).toBeInTheDocument();
        expect(screen.getByText('Alta')).toBeInTheDocument();
        expect(screen.getByText(/Lucas/)).toBeInTheDocument();
        expect(screen.getByText(/Paciente/)).toBeInTheDocument();
    });

    it('deve renderizar prioridade crítica', () => {
        renderWithProviders(
            <EventCard
                event={{
                    titulo: 'Evento crítico',
                    descricao: 'Descrição',
                    prioridade: 'critico',
                    usuarioNome: 'Ana',
                    usuarioTipo: 'Médico',
                    criadoEm: '2026-06-15T12:00:00'
                }}
            />
        );

        expect(screen.getByText('Crítico')).toBeInTheDocument();
    });

    it('deve renderizar prioridade padrão', () => {
        renderWithProviders(
            <EventCard
                event={{
                    titulo: 'Evento normal',
                    descricao: 'Descrição',
                    prioridade: 'normal',
                    usuarioNome: 'Ana',
                    usuarioTipo: 'Médico',
                    criadoEm: '2026-06-15T12:00:00'
                }}
            />
        );

        expect(screen.getByText('Normal')).toBeInTheDocument();
    });
});