import Button from '@mui/material/Button';

export default function ButtonUI({ children, minWidth, ...props }) {
    return (
        <Button
            variant="contained"
            size="small"
            {...props}
            sx={{
                minWidth: { minWidth },
                borderRadius: '30px',
                padding: '5px 16px',
                fontWeight: 'bold',
                textTransform: 'uppercase',
                fontSize: '12px',

                // 🔵 Degradê (esquerda → direita)
                background: 'linear-gradient(to right, #00aeff, #00ff5e)',

                // 🩶 Borda cinza
                border: '3px solid #bdbdbd',

                // 🌫️ Sombra cinza
                boxShadow: '0px 4px 8px rgba(0,0,0,0.2)',

                // Evita que o MUI sobrescreva o background
                '&:hover': {
                    background: 'linear-gradient(to right, #6cc8ec, #75f4a0)',
                    boxShadow: '0px 6px 12px rgba(0,0,0,0.25)',
                },

                '&:active': {
                    boxShadow: '0px 2px 4px rgba(0,0,0,0.2)',
                }
            }}
        >
            {children}
        </Button>
    );
}