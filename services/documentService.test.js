import { describe, it, expect, vi, beforeEach } from 'vitest';
import api from './api';
import {
    getPatientDocuments,
    getDoctorDocuments,
    uploadDocument,
    deleteDocument,
    getViewDocumentUrl,
    viewDocument,
    downloadDocument
} from './documentService';



vi.mock('./api', () => ({
    default: {
        get: vi.fn(),
        post: vi.fn(),
        delete: vi.fn(),
        defaults: {
            baseURL: 'http://localhost:8080'
        }
    }
}));

describe('documentService', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('deve buscar documentos do paciente', async () => {
        const response = { data: [{ id: 1, nome: 'exame.pdf' }] };

        api.get.mockResolvedValue(response);

        const result = await getPatientDocuments('12345678901');

        expect(api.get).toHaveBeenCalledWith('/documentos/getDocumentosPaciente/12345678901');
        expect(result).toEqual(response.data);
    });

    it('deve buscar documentos do médico', async () => {
        const response = { data: [{ id: 1, nome: 'exame.pdf' }] };

        api.get.mockResolvedValue(response);

        const result = await getDoctorDocuments();

        expect(api.get).toHaveBeenCalledWith('/documentos/getDocumentosMedico');
        expect(result).toEqual(response.data);
    });

    it('deve enviar documento', async () => {
        const formData = new FormData();
        const response = { data: { id: 1 } };

        api.post.mockResolvedValue(response);

        const result = await uploadDocument('12345678901', formData);

        expect(api.post).toHaveBeenCalledWith(
            '/documentos/upload/12345678901',
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }
        );
        expect(result).toEqual(response.data);
    });

    it('deve deletar documento', async () => {
        api.delete.mockResolvedValue();

        const result = await deleteDocument(10);

        expect(api.delete).toHaveBeenCalledWith('/documentos/deletarDocumento/10');
        expect(result).toBeUndefined();
    });

    it('deve retornar URL de visualização do documento', () => {
        expect(getViewDocumentUrl(10)).toBe(
            'http://localhost:8080/documentos/10/visualizarDocumento'
        );
    });

    it('deve criar URL para visualizar documento', async () => {
        const createObjectURL = vi
            .spyOn(URL, 'createObjectURL')
            .mockReturnValue('blob:http://localhost/documento');

        api.get.mockResolvedValue({
            data: new Blob(['conteudo'])
        });

        const result = await viewDocument(10, '.pdf');

        expect(api.get).toHaveBeenCalledWith(
            '/documentos/10/visualizarDocumento',
            { responseType: 'blob' }
        );
        expect(createObjectURL).toHaveBeenCalled();
        expect(result).toBe('blob:http://localhost/documento');

        createObjectURL.mockRestore();
    });

    it('deve usar tipo padrão ao visualizar documento com extensão desconhecida', async () => {
        const createObjectURL = vi
            .spyOn(URL, 'createObjectURL')
            .mockReturnValue('blob:http://localhost/documento');

        api.get.mockResolvedValue({
            data: new Blob(['conteudo'])
        });

        const result = await viewDocument(10, '.docx');

        expect(api.get).toHaveBeenCalledWith(
            '/documentos/10/visualizarDocumento',
            { responseType: 'blob' }
        );
        expect(createObjectURL).toHaveBeenCalled();
        expect(result).toBe('blob:http://localhost/documento');

        createObjectURL.mockRestore();
    });

    it('deve baixar documento', async () => {
        const click = vi.fn();

        vi.spyOn(URL, 'createObjectURL')
            .mockReturnValue('blob:http://localhost/download');

        vi.spyOn(URL, 'revokeObjectURL')
            .mockImplementation(() => { });

        vi.spyOn(document, 'createElement')
            .mockReturnValue({
                href: '',
                download: '',
                click
            });

        api.get.mockResolvedValue({
            data: new Blob(['conteudo'])
        });

        await downloadDocument(10, 'exame.pdf');

        expect(api.get).toHaveBeenCalledWith(
            '/documentos/10/downloadDocumento',
            { responseType: 'blob' }
        );
        expect(click).toHaveBeenCalledTimes(1);
        expect(URL.revokeObjectURL).toHaveBeenCalledWith(
            'blob:http://localhost/download'
        );

        URL.createObjectURL.mockRestore();
        URL.revokeObjectURL.mockRestore();
        document.createElement.mockRestore();
    });
});