import Button from '@mui/material/Button';
import { useTheme } from '@mui/material/styles';

export default function ButtonUI({
    children,
    minWidth,
    sx,
    ...props
}) {
    const theme = useTheme();
    const isDark = theme.palette.mode === "dark";

    return (
        <Button
            variant="contained"
            size="medium"
            {...props}
            sx={{
                minWidth,
                borderRadius: 2,
                padding: '9px 18px',
                fontWeight: 700,
                textTransform: 'uppercase',
                fontSize: '12px',
                letterSpacing: '0.04em',
                background: 'linear-gradient(135deg, #16a34a 0%, #0f766e 72%, #0ea5e9 100%)',
                border: `1px solid ${theme.vitta.borderStrong}`,
                color: '#ffffff',
                boxShadow: isDark ? '0 12px 24px rgba(0, 0, 0, 0.24)' : '0 12px 24px rgba(22, 163, 74, 0.2)',
                transition: 'background .18s ease, border-color .18s ease, color .18s ease, box-shadow .18s ease, transform .18s ease',

                '&:hover': {
                    background: 'linear-gradient(135deg, #15803d 0%, #115e59 72%, #0369a1 100%)',
                    boxShadow: isDark ? '0 14px 28px rgba(0, 0, 0, 0.32)' : '0 14px 28px rgba(22, 163, 74, 0.28)',
                    transform: 'translateY(-1px)',
                },

                '&:active': {
                    boxShadow: isDark ? '0 8px 18px rgba(0, 0, 0, 0.28)' : '0 8px 18px rgba(22, 163, 74, 0.22)',
                    transform: 'translateY(0)',
                },

                '&.Mui-disabled': {
                    background: isDark ? 'rgba(220, 252, 231, 0.08)' : '#d7e4e1',
                    color: 'text.secondary',
                    boxShadow: 'none'
                },

                ...sx,
            }}
        >
            {children}
        </Button>
    );
}
