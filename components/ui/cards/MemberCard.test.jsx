import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import MemberCard from './MemberCard';

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
        render(
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
        render(
            <MemberCard
                link={link}
                typeStyle={typeStyle}
            />
        );

        expect(screen.getByText('LU')).toBeInTheDocument();
    });

    it('deve chamar onRemove ao clicar no botão de remover', () => {
        const onRemove = vi.fn();

        render(
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
        render(
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