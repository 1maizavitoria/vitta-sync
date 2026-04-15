import React, { useState } from "react";
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

export default function Perfil() {
    const [editing, setEditing] = useState(false);
    const { showAlert } = useAlert();
    const [error, setError] = useState(false);
    const [errorEmail, setErrorEmail] = useState(false);
    const [errorName, setErrorName] = useState(false);

    const [formData, setFormData] = useState({
        nome: localStorage.getItem("nome") || "",
        dataNascimento: localStorage.getItem("dataNascimento") || "",
        cpf: localStorage.getItem("CPF") || "",
        email: localStorage.getItem("email") || "",
        tipo: localStorage.getItem("tipo") || "",
        conselho: localStorage.getItem("conselho") || "",
        privCompartilharDiario: localStorage.getItem("privCompartilharDiario") === "true",
        privCompartilharHabitos: localStorage.getItem("privCompartilharHabitos") === "true",
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

        if (
            (formData?.tipo === "responsavel" || formData?.tipo === "saude") &&
            isUnder18(formData.dataNascimento)
        ) {
            showAlert(
                "error",
                `"${formData.tipo}" precisa ser maior de idade`
            );
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
            privCompartilharDiario: formData.privCompartilharDiario,
            privCompartilharHabitos: formData.privCompartilharHabitos
        }

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



    return (
        <Paper elevation={3} sx={{ p: 3, borderRadius: 3 }}>
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                <Box>
                    <Typography variant="h6">Meus Dados de Cadastro</Typography>
                    <Typography variant="body2" color="text.secondary">
                        Informações pessoais
                    </Typography>
                </Box>

                {!editing ? (
                    <ButtonUI onClick={() => setEditing(true)}>
                        Editar
                    </ButtonUI>
                ) : (
                    <Box display="flex" gap={1}>

                        <ButtonUI onClick={() => {
                            handleDelete();
                            setEditing(false);
                        }}>
                            Deletar conta
                        </ButtonUI>

                        <ButtonUI onClick={() => setEditing(false)}>
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

                <Grid item xs={12} md={6}>
                    <InputUI
                        label="Tipo"
                        value={formData.tipo}
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

                {formData.tipo === "paciente" && <FormGroup>
                    <CheckboxUI
                        disabled={!editing}
                        label="Permitir compartilhar dados diarios"
                        checked={formData.privCompartilharDiario}
                        onChange={handleChange("privCompartilharDiario")}
                    />

                    <CheckboxUI
                        disabled={!editing}
                        label="Permitir compartilhar dados dos hábitos"
                        checked={formData.privCompartilharHabitos}
                        onChange={handleChange("privCompartilharHabitos")}
                    />
                </FormGroup>}
            </Grid>
        </Paper>
    );
}