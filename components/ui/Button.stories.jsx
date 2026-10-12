import ButtonUI from "./Button";

const meta = {
    title: "VittaSync/Button",
    component: ButtonUI,
    tags: ["autodocs"],
    args: {
        children: "Salvar",
        minWidth: "160px"
    },
    argTypes: {
        children: { control: "text" },
        minWidth: { control: "text" }
    }
};

export default meta;

export const Padrao = {};

export const Desabilitado = {
    args: {
        disabled: true
    }
};

export const LarguraAutomatica = {
    args: {
        minWidth: undefined,
        children: "Continuar"
    }
};
