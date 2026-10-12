import AlertUI from "./Alert";

const meta = {
    title: "VittaSync/Alert",
    component: AlertUI,
    tags: ["autodocs"],
    parameters: {
        layout: "fullscreen"
    },
    args: {
        type: "info",
        message: "Informação importante para o acompanhamento."
    },
    argTypes: {
        type: {
            control: "select",
            options: ["info", "success", "warning", "error"]
        },
        message: { control: "text" }
    }
};

export default meta;

export const Informacao = {};

export const Sucesso = {
    args: {
        type: "success",
        message: "Dados salvos com sucesso."
    }
};

export const Aviso = {
    args: {
        type: "warning",
        message: "Confira os dados antes de continuar."
    }
};

export const Erro = {
    args: {
        type: "error",
        message: "Não foi possível concluir a operação."
    }
};
