import { Box, FormGroup, Grid, Paper, Tooltip, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { createUser } from "../../services/userService";

import { useAlert } from "../../hooks/useAlert";

import ButtonUI from "../../components/ui/Button";
import InputUI from "../../components/ui/Input";
import AutocompleteUI from "../../components/ui/Autocomplete";
import DatePickerUI from "../../components/ui/DatePicker";
import CheckboxUI from "../../components/ui/Checkbox";
import PasswordTooltip from "../../components/ui/Tooltip";

import { formatCPF, isValidCpf } from "../../utils/formatters/formatCPF"
import { isValidEmail } from "../../utils/formatters/formatEmail";
import { validatePassword } from "../../utils/validators/passwordValidator";
import { getDateLimit, isUnder18 } from "../../utils/validators/dateValidator";
import { formatPhone } from "../../utils/formatters/formatPhone";

export default function Register() {
    const { showAlert } = useAlert();

    const [errorName, setErrorName] = useState(false);
    const [errorCPF, setErrorCPF] = useState(false);
    const [errorEmail, setErrorEmail] = useState(false);
    const [errorUserType, setErrorUserType] = useState(false);
    const [errorBirthDate, setErrorBirthDate] = useState(false);
    const [errorPassword, setErrorPassword] = useState(false);
    const [errorRepeatPassword, setErrorRepeatPassword] = useState(false);
    const [errorAdvice, setErrorAdvice] = useState(false);
    const [errorPhone, setErrorPhone] = useState(false);
    const [errorWeight, setErrorWeight] = useState(false);
    const [errorHeight, setErrorHeight] = useState(false);

    const [loading, setLoading] = useState(false);

    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [CPF, setCPF] = useState("");
    const [advice, setAdvice] = useState("");
    const [userType, setUserType] = useState(null);
    const [birthDate, setBirthDate] = useState(null);
    const [privateShareDaily, setPrivateShareDaily] = useState(false);
    const [privateShareHabits, setPrivateShareHabits] = useState(false);
    const [email, setEmail] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [dateLimit, setDateLimit] = useState();
    const [phone, setPhone] = useState("");
    const [initialWeight, setInitialWeight] = useState("");
    const [height, setHeight] = useState("");

    const navigate = useNavigate();

    const rulesPassword = validatePassword(password);
    const repeatRulesPassword = validatePassword(repeatPassword);

    function isValidPhone(phone) {
        return /^\d{10,11}$/.test(phone);
    }

    function isValidPositiveNumber(value) {
        return !isNaN(value) && Number(value) > 0;
    }

    const canRegister = () => {
        if (
            email == "" ||
            CPF == "" ||
            name == "" ||
            phone == "" ||
            initialWeight == "" ||
            height == "" ||
            userType == null ||
            birthDate == null ||
            password == "" ||
            repeatPassword == "" ||
            (userType.value === "saude" && advice == "")
        ) {
            setErrorName(name == "");
            setErrorCPF(CPF == "");
            setErrorEmail(email == "");
            setErrorPhone(phone == "");
            setErrorWeight(initialWeight == "");
            setErrorHeight(height == "");
            setErrorUserType(userType == null);
            setErrorBirthDate(birthDate == null);
            setErrorPassword(password == "");
            setErrorRepeatPassword(repeatPassword == "");
            setErrorAdvice(userType?.value === "saude" && advice == "");
            showAlert("error", "Preencha todos os campos");
            return false;
        }

        if (name.length < 5) {
            setErrorName(true);
            showAlert("error", "O nome deve ter pelo menos 5 caracteres");
            return false;
        }

        if (!isValidCpf(CPF)) {
            setErrorCPF(true);
            showAlert("error", "CPF inválido");
            return false;
        }

        if (!isValidPhone(phone)) {
            setErrorPhone(true);
            showAlert("error", "Telefone inválido");
            return false;
        }

        if (!isValidPositiveNumber(initialWeight)) {
            setErrorWeight(true);
            showAlert("error", "Peso inválido");
            return false;
        }

        if (!isValidPositiveNumber(height)) {
            setErrorHeight(true);
            showAlert("error", "Altura inválida");
            return false;
        }

        if (!isValidEmail(email)) {
            setErrorEmail(true);
            showAlert("error", "Email inválido");
            return false;
        }

        if (!rulesPassword.isValid || !repeatRulesPassword.isValid) {
            setErrorPassword(true);
            setErrorRepeatPassword(true);
            showAlert("error", "Verifique as regras de senha");
            return false;
        }

        if (password !== repeatPassword) {
            setErrorPassword(true);
            setErrorRepeatPassword(true);
            showAlert("error", "As senhas devem ser iguais");
            return false;
        }

        if (
            (userType?.value === "responsavel" || userType?.value === "saude") &&
            isUnder18(birthDate)
        ) {
            setErrorBirthDate(true);
            showAlert(
                "error",
                `"${userType?.label}" precisa ser maior de idade`
            );
            return;
        }

        setErrorName(false);
        setErrorCPF(false);
        setErrorEmail(false);
        setErrorPhone(false);
        setErrorWeight(false);
        setErrorHeight(false);
        setErrorUserType(false);
        setErrorBirthDate(false);
        setErrorPassword(false);
        setErrorRepeatPassword(false);
        setErrorAdvice(false);

        return true;

    };

    async function handleRegister() {

        if (loading) return;
        setLoading(true);

        if (!canRegister()) {
            setLoading(false);
            return;
        }

        const data = {
            nome: name,
            email: email,
            telefone: phone,
            pesoInicial: Number(initialWeight),
            altura: Number(height),
            senha: password,
            cpf: CPF,
            conselho: advice,
            tipo: userType.value,
            dataNascimento: birthDate,
            privCompartilharDiario: privateShareDaily,
            privCompartilharHabitos: privateShareHabits,
        };

        try {

            const response = await createUser(data);

            console.log("Usuário criado:", response);

            showAlert("success", "Cadastro realizado com sucesso");

            navigate("/login");

            setLoading(false);

        } catch (error) {

            console.log("Erro que retorna do backend: ", error.response.data.value);
            if (error.response.data.value === "duplicateEmail") {
                showAlert("error", "CPF ou Email já cadastrados");
                setErrorEmail(true);
                setErrorCPF(true);
                return;
            }
            showAlert("error", "Erro ao cadastrar usuário");
            setLoading(false);
        } finally {

            setLoading(false);

        }
    }

    useEffect(() => {
        setDateLimit(getDateLimit(userType));
    }, [userType]);

    return (
        <Box>
            <Grid
                container
                justifyContent="center"
                alignItems="center"
            >
                <Grid item>
                    <Paper elevation={5} sx={{ p: 4, width: 360, }}>
                        <Box
                            display="flex"
                            flexDirection="column"
                            alignItems="center"
                            gap={1}
                        >
                            <Box textAlign="center" mb={2}>
                                <Typography
                                    sx={{
                                        fontFamily: 'Inter, sans-serif',
                                        fontWeight: 600,
                                        fontSize: '28px',
                                        color: '#1a1a1a',
                                        letterSpacing: '0.5px',
                                    }}
                                >
                                    Vitta<span style={{ fontWeight: 400 }}>Sync</span>
                                </Typography>

                                <Typography
                                    sx={{
                                        fontFamily: 'Inter, sans-serif',
                                        fontWeight: 500,
                                        fontSize: '20px',
                                        color: '#4a4a4a',
                                        mt: 0.5,
                                    }}
                                >
                                    Cadastro
                                </Typography>
                            </Box>

                            <InputUI
                                label="Nome"
                                placeholder="Ex: João Silva"
                                type="string"
                                error={errorName}
                                value={name}
                                onChange={(e) => (
                                    setName(e.target.value),
                                    setErrorName(false)
                                )}
                            >
                            </InputUI>

                            <InputUI
                                label="CPF"
                                placeholder="999.999.999-99"
                                limit={14}
                                error={errorCPF}
                                value={formatCPF(CPF)}
                                onChange={(e) => {
                                    setCPF(e.target.value.replace(/\D/g, ""));
                                    setErrorCPF(false);
                                }
                                }
                            >
                            </InputUI>

                            <InputUI
                                label="Email"
                                placeholder="exemplo@gmail.com"
                                type="email"
                                error={errorEmail}
                                value={email}
                                onChange={(e) => (
                                    setEmail(e.target.value),
                                    setErrorEmail(false)
                                )}
                            >
                            </InputUI>

                            <InputUI
                                label="Telefone"
                                placeholder="(11) 99999-9999"
                                limit={15}
                                error={errorPhone}
                                value={formatPhone(phone)}
                                onChange={(e) => {
                                    setPhone(e.target.value.replace(/\D/g, ""));
                                    setErrorPhone(false);
                                }}
                            >
                            </InputUI>

                            <InputUI
                                label="Peso Inicial"
                                placeholder="Ex: 70.5"
                                type="number"
                                error={errorWeight}
                                value={initialWeight}
                                onChange={(e) => {
                                    const value = e.target.value;
                                    if (value < 0 || value > 500) return;
                                    setInitialWeight(value);
                                    setErrorWeight(false);
                                }}
                            >
                            </InputUI>

                            <InputUI
                                label="Altura"
                                placeholder="Ex: 1.75"
                                type="number"
                                error={errorHeight}
                                value={height}
                                onChange={(e) => {
                                    const value = e.target.value;
                                    if (value < 0 || value > 3) return;
                                    setHeight(value);
                                    setErrorHeight(false);
                                }}
                            >
                            </InputUI>

                            <AutocompleteUI
                                label="Tipo de usuário"
                                error={errorUserType && userType == null}
                                //helperText="Selecione um tipo válido"
                                value={userType}
                                onChange={(newValue) =>
                                    setUserType(newValue)
                                    // setErrorUserType(false)
                                }
                                options={[
                                    { value: "paciente", label: "Paciente" },
                                    { value: "responsavel", label: "Responsável" },
                                    { value: "saude", label: "Profissional da Saúde" }
                                ]}
                            />

                            {userType?.value === "saude" && <InputUI
                                label="Conselho"
                                placeholder="CRM, etc"
                                error={errorAdvice}
                                value={advice}
                                onChange={(e) => {
                                    setAdvice(e.target.value);
                                    setErrorAdvice(false);
                                }
                                }
                            >
                            </InputUI>}

                            <DatePickerUI
                                label="Data de nascimento"
                                dateLimit={dateLimit}
                                error={errorBirthDate && birthDate == null}
                                value={birthDate}
                                onChange={setBirthDate}
                            />

                            <Tooltip
                                title={<PasswordTooltip rules={rulesPassword} />}
                                placement="right"
                                arrow
                            >
                                <InputUI
                                    label="Senha"
                                    placeholder="Digite sua senha"
                                    type="password"
                                    showPasswordToggle={true}
                                    error={errorPassword}
                                    value={password}
                                    onChange={(e) => (
                                        setPassword(e.target.value),
                                        setErrorPassword(false)
                                    )}
                                />
                            </Tooltip>

                            <Tooltip
                                title={<PasswordTooltip rules={repeatRulesPassword} />}
                                placement="right"
                                arrow
                            >
                                <InputUI
                                    label="Repetir senha"
                                    placeholder="Repita sua senha"
                                    type="password"
                                    showPasswordToggle={true}
                                    error={errorRepeatPassword}
                                    value={repeatPassword}
                                    onChange={(e) => (
                                        setRepeatPassword(e.target.value),
                                        setErrorRepeatPassword(false)
                                    )}
                                >
                                </InputUI>
                            </Tooltip>

                            {userType?.value === "paciente" && <FormGroup>
                                <CheckboxUI
                                    label="Permitir compartilhar dados diarios"
                                    checked={privateShareDaily}
                                    onChange={setPrivateShareDaily}
                                />

                                <CheckboxUI
                                    label="Permitir compartilhar dados dos hábitos"
                                    checked={privateShareHabits}
                                    onChange={setPrivateShareHabits}
                                />
                            </FormGroup>}

                            <ButtonUI
                                onClick={handleRegister}
                                disabled={loading}
                            >
                                {loading ? "Cadastrando..." : "Cadastrar"}
                            </ButtonUI>
                        </Box>
                    </Paper>
                </Grid>
            </Grid>
        </Box>
    )
}