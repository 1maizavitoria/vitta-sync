import {
    Box,
    CircularProgress,
    Typography
} from "@mui/material";

import { useEffect, useRef, useState } from "react";

import AutocompleteUI from "../../components/ui/Autocomplete";
import ButtonUI from "../../components/ui/Button";

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

import {
    funcoesGrupo,
    funcoesMedico
} from "../../utils/validators/userFunction";

export default function JoinLink() {

    const [waitingRole, setWaitingRole] = useState(false);
    const [funcao, setFuncao] = useState("");
    const [errorFuncao, setErrorFuncao] = useState(false);

    const hasExecuted =
        useRef(false);

    const navigate =
        useNavigate();

    const [searchParams] =
        useSearchParams();

    const { showAlert } =
        useAlert();

    const userType = localStorage.getItem("tipo");

    const opcoesFuncao =
        userType?.toLowerCase() === "saude"
            ? funcoesMedico
            : funcoesGrupo;

    const precisaEscolherFuncao =
        ["responsavel", "saude"]
            .includes(
                userType?.toLowerCase()
            );


    async function handleJoin(funcaoSelecionada = null) {

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

        const token =
            localStorage.getItem("token");

        if (!token) {

            navigate(
                `/login?redirect=/entrar?codigo=${code}`
            );

            return;
        }

        if (
            precisaEscolherFuncao
            &&
            !funcaoSelecionada
        ) {

            setWaitingRole(true);

            return;
        }

        try {

            await joinWithCode(code, funcaoSelecionada);

            showAlert("success", "Vínculo criado com sucesso");

            navigate("/links");

        } catch (error) {

            console.log(error);

            showAlert(
                "error",
                error?.response?.data
                || "Erro ao criar vínculo"
            );
        }
    }

    useEffect(() => {

        if (hasExecuted.current) {
            return;
        }

        hasExecuted.current = true;

        handleJoin();

    }, []);

    if (waitingRole) {

        return (

            <Box
                display="flex"
                flexDirection="column"
                justifyContent="center"
                alignItems="center"
                minHeight="70vh"
                gap={3}
            >

                <Typography
                    variant="h4"
                    fontWeight={700}
                    textAlign="center"
                >
                    Bem-vindo ao grupo 👋
                </Typography>

                <Typography
                    color="text.secondary"
                    textAlign="center"
                    maxWidth="500px"
                >
                    Antes de concluir seu vínculo,
                    informe qual será sua participação
                    no acompanhamento deste paciente.
                </Typography>

                <Box
                    display="flex"
                    flexDirection="column"
                    justifyContent="center"
                    alignItems="center"
                    sx={{

                        width: "100%",
                        maxWidth: "300px",
                        p: 4,
                        borderRadius: 4,
                        border: "1px solid #e5e5e5",
                        backgroundColor: "#fff",
                        boxShadow: "0 8px 30px rgba(0,0,0,.06)"
                    }}
                >
                    <Box>


                        <AutocompleteUI
                            label="Função no grupo"
                            options={opcoesFuncao}
                            error={errorFuncao}
                            value={
                                opcoesFuncao.find(
                                    option =>
                                        option.value === funcao
                                ) || null
                            }
                            onChange={(newValue) => {

                                setFuncao(
                                    newValue?.value || ""
                                );

                                setErrorFuncao(false);
                            }}
                            renderOption={(props, option) => (
                                <li {...props}>
                                    <Box>
                                        <Typography
                                            sx={{
                                                fontWeight: 600
                                            }}
                                        >
                                            {option.label}
                                        </Typography>

                                        <Typography
                                            variant="body2"
                                            sx={{
                                                color: "#777"
                                            }}
                                        >
                                            {option.descricao}
                                        </Typography>
                                    </Box>
                                </li>
                            )}
                        />
                    </Box>
                    <Box>
                        <ButtonUI
                            sx={{
                                mt: 3
                            }}
                            onClick={() => {

                                if (!funcao) {

                                    setErrorFuncao(true);

                                    return;
                                }

                                handleJoin(funcao);
                            }}
                        >
                            Entrar no grupo
                        </ButtonUI>
                    </Box>
                </Box>

            </Box>
        );
    }

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