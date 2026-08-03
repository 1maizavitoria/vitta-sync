import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import SymptomCard from './SymptomCard';
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

describe('SymptomCard', () => {
    it('deve renderizar informações principais do sintoma', () => {
        renderWithProviders(
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
        renderWithProviders(
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
        renderWithProviders(
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

        renderWithProviders(
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