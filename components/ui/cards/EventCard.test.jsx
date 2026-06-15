import { describe, it, expect, afterEach } from 'vitest';
import { render, screen, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import EventCard from './EventCard';

afterEach(() => {
    cleanup();
});

describe('EventCard', () => {
    it('deve renderizar informações do evento', () => {
        render(
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
        expect(screen.getByText('alta')).toBeInTheDocument();
        expect(screen.getByText(/Lucas/)).toBeInTheDocument();
        expect(screen.getByText(/Paciente/)).toBeInTheDocument();
    });

    it('deve renderizar prioridade crítica', () => {
        render(
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

        expect(screen.getByText('critico')).toBeInTheDocument();
    });

    it('deve renderizar prioridade padrão', () => {
        render(
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

        expect(screen.getByText('normal')).toBeInTheDocument();
    });
});