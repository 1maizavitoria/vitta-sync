import {
    Dialog as MuiDialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button
} from "@mui/material";

export default function DialogUI({
    open,
    onClose,
    title,
    children,
    onConfirm,
    confirmText = "Confirmar",
    cancelText = "Cancelar"
}) {
    return (
        <MuiDialog open={open} onClose={onClose}>
            {title && <DialogTitle>{title}</DialogTitle>}

            <DialogContent>
                {children}
            </DialogContent>

            <DialogActions>
                <Button onClick={onClose}>
                    {cancelText}
                </Button>

                {onConfirm && (
                    <Button onClick={onConfirm} variant="contained">
                        {confirmText}
                    </Button>
                )}
            </DialogActions>
        </MuiDialog>
    );
}