import { Visibility, VisibilityOff } from '@mui/icons-material';
import { IconButton, InputAdornment } from '@mui/material';
import TextField from '@mui/material/TextField';
import { useTheme } from '@mui/material/styles';
import { useState } from 'react';

export default function InputUI({
    label,
    type = "text",
    value,
    onChange,
    placeholder,
    limit,
    showPasswordToggle = false,
    ...props
}) {
    const theme = useTheme();
    const isDark = theme.palette.mode === "dark";
    const [showPassword, setShowPassword] = useState(false);

    const isPassword = type === "password";
    const finalType =
        showPasswordToggle && isPassword
            ? (showPassword ? "text" : "password")
            : type;

    return (
        <TextField
            label={label}
            type={finalType}
            value={value}
            onChange={onChange}
            variant="outlined"
            fullWidth={props.fullWidth ?? true}
            margin="normal"
            size="small"
            placeholder={placeholder}
            {...props}
            sx={{
                ...props.sx,

                '& .MuiInputLabel-root': {
                    color: 'text.secondary',
                    transform: 'translate(14px, 9px) scale(1)',
                },

                '& .MuiInputLabel-root.Mui-focused, & .MuiInputLabel-root.MuiFormLabel-filled': {
                    color: 'primary.main',
                    transform: 'translate(10px, -18px) scale(0.85)',
                    backgroundColor: 'background.paper',
                    padding: '0 4px',
                },

                '& .MuiOutlinedInput-root': {
                    position: 'relative',
                    borderRadius: 2,
                    backgroundColor: isDark ? 'rgba(220, 252, 231, 0.06)' : 'rgba(22, 163, 74, 0.06)',
                    transition: 'all 0.2s ease',

                    '& .MuiOutlinedInput-input': {
                        whiteSpace: 'nowrap',
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
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
                },
            }}
            slotProps={{
                htmlInput: { maxLength: limit },
                input: {
                    ...props.slotProps?.input,
                    endAdornment:
                        showPasswordToggle && isPassword ? (
                            <InputAdornment position="end">
                                <IconButton
                                    onClick={() =>
                                        setShowPassword((prev) => !prev)
                                    }
                                    edge="end"
                                >
                                    {showPassword
                                        ? <VisibilityOff />
                                        : <Visibility />
                                    }
                                </IconButton>
                            </InputAdornment>
                        ) : props.slotProps?.input?.endAdornment || null
                }
            }}
        />
    );
}
