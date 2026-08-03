import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { LanguageProvider } from '../../src/i18n';
import ReminderList from './RemindList';

function renderWithLanguage(component) {
    return render(
        <LanguageProvider>
            {component}
        </LanguageProvider>
    );
}

afterEach(() => {
    cleanup();
});

describe('ReminderList', () => {
    it('não deve renderizar quando não houver lembrete', () => {
        const { container } = renderWithLanguage(
            <ReminderList
                reminder={null}
                handleToggleReminder={() => { }}
            />
        );

        expect(container).toBeEmptyDOMElement();
    });

    it('não deve renderizar quando lembrete não tiver dias ou horário', () => {
        const { container } = renderWithLanguage(
            <ReminderList
                reminder={{ diasSemana: '', horario: '' }}
                handleToggleReminder={() => { }}
            />
        );

        expect(container).toBeEmptyDOMElement();
    });

    it('deve renderizar informações do lembrete', () => {
        renderWithLanguage(
            <ReminderList
                reminder={{
                    id: 1,
                    diasSemana: 'MONDAY,WEDNESDAY,FRIDAY',
                    horario: '08:30:00',
                    ativo: true
                }}
                handleToggleReminder={() => { }}
            />
        );

        expect(screen.getByText('Fazer medição')).toBeInTheDocument();
        expect(screen.getByText('08:30')).toBeInTheDocument();
        expect(screen.getByText('Seg, Qua, Sex')).toBeInTheDocument();
    });

    it('deve chamar handleToggleReminder ao clicar no switch', () => {
        const handleToggleReminder = vi.fn();

        renderWithLanguage(
            <ReminderList
                reminder={{
                    id: 1,
                    diasSemana: 'MONDAY',
                    horario: '08:30:00',
                    ativo: true
                }}
                handleToggleReminder={handleToggleReminder}
            />
        );

        fireEvent.click(screen.getByRole('switch'));

        expect(handleToggleReminder).toHaveBeenCalledTimes(1);
    });
});