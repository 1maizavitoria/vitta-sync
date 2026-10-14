import { describe, it, expect } from 'vitest';
import { classifyApiError } from './classifyApiError';

describe('classifyApiError', () => {
    it('deve classificar erro sem response como erro de rede', () => {
        expect(classifyApiError({})).toEqual({
            type: 'network',
            status: null,
            retryable: true
        });
    });

    it('deve classificar status conhecidos', () => {
        expect(classifyApiError({ response: { status: 400 } })).toMatchObject({
            type: 'invalidRequest',
            retryable: false
        });
        expect(classifyApiError({ response: { status: 401 } })).toMatchObject({
            type: 'unauthorized',
            retryable: false
        });
        expect(classifyApiError({ response: { status: 403 } })).toMatchObject({
            type: 'forbidden',
            retryable: false
        });
        expect(classifyApiError({ response: { status: 404 } })).toMatchObject({
            type: 'notFound',
            retryable: false
        });
        expect(classifyApiError({ response: { status: 409 } })).toMatchObject({
            type: 'conflict',
            retryable: false
        });
    });

    it('deve classificar status 500 como erro de servidor retentável', () => {
        expect(classifyApiError({ response: { status: 500 } })).toEqual({
            type: 'server',
            status: 500,
            retryable: true
        });
    });

    it('deve classificar status desconhecido', () => {
        expect(classifyApiError({ response: { status: 418 } })).toEqual({
            type: 'unknown',
            status: 418,
            retryable: false
        });
    });
});
