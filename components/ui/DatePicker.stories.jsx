import dayjs from "dayjs";
import { useState } from "react";

import DatePickerUI from "./DatePicker";

function ControlledDatePicker(args) {
    const [value, setValue] = useState(args.value || null);

    return <DatePickerUI {...args} value={value} onChange={setValue} />;
}

const meta = {
    title: "VittaSync/DatePicker",
    component: DatePickerUI,
    tags: ["autodocs"],
    render: (args) => <ControlledDatePicker {...args} />,
    args: {
        label: "Data da medição",
        value: null
    }
};

export default meta;

export const Vazio = {};

export const Preenchido = {
    args: { value: "2026-10-12" }
};

export const ComErro = {
    args: { error: true }
};

export const DataMaxima = {
    args: {
        label: "Data de nascimento",
        dateLimit: dayjs().subtract(18, "year")
    }
};
