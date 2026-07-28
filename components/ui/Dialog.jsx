import {
    Dialog as MuiDialog,
    DialogTitle,
    DialogContent,
    DialogActions
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
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
    const theme = useTheme();

    return (
        <MuiDialog
            open={open}
            onClose={disabledClose ? undefined : onClose}
            PaperProps={{
                sx: {
                    borderRadius: 3,
                    bgcolor: "background.paper",
                    color: "text.primary",
                    border: "1px solid",
                    borderColor: theme.vitta.border,
                    boxShadow: theme.vitta.shadow,
                    backgroundImage: "none"
                }
            }}
        >
            {title && <DialogTitle sx={{ fontWeight: 800 }}>{title}</DialogTitle>}

            <DialogContent sx={{ color: "text.primary" }}>
                {children}
            </DialogContent>

            <DialogActions sx={{ px: 3, pb: 2.5 }}>
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
