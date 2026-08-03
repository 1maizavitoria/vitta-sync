import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import VitalCard from './VitalCard';
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

describe('VitalCard', () => {
    it('deve renderizar informações principais do sinal vital', () => {
        renderWithProviders(
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
        renderWithProviders(
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
        renderWithProviders(
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

        renderWithProviders(
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