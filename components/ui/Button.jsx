import Button from '@mui/material/Button';

export default function ButtonUI({
    children,
    minWidth,
    sx,
    ...props
}) {
    return (
        <Button
            variant="contained"
            size="small"
            {...props}
            sx={{
                minWidth,

                borderRadius: '30px',
                padding: '5px 16px',

                fontWeight: 'bold',
                textTransform: 'uppercase',
                fontSize: '12px',

                background:
                    'linear-gradient(90deg, #c6eee6 0%, #b6d98e 100%)',

                border: '2px solid #bdbdbd',

                color: '#2b2b2b',

                boxShadow:
                    '0px 4px 8px rgba(0,0,0,0.15)',

                transition: '0.2s ease',

                '&:hover': {
                    background:
                        'linear-gradient(90deg, #d4f5ee 0%, #c6e7a7 100%)',

                    boxShadow:
                        '0px 6px 12px rgba(0,0,0,0.2)',
                },

                '&:active': {
                    boxShadow:
                        '0px 2px 4px rgba(0,0,0,0.2)',
                },

                ...sx,
            }}
        >
            {children}
        </Button>
    );
}