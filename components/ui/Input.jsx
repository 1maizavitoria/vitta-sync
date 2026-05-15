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
            variant="outlined"
            fullWidth={props.fullWidth ?? true}
            margin="normal"
            size="small"
            placeholder={placeholder}
            {...props}

            sx={{
                ...props.sx,

                '& .MuiInputLabel-root': {
                    transform: 'translate(14px, 9px) scale(1)', // posição inicial
                },

                '& .MuiInputLabel-root.Mui-focused, & .MuiInputLabel-root.MuiFormLabel-filled': {
                    transform: 'translate(10px, -20px) scale(0.85)', // quando sobe
                    // backgroundColor: '#fff', // evita sobrepor a borda
                    padding: '0 4px',
                },

                '& .MuiOutlinedInput-root': {


                    position: 'relative',

                    borderRadius: '30px',
                    backgroundColor: '#e0e0e0', // cinza padrão

                    transition: 'all 0.5s ease',

                    '& fieldset': {
                        border: '1px solid transparent',
                    },

                    // 🔵 borda gradiente (hover + focus)
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

                    // 🟢 foco / interação
                    '&.Mui-focused': {
                        backgroundColor: '#ffffff',

                        // 🔽 sombra interna (afundado)
                        boxShadow: 'inset 0px 2px 10px rgba(0,0,0,0.2)',

                        '& fieldset': {
                            border: '1px solid #bdbdbd',
                        }
                    },

                    // hover (opcional)
                    '&:hover': {
                        backgroundColor: '#eeeeee',
                        // 🔽 sombra interna (afundado)
                        boxShadow: '0px 5px 15px -2px #4e4e4e',
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