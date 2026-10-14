import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import { previewReport, downloadReport } from './reportService';

vi.mock('./api', () => ({
    default: {
        post: vi.fn()
    }
}));

describe('reportService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve gerar preview do relatório', async () => {
        const filters = { formato: 'PDF', inicio: '2026-01-01' };
        const signal = {};
        const response = { data: { resumo: [] } };

        api.post.mockResolvedValue(response);

        const result = await previewReport('123.456.789-01', filters, signal);

        expect(api.post).toHaveBeenCalledWith(
            '/relatorio/exportar/123.456.789-01',
            { ...filters, preview: true },
            { signal }
        );
        expect(result).toEqual(response.data);
    });

    it('deve baixar relatório usando nome do content-disposition', async () => {
        const blob = new Blob(['conteudo']);
        const filters = { formato: 'PDF' };
        const signal = {};

        api.post.mockResolvedValue({
            data: blob,
            headers: {
                'content-disposition': 'attachment; filename="relatorio-final.pdf"'
            }
        });

        const result = await downloadReport('12345678901', filters, signal);

        expect(api.post).toHaveBeenCalledWith(
            '/relatorio/exportar/12345678901',
            { ...filters, preview: false },
            { responseType: 'blob', signal }
        );
        expect(result).toEqual({
            blob,
            filename: 'relatorio-final.pdf'
        });
    });

    it('deve usar nome padrão quando content-disposition não vier preenchido', async () => {
        const blob = new Blob(['conteudo']);

        api.post.mockResolvedValue({
            data: blob,
            headers: {}
        });

        const result = await downloadReport('12345678901', { formato: 'CSV' });

        expect(result).toEqual({
            blob,
            filename: 'relatorio.csv'
        });
    });

    it('deve limpar barras do nome do arquivo', async () => {
        const blob = new Blob(['conteudo']);

        api.post.mockResolvedValue({
            data: blob,
            headers: {
                'content-disposition': 'attachment; filename="pasta\\relatorio/pdf.pdf"'
            }
        });

        const result = await downloadReport('12345678901', { formato: 'PDF' });

        expect(result.filename).toBe('pasta-relatorio-pdf.pdf');
    });

    it('deve converter erro em blob JSON antes de relançar', async () => {
        const error = {
            response: {
                data: new Blob([JSON.stringify({ message: 'Erro no relatório' })], {
                    type: 'application/json'
                })
            }
        };

        api.post.mockRejectedValue(error);

        await expect(downloadReport('12345678901', { formato: 'PDF' }))
            .rejects
            .toBe(error);
        expect(error.response.data).toEqual({ message: 'Erro no relatório' });
    });
});
