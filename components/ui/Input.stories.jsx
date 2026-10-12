import { useState } from "react";

import InputUI from "./Input";

function ControlledInput(args) {
    const [value, setValue] = useState(args.value || "");

    return (
        <InputUI
            {...args}
            value={value}
            onChange={(event) => setValue(event.target.value)}
        />
    );
}

const meta = {
    title: "VittaSync/Input",
    component: InputUI,
    tags: ["autodocs"],
    render: (args) => <ControlledInput {...args} />,
    args: {
        label: "Email",
        placeholder: "Ex.: maria@email.com",
        value: ""
    }
};

export default meta;

export const Vazio = {};

export const Preenchido = {
    args: {
        value: "maria@email.com"
    }
};

export const ComErro = {
    args: {
        error: true,
        helperText: "Informe um email válido"
    }
};

export const Senha = {
    args: {
        label: "Senha",
        placeholder: "Digite sua senha",
        type: "password",
        showPasswordToggle: true,
        value: "Senha@123"
    }
};
