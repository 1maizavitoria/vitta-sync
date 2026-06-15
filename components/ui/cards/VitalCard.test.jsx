import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import VitalCard from './VitalCard';

afterEach(() => {
    cleanup();
});

describe('VitalCard', () => {
    it('deve renderizar informações principais do sinal vital', () => {
        render(
            <VitalCard
                icon={<span>icone</span>}
                title="Temperatura"
                value="36.5"
                unit="°C"
                date="15/06/2026"
            />
        );

        expect(screen.getByText('Temperatura')).toBeInTheDocument();
        expect(screen.getByText('36.5 °C')).toBeInTheDocument();
        expect(screen.getByText('Última medição')).toBeInTheDocument();
        expect(screen.getByText('15/06/2026')).toBeInTheDocument();
    });

    it('deve renderizar informações de quem registrou', () => {
        render(
            <VitalCard
                title="Pressão"
                value="120/80"
                unit="mmHg"
                date="15/06/2026"
                userName="Dra. Ana"
                userFunction="Especialista"
                userStyle={{
                    color: '#2e7d32',
                    background: '#e8f5e9'
                }}
            />
        );

        expect(screen.getByText(/Dra. Ana/)).toBeInTheDocument();
        expect(screen.getByText('Especialista')).toBeInTheDocument();
    });

    it('deve renderizar input quando showInput for true', () => {
        render(
            <VitalCard
                title="Temperatura"
                value="36.5"
                unit="°C"
                date="15/06/2026"
                showInput
                inputValue="37"
                onInputChange={() => { }}
            />
        );

        expect(screen.getByDisplayValue('37')).toBeInTheDocument();
    });

    it('deve chamar onInputChange ao alterar input', () => {
        const onInputChange = vi.fn();

        render(
            <VitalCard
                title="Temperatura"
                value="36.5"
                unit="°C"
                date="15/06/2026"
                showInput
                inputValue="37"
                onInputChange={onInputChange}
            />
        );

        fireEvent.change(screen.getByDisplayValue('37'), {
            target: { value: '38' }
        });

        expect(onInputChange).toHaveBeenCalled();
    });
});