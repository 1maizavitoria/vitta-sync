import React, { useEffect, useState } from "react";
import { Box, Grid, Typography, Paper, FormGroup } from "@mui/material";
import ButtonUI from "./Button";
import InputUI from "./Input";
import { deleteUser, editUser } from "../../services/userService";
import DatePickerUI from "./DatePicker";
import { useAlert } from "../../hooks/useAlert";
import { isValidEmail } from "../../utils/formatters/formatEmail";
import { logout } from "../../services/authService";
import CheckboxUI from "./Checkbox";
import { getDateLimit, isUnder18 } from "../../utils/validators/dateValidator";
import { formatPhone, isValidPhone } from "../../utils/formatters/formatPhone";
import { usePatient } from "../../context/PatientContext";
import { getUserByCpf } from "../../services/userService";
import Tooltip from "@mui/material/Tooltip";

export default function Perfil() {
    const { selectedPatient } = usePatient();

    const [editing, setEditing] = useState(false);
    const { showAlert } = useAlert();
    const [error, setError] = useState(false);
    const [errorEmail, setErrorEmail] = useState(false);
    const [errorName, setErrorName] = useState(false);

    const [formData, setFormData] = useState({
        nome: "",
        dataNascimento: "",
        cpf: "",
        email: "",
        tipo: "",
        conselho: "",
        telefone: "",
        pesoInicial: "",
        altura: "",
        funcaoResponsavel: ""
    });

    const handleChange = (campo) => (value) => {
        setFormData(prev => ({
            ...prev,
            [campo]: value,
            ...(campo === "tipo" && { dataNascimento: getDateLimit(value) })
        }));
    };

    function canSave() {
        if (!formData.nome || !formData.email || !formData.dataNascimento) {
            showAlert("error", "Preencha todos os campos");
            setError(true);
            return false;
        }

        if (formData.nome.length < 5) {
            setErrorName(true);
            showAlert("error", "O nome deve ter pelo menos 5 caracteres");
            return false;
        }

        if (!isValidEmail(formData.email)) {
            setErrorEmail(true);
            showAlert("error", "Email inválido");
            return false;
        }

        if (!isValidPhone(formData.telefone)) {
            showAlert("error", "Telefone inválido");
            return false;
        }

        if (formData.tipo === "paciente") {
            if (Number(formData.pesoInicial) <= 0) {
                showAlert("error", "Peso inválido");
                return false;
            }
            if (Number(formData.altura) <= 0) {
                showAlert("error", "Altura inválida");
                return false;
            }
        }


        if (
            (formData?.tipo === "responsavel" || formData?.tipo === "saude") &&
            isUnder18(formData.dataNascimento)
        ) {
            showAlert("error", `"${formData.tipo}" precisa ser maior de idade`);
            setError(true);
            return;
        }

        setError(false);
        return true;
    }

    async function handleChangeSave() {

        if (!canSave()) return;

        const data = {
            nome: formData.nome,
            email: formData.email,
            dataNascimento: formData.dataNascimento,
            telefone: formData.telefone,
            ...(formData.tipo === "paciente" && {
                pesoInicial: Number(formData.pesoInicial),
                altura: Number(formData.altura),
            }),
            ...(formData.tipo === "responsavel" && {
                funcaoResponsavel: formData.funcaoResponsavel,
            }),
            ...(formData.tipo === "saude" && {
                conselho: formData.conselho,
            }),
        };

        try {
            await editUser(formData.cpf, data);
            setEditing(false);
            showAlert("success", "Dados atualizados com sucesso");

        } catch (error) {
            setEditing(true);
            showAlert("error", "Erro ao salvar dados");
            console.error("Erro ao salvar dados:", error);
        }
    }

    async function handleDelete() {

        const confirmed = window.confirm(
            "Tem certeza que deseja deletar sua conta? Essa ação não pode ser desfeita."
        );

        if (!confirmed) return;

        try {
            await deleteUser(formData.cpf);
            showAlert("success", "Conta deletada com sucesso");
        } catch (error) {
            showAlert("error", "Erro ao deletar conta");
            console.error(error);
            return;
        }

        try {
            await logout();
        } catch (error) {
            console.error("Erro no logout:", error);
        }

    }

    const userType = localStorage.getItem("tipo")?.toLowerCase();
    const canEdit = userType !== "saude";
    const loggedCpf = localStorage.getItem("CPF");

    const targetCpf =
        userType === "paciente"
            ? localStorage.getItem("CPF")
            : selectedPatient?.cpf;

    useEffect(() => {
        if (!targetCpf) return;

        async function fetchUser() {
            try {
                const data =
                    await getUserByCpf({
                        CPF: targetCpf
                    });

                setFormData({
                    nome: data.nome,
                    telefone: data.telefone,
                    pesoInicial: data.pesoInicial,
                    altura: data.altura,
                    dataNascimento: data.dataNascimento,
                    cpf: data.cpf,
                    email: data.email,
                    tipo: data.tipo,
                    conselho: data.conselho,
                    funcaoResponsavel: data.funcaoResponsavel,
                });

            } catch (error) {

                console.error(error);
            }
        }

        fetchUser();

    }, [targetCpf]);


    return (
        <Paper elevation={3} sx={{ p: 3, borderRadius: 3 }}>
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                <Box>
                    <Typography
                        variant="h4"
                        sx={{
                            fontWeight: 700
                        }}
                    >
                        Dados de Cadastro de {selectedPatient?.nome || "Paciente"}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        Informações pessoais
                    </Typography>
                </Box>

                {!editing && (canEdit || loggedCpf === targetCpf) && (

                    <ButtonUI
                        onClick={() =>
                            setEditing(true)
                        }
                    >
                        Editar
                    </ButtonUI>

                )}

                {editing && (canEdit || loggedCpf === targetCpf) && (

                    <Box
                        display="flex"
                        gap={1}
                    >

                        <ButtonUI
                            onClick={() => {

                                handleDelete();

                                setEditing(false);
                            }}
                        >
                            Deletar conta
                        </ButtonUI>

                        <ButtonUI
                            onClick={() =>
                                setEditing(false)
                            }
                        >
                            Cancelar
                        </ButtonUI>

                        <ButtonUI
                            onClick={() => {

                                handleChangeSave();
                            }}
                        >
                            Salvar
                        </ButtonUI>

                    </Box>

                )}
            </Box>

            <Grid container spacing={2}>
                <Grid item xs={12} md={6}>
                    <InputUI
                        label="Nome"
                        value={formData.nome}
                        onChange={(e) => {
                            handleChange("nome")(e.target.value);
                            setErrorName(false);
                        }}
                        disabled={!editing}
                        fullWidth
                        error={(error && !formData.nome) || errorName}
                    />
                </Grid>

                <Grid item xs={12} md={6}>

                    <InputUI
                        label="Telefone"
                        limit={15}
                        value={formatPhone(
                            formData.telefone
                        )}
                        onChange={(e) => {
                            const rawValue =
                                e.target.value
                                    .replace(/\D/g, "");
                            handleChange(
                                "telefone"
                            )(rawValue);
                        }}
                        disabled={!editing}
                        fullWidth
                    />

                </Grid>

                <Grid item xs={12} md={6}>
                    <DatePickerUI
                        label="Data de Nascimento"
                        value={formData.dataNascimento}
                        onChange={(value) =>
                            setFormData({
                                ...formData,
                                dataNascimento: value
                            })
                        }
                        disabled={!editing}
                        fullWidth
                        error={error && !formData.dataNascimento}
                    />
                </Grid>

                <Grid item xs={12} md={6}>
                    <InputUI
                        label="Email"
                        value={formData.email}
                        onChange={(e) => {
                            handleChange("email")(e.target.value);
                            setErrorEmail(false);
                        }}
                        disabled={!editing}
                        fullWidth
                        error={(error && !formData.email) || errorEmail}
                    />
                </Grid>

                <Grid item xs={12} md={6}>
                    <InputUI
                        label="CPF"
                        value={formData.cpf}
                        disabled
                        fullWidth
                    />
                </Grid>

                {formData.tipo === "saude" && (
                    <Grid item xs={12}>
                        <InputUI
                            label="Conselho"
                            value={formData.conselho}
                            onChange={handleChange("conselho")}
                            disabled={!editing}
                            fullWidth
                            error={error}
                        />
                    </Grid>
                )}

                {formData.tipo === "paciente" && <Grid item xs={12} md={6}>
                    <InputUI
                        label="Peso Inicial (kg)"
                        type="number"
                        value={formData.pesoInicial}
                        onChange={(e) =>
                            handleChange(
                                "pesoInicial"
                            )(e.target.value)
                        }
                        disabled={!editing}
                        fullWidth
                    />
                </Grid>}

                {formData.tipo === "paciente" && <Grid item xs={12} md={6}>
                    <InputUI
                        label="Altura (m)"
                        type="number"
                        value={formData.altura}
                        onChange={(e) =>
                            handleChange(
                                "altura"
                            )(e.target.value)
                        }
                        disabled={!editing}
                        fullWidth
                    />
                </Grid>}
            </Grid>
        </Paper>
    );
}