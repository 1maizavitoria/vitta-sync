import { Autocomplete, TextField } from "@mui/material";

export default function AutocompleteUI({
    label,
    value,
    onChange,
    error = "false",
    //helperText,
    options = [],
    placeholder = "Selecione...",
    getOptionLabel = (option) => option.label || "",
    isOptionEqualToValue = (option, value) => option.value === value.value,
    ...props
}) {
    return (
        <Autocomplete
            options={options}
            value={value}
            onChange={(event, newValue) => onChange(newValue)}
            getOptionLabel={getOptionLabel}
            isOptionEqualToValue={isOptionEqualToValue}
            fullWidth
            renderInput={(params) => (
                <TextField
                    {...params}
                    label={label}
                    placeholder={placeholder}
                    variant="standard"
                    error={error}
                //helperText={error ? helperText : ""}
                />
            )}
            {...props}
        />
    );
}