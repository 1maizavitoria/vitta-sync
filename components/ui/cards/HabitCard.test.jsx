import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import HabitCard from './HabitCard';

afterEach(() => {
    cleanup();
});

describe('HabitCard', () => {
    it('deve renderizar informações principais do hábito', () => {
        render(
            <HabitCard
                icon={<span>icone</span>}
                title="Água"
                value="2"
                unit="L"
                date="15/06/2026"
            />
        );

        expect(screen.getByText('Água')).toBeInTheDocument();
        expect(screen.getByText('2 L')).toBeInTheDocument();
        expect(screen.getByText('Último registro')).toBeInTheDocument();
        expect(screen.getByText('15/06/2026')).toBeInTheDocument();
    });

    it('deve renderizar informações de quem registrou', () => {
        render(
            <HabitCard
                title="Sono"
                value="8"
                unit="h"
                date="15/06/2026"
                userName="Lucas"
                userFunction="Cuidador"
                userStyle={{
                    color: '#e65100',
                    background: '#fff3e0'
                }}
            />
        );

        expect(screen.getByText(/Lucas/)).toBeInTheDocument();
        expect(screen.getByText('Cuidador')).toBeInTheDocument();
    });

    it('deve renderizar input quando showInput for true', () => {
        render(
            <HabitCard
                title="Água"
                value="2"
                unit="L"
                date="15/06/2026"
                showInput
                inputValue="3"
                onInputChange={() => { }}
            />
        );

        expect(screen.getByDisplayValue('3')).toBeInTheDocument();
    });

    it('deve chamar onInputChange ao alterar input', () => {
        const onInputChange = vi.fn();

        render(
            <HabitCard
                title="Água"
                value="2"
                unit="L"
                date="15/06/2026"
                showInput
                inputValue="3"
                onInputChange={onInputChange}
            />
        );

        fireEvent.change(screen.getByDisplayValue('3'), {
            target: { value: '4' }
        });

        expect(onInputChange).toHaveBeenCalled();
    });
});