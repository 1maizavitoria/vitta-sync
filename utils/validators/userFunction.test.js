import { describe, it, expect } from 'vitest';
import {
    funcoesMedico,
    funcoesGrupo,
    getNomeFuncao,
    getResponsavelStyle,
    getMedicoStyle
} from './userFunction';

describe('funcoesMedico', () => {
    it('deve conter as funções de médico', () => {
        expect(funcoesMedico).toHaveLength(5);
        expect(funcoesMedico[0]).toMatchObject({
            label: 'Médico Principal',
            value: 'medico_principal'
        });
    });
});

describe('funcoesGrupo', () => {
    it('deve conter as funções de grupo', () => {
        expect(funcoesGrupo).toHaveLength(5);
        expect(funcoesGrupo[0]).toMatchObject({
            label: 'Cuidador',
            value: 'cuidador'
        });
    });
});

describe('getNomeFuncao', () => {
    it('deve retornar Paciente quando a função for paciente', () => {
        expect(getNomeFuncao('paciente')).toBe('Paciente');
    });

    it('deve retornar o label de uma função do grupo', () => {
        expect(getNomeFuncao('responsavel_legal')).toBe('Responsável Legal');
    });

    it('deve retornar o label de uma função médica', () => {
        expect(getNomeFuncao('especialista')).toBe('Especialista');
    });

    it('deve retornar o próprio valor quando a função não for encontrada', () => {
        expect(getNomeFuncao('funcao_desconhecida')).toBe('funcao_desconhecida');
    });
});

describe('getResponsavelStyle', () => {
    it('deve retornar estilo de cuidador', () => {
        expect(getResponsavelStyle('cuidador')).toMatchObject({
            background: '#fff3e0',
            color: '#e65100',
            label: 'Cuidador'
        });
    });

    it('deve retornar estilo padrão para função desconhecida', () => {
        expect(getResponsavelStyle('outra_funcao')).toMatchObject({
            background: '#eeeeee',
            color: '#616161',
            label: 'Responsável'
        });
    });

    it('deve retornar estilo de responsável legal', () => {
        expect(getResponsavelStyle('responsavel_legal')).toMatchObject({
            background: '#fce4ec',
            color: '#c2185b',
            label: 'Responsável Legal'
        });
    });

    it('deve retornar estilo de acompanhante', () => {
        expect(getResponsavelStyle('acompanhante')).toMatchObject({
            background: '#ede7f6',
            color: '#5e35b1',
            label: 'Acompanhante'
        });
    });

    it('deve retornar estilo de contato de emergência', () => {
        expect(getResponsavelStyle('contato_emergencia')).toMatchObject({
            background: '#ffebee',
            color: '#c62828',
            label: 'Contato de Emergência'
        });
    });

    it('deve retornar estilo de tutor', () => {
        expect(getResponsavelStyle('tutor')).toMatchObject({
            background: '#e0f7fa',
            color: '#00838f',
            label: 'Tutor'
        });
    });
});

describe('getMedicoStyle', () => {
    it('deve retornar estilo de médico principal', () => {
        expect(getMedicoStyle('medico_principal')).toMatchObject({
            background: '#e3f2fd',
            color: '#1565c0',
            label: 'Médico Principal'
        });
    });

    it('deve retornar estilo padrão para função desconhecida', () => {
        expect(getMedicoStyle('outra_funcao')).toMatchObject({
            background: '#eeeeee',
            color: '#616161',
            label: 'Profissional de Saúde'
        });
    });

    it('deve retornar estilo de especialista', () => {
        expect(getMedicoStyle('especialista')).toMatchObject({
            background: '#e8f5e9',
            color: '#2e7d32',
            label: 'Especialista'
        });
    });

    it('deve retornar estilo de consultor', () => {
        expect(getMedicoStyle('consultor')).toMatchObject({
            background: '#fff8e1',
            color: '#f9a825',
            label: 'Consultor'
        });
    });

    it('deve retornar estilo de acompanhamento clínico', () => {
        expect(getMedicoStyle('acompanhamento_clinico')).toMatchObject({
            background: '#e0f2f1',
            color: '#00695c',
            label: 'Acompanhamento Clínico'
        });
    });

    it('deve retornar estilo de equipe assistencial', () => {
        expect(getMedicoStyle('equipe_assistencial')).toMatchObject({
            background: '#ede7f6',
            color: '#4527a0',
            label: 'Equipe Assistencial'
        });
    });
});