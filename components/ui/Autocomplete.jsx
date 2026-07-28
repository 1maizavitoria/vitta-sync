import { Autocomplete, TextField } from "@mui/material";
import { useTheme } from "@mui/material/styles";

export default function AutocompleteUI({
    label,
    value,
    onChange,
    error = false,
    options = [],
    placeholder = "Selecione...",
    getOptionLabel = (option) => option.label || "",
    isOptionEqualToValue = (option, value) => option.value === value.value,
    ...props
}) {
    const theme = useTheme();
    const isDark = theme.palette.mode === "dark";

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
                    variant="outlined"
                    error={error}
                    size="small"
                    margin="normal"
                    sx={{
                        '& .MuiInputLabel-root': {
                            transform: 'translate(14px, 9px) scale(1)',
                            color: 'text.secondary',
                        },

                        '& .MuiInputLabel-root.Mui-focused, & .MuiInputLabel-root.MuiFormLabel-filled': {
                            transform: 'translate(10px, -18px) scale(0.85)',
                            backgroundColor: 'background.paper',
                            color: 'primary.main',
                            padding: '0 4px',
                        },

                        '& .MuiOutlinedInput-root': {
                            position: 'relative',
                            borderRadius: 2,
                            backgroundColor: isDark ? 'rgba(220, 252, 231, 0.06)' : 'rgba(22, 163, 74, 0.06)',
                            transition: 'all 0.2s ease',

                            '& input': {
                                padding: '10px 14px',
                            },

                            '& fieldset': {
                                borderColor: theme.vitta.border,
                            },

                            '&.Mui-focused': {
                                backgroundColor: 'background.paper',
                                boxShadow: isDark ? '0 0 0 4px rgba(34, 197, 94, 0.12)' : '0 0 0 4px rgba(22, 163, 74, 0.12)',

                                '& fieldset': {
                                    borderColor: 'primary.main',
                                }
                            },

                            '&:hover': {
                                backgroundColor: 'background.paper',

                                '& fieldset': {
                                    borderColor: 'primary.main',
                                }
                            }
                        }
                    }}
                />
            )}
            {...props}
        />
    );
}
