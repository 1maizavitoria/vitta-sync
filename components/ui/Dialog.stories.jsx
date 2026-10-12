import { Box, Typography } from "@mui/material";
import { useState } from "react";

import ButtonUI from "./Button";
import DialogUI from "./Dialog";

function DialogExample(args) {
    const [open, setOpen] = useState(true);

    return (
        <Box sx={{ p: 3 }}>
            <ButtonUI onClick={() => setOpen(true)}>Abrir diálogo</ButtonUI>
            <DialogUI
                {...args}
                open={open}
                onClose={() => setOpen(false)}
                onConfirm={() => setOpen(false)}
            />
        </Box>
    );
}

const meta = {
    title: "VittaSync/Dialog",
    component: DialogUI,
    tags: ["autodocs"],
    render: (args) => <DialogExample {...args} />,
    args: {
        title: "Confirmar ação",
        confirmText: "Confirmar",
        cancelText: "Cancelar",
        children: <Typography>Deseja continuar com esta ação?</Typography>
    }
};

export default meta;

export const Confirmacao = {};

export const Exclusao = {
    args: {
        title: "Excluir registro",
        confirmText: "Excluir",
        children: <Typography>Esta ação não poderá ser desfeita.</Typography>
    }
};

export const FechamentoObrigatorio = {
    args: {
        disabledClose: true,
        title: "Atenção necessária",
        children: <Typography>Confirme a leitura desta informação para continuar.</Typography>
    }
};

export const ConteudoLongo = {
    args: {
        title: "Orientações",
        children: (
            <Box>
                <Typography paragraph>Confira os dados informados antes de salvar.</Typography>
                <Typography paragraph>Os registros serão utilizados no acompanhamento do paciente.</Typography>
                <Typography>Em caso de dúvida, procure um profissional de saúde.</Typography>
            </Box>
        )
    }
};
