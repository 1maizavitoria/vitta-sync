import { describe, it, expect } from 'vitest';
import {
    formatNumber,
    formatPercent,
    getDisplayUnit,
    convertMeasurementValue,
    convertMeasurementToMetricValue,
    formatMeasurement
} from './formatNumber';

describe('formatNumber', () => {
    it('deve retornar vazio para valor nulo ou vazio', () => {
        expect(formatNumber(null)).toBe('');
        expect(formatNumber(undefined)).toBe('');
        expect(formatNumber('')).toBe('');
    });

    it('deve retornar string original quando valor não for número', () => {
        expect(formatNumber('abc')).toBe('abc');
    });

    it('deve formatar número', () => {
        expect(formatNumber(1234.5, 'pt-BR')).toBe('1.234,5');
    });
});

describe('formatPercent', () => {
    it('deve formatar percentual sem casas decimais por padrão', () => {
        expect(formatPercent(12.7, 'pt-BR')).toBe('13');
    });
});

describe('getDisplayUnit', () => {
    it('deve manter unidade no sistema métrico', () => {
        expect(getDisplayUnit('kg')).toBe('kg');
    });

    it('deve converter unidade para sistema imperial', () => {
        expect(getDisplayUnit('kg', 'imperial')).toBe('lb');
        expect(getDisplayUnit('°C', 'imperial')).toBe('°F');
        expect(getDisplayUnit('m', 'imperial')).toBe('in');
        expect(getDisplayUnit('cm', 'imperial')).toBe('in');
    });

    it('deve manter unidade desconhecida no sistema imperial', () => {
        expect(getDisplayUnit('bpm', 'imperial')).toBe('bpm');
    });
});

describe('convertMeasurementValue', () => {
    it('deve manter valor quando não for número ou sistema não for imperial', () => {
        expect(convertMeasurementValue('abc', 'kg', 'imperial')).toBe('abc');
        expect(convertMeasurementValue(10, 'kg')).toBe(10);
    });

    it('deve converter medidas para imperial', () => {
        expect(convertMeasurementValue(1, 'kg', 'imperial')).toBeCloseTo(2.2046);
        expect(convertMeasurementValue(0, '°C', 'imperial')).toBeCloseTo(32);
        expect(convertMeasurementValue(1, 'm', 'imperial')).toBeCloseTo(39.3701);
        expect(convertMeasurementValue(2.54, 'cm', 'imperial')).toBeCloseTo(1);
    });

    it('deve manter unidade desconhecida', () => {
        expect(convertMeasurementValue(80, 'bpm', 'imperial')).toBe(80);
    });
});

describe('convertMeasurementToMetricValue', () => {
    it('deve manter valor quando não for número ou sistema não for imperial', () => {
        expect(convertMeasurementToMetricValue('abc', 'kg', 'imperial')).toBe('abc');
        expect(convertMeasurementToMetricValue(10, 'kg')).toBe(10);
    });

    it('deve converter medidas para métrico', () => {
        expect(convertMeasurementToMetricValue(2.2046226218, 'kg', 'imperial')).toBeCloseTo(1);
        expect(convertMeasurementToMetricValue(32, '°F', 'imperial')).toBe(32);
        expect(convertMeasurementToMetricValue(32, '°C', 'imperial')).toBeCloseTo(0);
        expect(convertMeasurementToMetricValue(39.37007874, 'm', 'imperial')).toBeCloseTo(1);
        expect(convertMeasurementToMetricValue(1, 'cm', 'imperial')).toBeCloseTo(2.54);
    });

    it('deve manter unidade desconhecida', () => {
        expect(convertMeasurementToMetricValue(80, 'bpm', 'imperial')).toBe(80);
    });
});

describe('formatMeasurement', () => {
    it('deve formatar medida com unidade', () => {
        expect(formatMeasurement(36.55, '°C', 'pt-BR')).toBe('36,6 °C');
    });

    it('deve formatar medida sem unidade', () => {
        expect(formatMeasurement(36.55, '', 'pt-BR')).toBe('36,6');
    });

    it('deve formatar medida convertida para imperial', () => {
        expect(formatMeasurement(1, 'kg', 'pt-BR', { maximumFractionDigits: 2 }, 'imperial'))
            .toBe('2,2 lb');
    });
});
