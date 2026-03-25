import { Visibility, VisibilityOff } from '@mui/icons-material';
import { IconButton, InputAdornment } from '@mui/material';
import TextField from '@mui/material/TextField';
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
            variant="standard"
            fullWidth
            margin="normal"
            placeholder={placeholder}
            {...props}

            slotProps={{
                htmlInput: { maxLength: limit },
                input: {
                    ...props.slotProps?.input,
                    endAdornment:
                        showPasswordToggle && isPassword ? (
                            <InputAdornment position="end">
                                <IconButton
                                    onClick={() => setShowPassword((prev) => !prev)}
                                    edge="end"
                                >
                                    {showPassword ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        ) : null

                }
            }}
        />
    );
}