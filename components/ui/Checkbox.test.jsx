import { describe, it, expect, vi, afterEach } from 'vitest';
import { render, screen, fireEvent, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import CheckboxUI from './Checkbox';

afterEach(() => {
    cleanup();
});

describe('CheckboxUI', () => {
    it('deve renderizar o label', () => {
        render(<CheckboxUI label="Aceito os termos" />);

        expect(screen.getByLabelText('Aceito os termos'))
            .toBeInTheDocument();
    });

    it('deve renderizar marcado quando checked for true', () => {
        render(<CheckboxUI label="Ativo" checked />);

        expect(screen.getByLabelText('Ativo'))
            .toBeChecked();
    });

    it('deve chamar onChange com true ao marcar', () => {
        const onChange = vi.fn();

        render(
            <CheckboxUI
                label="Receber notificações"
                checked={false}
                onChange={onChange}
            />
        );

        fireEvent.click(screen.getByLabelText('Receber notificações'));

        expect(onChange).toHaveBeenCalledWith(true);
    });

    it('deve chamar onChange com false ao desmarcar', () => {
        const onChange = vi.fn();

        render(
            <CheckboxUI
                label="Receber notificações"
                checked
                onChange={onChange}
            />
        );

        fireEvent.click(screen.getByLabelText('Receber notificações'));

        expect(onChange).toHaveBeenCalledWith(false);
    });
});