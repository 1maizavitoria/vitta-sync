import { useState } from "react";

import CheckboxUI from "./Checkbox";

function ControlledCheckbox(args) {
    const [checked, setChecked] = useState(args.checked);

    return (
        <CheckboxUI
            {...args}
            checked={checked}
            onChange={setChecked}
        />
    );
}

const meta = {
    title: "VittaSync/Checkbox",
    component: CheckboxUI,
    tags: ["autodocs"],
    render: (args) => <ControlledCheckbox {...args} />,
    args: {
        label: "Receber alertas",
        checked: false
    }
};

export default meta;

export const Desmarcado = {};

export const Marcado = {
    args: {
        checked: true
    }
};

export const Desabilitado = {
    args: {
        disabled: true
    }
};
