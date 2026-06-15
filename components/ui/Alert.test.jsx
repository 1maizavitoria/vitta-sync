import { describe, it, expect, afterEach } from 'vitest';
import { render, screen, cleanup } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import AlertUI from './Alert';

afterEach(() => {
    cleanup();
});

describe('AlertUI', () => {
    it('deve renderizar a mensagem', () => {
        render(<AlertUI message="Operação realizada com sucesso" />);

        expect(screen.getByText('Operação realizada com sucesso'))
            .toBeInTheDocument();
    });

    it('deve usar tipo info como padrão', () => {
        render(<AlertUI message="Mensagem informativa" />);

        expect(screen.getByRole('alert'))
            .toHaveClass('MuiAlert-standardInfo');
    });

    it('deve renderizar alerta de erro', () => {
        render(<AlertUI type="error" message="Erro ao salvar" />);

        expect(screen.getByRole('alert'))
            .toHaveClass('MuiAlert-standardError');
    });
});