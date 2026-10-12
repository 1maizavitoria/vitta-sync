import { useState } from "react";

import AutocompleteUI from "./Autocomplete";

const options = [
    { value: "paciente", label: "Paciente" },
    { value: "responsavel", label: "Responsável" },
    { value: "saude", label: "Profissional de saúde" }
];

function ControlledAutocomplete(args) {
    const [value, setValue] = useState(args.value || null);

    return <AutocompleteUI {...args} value={value} onChange={setValue} />;
}

const meta = {
    title: "VittaSync/Autocomplete",
    component: AutocompleteUI,
    tags: ["autodocs"],
    render: (args) => <ControlledAutocomplete {...args} />,
    args: {
        label: "Tipo de usuário",
        placeholder: "Selecione uma opção",
        options,
        value: null
    }
};

export default meta;

export const Vazio = {};

export const Selecionado = {
    args: { value: options[0] }
};

export const ComErro = {
    args: { error: true }
};

export const Desabilitado = {
    args: {
        disabled: true,
        value: options[2]
    }
};
