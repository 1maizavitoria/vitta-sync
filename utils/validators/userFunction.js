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

export function getNomeFuncao(funcao) {

    if (funcao === "paciente") {
        return "Paciente";
    }

    const todasFuncoes = [
        ...funcoesGrupo,
        ...funcoesMedico
    ];

    const encontrada =
        todasFuncoes.find(
            item => item.value === funcao
        );

    return encontrada?.label || funcao;
}

export function getResponsavelStyle(funcao) {

    switch (funcao?.toLowerCase()) {

        case "cuidador":
            return {
                background: "#fff3e0",
                color: "#e65100",
                label: "Cuidador"
            };

        case "responsavel_legal":
            return {
                background: "#fce4ec",
                color: "#c2185b",
                label: "Responsável Legal"
            };

        case "acompanhante":
            return {
                background: "#ede7f6",
                color: "#5e35b1",
                label: "Acompanhante"
            };

        case "contato_emergencia":
            return {
                background: "#ffebee",
                color: "#c62828",
                label: "Contato de Emergência"
            };

        case "tutor":
            return {
                background: "#e0f7fa",
                color: "#00838f",
                label: "Tutor"
            };

        default:
            return {
                background: "#eeeeee",
                color: "#616161",
                label: "Responsável"
            };
    }
}

export function getMedicoStyle(funcao) {

    switch (funcao?.toLowerCase()) {

        case "medico_principal":
            return {
                background: "#e3f2fd",
                color: "#1565c0",
                label: "Médico Principal"
            };

        case "especialista":
            return {
                background: "#e8f5e9",
                color: "#2e7d32",
                label: "Especialista"
            };

        case "consultor":
            return {
                background: "#fff8e1",
                color: "#f9a825",
                label: "Consultor"
            };

        case "acompanhamento_clinico":
            return {
                background: "#e0f2f1",
                color: "#00695c",
                label: "Acompanhamento Clínico"
            };

        case "equipe_assistencial":
            return {
                background: "#ede7f6",
                color: "#4527a0",
                label: "Equipe Assistencial"
            };

        default:
            return {
                background: "#eeeeee",
                color: "#616161",
                label: "Profissional de Saúde"
            };
    }
}