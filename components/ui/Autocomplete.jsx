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
                    variant="outlined"
                    error={error}
                    size="small"
                    margin="normal"
                    sx={{
                        '& .MuiInputLabel-root': {
                            transform: 'translate(14px, 9px) scale(1)',
                        },

                        '& .MuiInputLabel-root.Mui-focused, & .MuiInputLabel-root.MuiFormLabel-filled': {
                            transform: 'translate(10px, -20px) scale(0.85)',
                            padding: '0 4px',
                        },

                        '& .MuiOutlinedInput-root': {
                            position: 'relative',
                            borderRadius: '30px',
                            backgroundColor: '#e0e0e0',
                            transition: 'all 0.5s ease',

                            '& input': {
                                padding: '10px 14px',
                            },

                            '& fieldset': {
                                border: '1px solid transparent',
                            },

                            '&:hover::before, &.Mui-focused::before': {
                                content: '""',
                                position: 'absolute',
                                inset: 0,
                                borderRadius: '30px',
                                padding: '1.5px',
                                background: 'linear-gradient(to right, #00b7ff, #00ff55)',

                                WebkitMask:
                                    'linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0)',
                                WebkitMaskComposite: 'xor',
                                maskComposite: 'exclude',

                                pointerEvents: 'none',
                            },

                            '&.Mui-focused': {
                                backgroundColor: '#ffffff',
                                boxShadow: 'inset 0px 2px 10px rgba(0,0,0,0.2)',
                            },

                            '&:hover': {
                                backgroundColor: '#eeeeee',
                                boxShadow: '0px 5px 15px -2px #4e4e4e',
                            }
                        }
                    }
                    }
                />
            )
            }
            {...props}
        />
    );
}