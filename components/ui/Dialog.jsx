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
    disabledClose = false,
    title,
    children,
    onConfirm,
    disabledConfirm = false,
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
                {!disabledClose && <ButtonUI onClick={onClose}>
                    {cancelText}
                </ButtonUI>}

                {onConfirm && !disabledConfirm && (
                    <ButtonUI onClick={onConfirm} variant="contained">
                        {confirmText}
                    </ButtonUI>
                )}
            </DialogActions>
        </MuiDialog>
    );
}