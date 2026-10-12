import { useI18n } from "../../src/i18n";
import ClinicalStability from "./ClinicalStability";

const factors = [
    { tipo: "fc_bpm", indice: 8.4, categoria: "saudavel", peso: 1 },
    { tipo: "pressao", indice: 6.1, categoria: "moderado", peso: 1.5 },
    { tipo: "spo2", indice: 9.2, categoria: "saudavel", peso: 1 }
];

function StabilityExample(args) {
    const { t, formatNumber } = useI18n();

    return <ClinicalStability {...args} t={t} formatNumber={formatNumber} />;
}

const meta = {
    title: "VittaSync/ClinicalStability",
    component: ClinicalStability,
    tags: ["autodocs"],
    render: (args) => <StabilityExample {...args} />,
    args: {
        period: 30,
        items: [
            { tipo: "geral", indice: 8.1, categoria: "saudavel" },
            ...factors
        ]
    }
};

export default meta;

export const Saudavel = {};

export const Moderada = {
    args: {
        items: [
            { tipo: "geral", indice: 5.8, categoria: "moderado" },
            ...factors
        ]
    }
};

export const Critica = {
    args: {
        items: [
            { tipo: "geral", indice: 2.7, categoria: "critico" },
            { tipo: "fc_bpm", indice: 2.4, categoria: "critico", peso: 1 },
            { tipo: "pressao", indice: 3.1, categoria: "critico", peso: 1.5 },
            { tipo: "spo2", indice: 4.2, categoria: "moderado", peso: 1 }
        ]
    }
};

export const SemFatores = {
    args: {
        items: [
            { tipo: "geral", indice: null, categoria: "n/a" }
        ]
    }
};
