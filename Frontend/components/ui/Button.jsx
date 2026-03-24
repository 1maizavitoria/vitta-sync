import Button from '@mui/material/Button';

export default function ButtonUI({ children, ...props }) {
    return (
        <Button variant="contained" {...props}>
            {children}
        </Button>
    );
}