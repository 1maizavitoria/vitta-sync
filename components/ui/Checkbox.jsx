import { Checkbox, FormControlLabel } from "@mui/material";

export default function CheckboxUI({
    label,
    checked = false,
    onChange,
    ...props
}) {
    return (
        <FormControlLabel
            control={
                <Checkbox
                    checked={checked}
                    onChange={(e) => onChange(e.target.checked)}
                    {...props}
                />
            }
            label={label}
        />
    );
}