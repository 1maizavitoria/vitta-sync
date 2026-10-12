import PersonalBaseline from "./PersonalBaseline";

const establishedBaseline = {
    sinal: "frequencia_cardiaca",
    situacao: "estabelecida",
    media: 72,
    limiteInferior: 62,
    limiteSuperior: 82,
    ultimoValor: 74,
    comparacao: "dentro",
    dataInicio: "2026-09-12",
    dataFim: "2026-10-12",
    dataUltimoValor: "2026-10-12T08:30:00"
};

const meta = {
    title: "VittaSync/PersonalBaseline",
    component: PersonalBaseline,
    tags: ["autodocs"],
    args: {
        items: [establishedBaseline],
        categoryFilter: "todas"
    }
};

export default meta;

export const Estabelecida = {};

export const EmFormacao = {
    args: {
        items: [
            {
                sinal: "peso",
                situacao: "em_formacao",
                diasRegistrados: 8,
                diasNecessarios: 14,
                ultimoValor: 78.4,
                dataUltimoValor: "2026-10-12T07:45:00"
            }
        ]
    }
};

export const ForaDoIntervalo = {
    args: {
        items: [
            {
                ...establishedBaseline,
                ultimoValor: 96,
                comparacao: "acima"
            }
        ]
    }
};

export const Indisponivel = {
    args: { items: [] }
};
