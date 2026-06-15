import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import SymptomCard from './SymptomCard';

afterEach(() => {
    cleanup();
});

describe('SymptomCard', () => {
    it('deve renderizar informações principais do sintoma', () => {
        render(
            <SymptomCard
                icon={<span>icone</span>}
                title="Dor de cabeça"
                value="Leve"
                unit=""
                date="15/06/2026"
            />
        );

        expect(screen.getByText('Dor de cabeça')).toBeInTheDocument();
        expect(screen.getByText(/Leve/)).toBeInTheDocument();
        expect(screen.getByText('Último registro')).toBeInTheDocument();
        expect(screen.getByText('15/06/2026')).toBeInTheDocument();
    });

    it('deve renderizar informações de quem registrou', () => {
        render(
            <SymptomCard
                title="Náusea"
                value="Moderada"
                unit=""
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
            <SymptomCard
                title="Sintoma"
                value="Leve"
                unit=""
                date="15/06/2026"
                showInput
                inputValue="Tosse"
                onInputChange={() => { }}
            />
        );

        expect(screen.getByDisplayValue('Tosse')).toBeInTheDocument();
    });

    it('deve chamar onInputChange ao alterar input', () => {
        const onInputChange = vi.fn();

        render(
            <SymptomCard
                title="Sintoma"
                value="Leve"
                unit=""
                date="15/06/2026"
                showInput
                inputValue="Tosse"
                onInputChange={onInputChange}
            />
        );

        fireEvent.change(screen.getByDisplayValue('Tosse'), {
            target: { value: 'Febre' }
        });

        expect(onInputChange).toHaveBeenCalled();
    });
});