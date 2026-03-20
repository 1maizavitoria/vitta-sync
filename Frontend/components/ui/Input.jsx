import TextField from '@mui/material/TextField';

export default function Input({
    label,
    type = "text",
    value,
    onChange,
    placeholder,
    ...props
}) {
    return (
        <TextField
            label={label}
            type={type}
            value={value}
            onChange={onChange}
            variant="standard"
            fullWidth
            margin="normal"
            placeholder={placeholder}
            {...props}
        />
    );
}