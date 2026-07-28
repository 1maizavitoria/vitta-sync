export const funcoesMedico = [
    {
        label: "Médico Principal",
        value: "medico_principal",
        descricao: "Responsável principal pelo acompanhamento do paciente."
    },
    {
        label: "Especialista",
        value: "especialista",
        descricao: "Profissional consultado para uma área específica."
    },
    {
        label: "Consultor",
        value: "consultor",
        descricao: "Participa com orientações e pareceres ocasionais."
    },
    {
        label: "Acompanhamento Clínico",
        value: "acompanhamento_clinico",
        descricao: "Auxilia no monitoramento contínuo da saúde."
    },
    {
        label: "Equipe Assistencial",
        value: "equipe_assistencial",
        descricao: "Participa do cuidado multidisciplinar do paciente."
    }
];

export const funcoesGrupo = [
    {
        label: "Cuidador",
        value: "cuidador",
        descricao: "Pessoa que acompanha cuidados diários do paciente."
    },
    {
        label: "Responsável Legal",
        value: "responsavel_legal",
        descricao: "Pai, mãe, tutor ou curador legal do paciente."
    },
    {
        label: "Acompanhante",
        value: "acompanhante",
        descricao: "Pessoa que acompanha consultas e exames."
    },
    {
        label: "Contato de Emergência",
        value: "contato_emergencia",
        descricao: "Pessoa acionada em situações de urgência."
    },
    {
        label: "Tutor",
        value: "tutor",
        descricao: "Responsável por menores ou incapazes."
    }
];

function translateFunctionLabel(value, fallback, t) {
    return t ? t(`groupFunctions.labels.${value}`) : fallback;
}

function translateFunctionDescription(value, fallback, t) {
    return t ? t(`groupFunctions.descriptions.${value}`) : fallback;
}

export function translateFunctionOptions(options, t) {
    return options.map((option) => ({
        ...option,
        label: translateFunctionLabel(option.value, option.label, t),
        descricao: translateFunctionDescription(option.value, option.descricao, t)
    }));
}

export function getNomeFuncao(funcao, t) {
    if (funcao === "paciente") {
        return t ? t("userTypes.paciente") : "Paciente";
    }

    const todasFuncoes = [
        ...funcoesGrupo,
        ...funcoesMedico
    ];

    const encontrada =
        todasFuncoes.find(
            item => item.value === funcao
        );

    return encontrada
        ? translateFunctionLabel(encontrada.value, encontrada.label, t)
        : funcao;
}

export function getResponsavelStyle(funcao, t) {
    switch (funcao?.toLowerCase()) {
        case "cuidador":
            return {
                background: "#fff3e0",
                color: "#e65100",
                label: translateFunctionLabel("cuidador", "Cuidador", t)
            };

        case "responsavel_legal":
            return {
                background: "#fce4ec",
                color: "#c2185b",
                label: translateFunctionLabel("responsavel_legal", "Responsável Legal", t)
            };

        case "acompanhante":
            return {
                background: "#ede7f6",
                color: "#5e35b1",
                label: translateFunctionLabel("acompanhante", "Acompanhante", t)
            };

        case "contato_emergencia":
            return {
                background: "#ffebee",
                color: "#c62828",
                label: translateFunctionLabel("contato_emergencia", "Contato de Emergência", t)
            };

        case "tutor":
            return {
                background: "#e0f7fa",
                color: "#00838f",
                label: translateFunctionLabel("tutor", "Tutor", t)
            };

        default:
            return {
                background: "#eeeeee",
                color: "#616161",
                label: translateFunctionLabel("responsavel", "Responsável", t)
            };
    }
}

export function getMedicoStyle(funcao, t) {
    switch (funcao?.toLowerCase()) {
        case "medico_principal":
            return {
                background: "#e3f2fd",
                color: "#1565c0",
                label: translateFunctionLabel("medico_principal", "Médico Principal", t)
            };

        case "especialista":
            return {
                background: "#e8f5e9",
                color: "#2e7d32",
                label: translateFunctionLabel("especialista", "Especialista", t)
            };

        case "consultor":
            return {
                background: "#fff8e1",
                color: "#f9a825",
                label: translateFunctionLabel("consultor", "Consultor", t)
            };

        case "acompanhamento_clinico":
            return {
                background: "#e0f2f1",
                color: "#00695c",
                label: translateFunctionLabel("acompanhamento_clinico", "Acompanhamento Clínico", t)
            };

        case "equipe_assistencial":
            return {
                background: "#ede7f6",
                color: "#4527a0",
                label: translateFunctionLabel("equipe_assistencial", "Equipe Assistencial", t)
            };

        default:
            return {
                background: "#eeeeee",
                color: "#616161",
                label: translateFunctionLabel("profissional_saude", "Profissional de Saúde", t)
            };
    }
}
