import {
    Dialog as MuiDialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button
} from "@mui/material";
import ButtonUI from "./Button";

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
                <ButtonUI onClick={onClose}>
                    {cancelText}
                </ButtonUI>

                {onConfirm && (
                    <ButtonUI onClick={onConfirm} variant="contained">
                        {confirmText}
                    </ButtonUI>
                )}
            </DialogActions>
        </MuiDialog>
    );
}