import {
    Box,
    CircularProgress,
    Typography
} from "@mui/material";

import { useEffect, useRef }
    from "react";

import {
    useNavigate,
    useSearchParams
} from "react-router-dom";

import {
    joinWithCode
} from "../../services/linkService";

import {
    useAlert
} from "../../hooks/useAlert";

export default function JoinLink() {
    const hasExecuted =
        useRef(false);

    const navigate =
        useNavigate();

    const [searchParams] =
        useSearchParams();

    const { showAlert } =
        useAlert();

    useEffect(() => {

        if (hasExecuted.current) {
            return;
        }

        hasExecuted.current = true;

        async function handleJoin() {

            const code =
                searchParams.get("codigo");

            if (!code) {

                showAlert(
                    "error",
                    "Código inválido"
                );

                navigate("/");

                return;
            }

            try {

                await joinWithCode(code);

                showAlert(
                    "success",
                    "Vínculo criado com sucesso"
                );

                navigate("/links");

            } catch (error) {

                console.error(error);

                showAlert(
                    "error",
                    "Erro ao criar vínculo"
                );

                navigate("/");
            }
        }

        handleJoin();

    }, []);

    return (

        <Box
            display="flex"
            flexDirection="column"
            justifyContent="center"
            alignItems="center"
            minHeight="100vh"
            gap={3}
        >

            <CircularProgress />

            <Typography>
                Criando vínculo...
            </Typography>

        </Box>
    );
}