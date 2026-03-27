import { Box, FormGroup, Grid, Paper, Tooltip, Typography } from "@mui/material";
import ButtonUI from "../../components/ui/Button";
import InputUI from "../../components/ui/Input";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import AlertUI from "../../components/ui/Alert";
import { createUser } from "../../services/userService"
import AutocompleteUI from "../../components/ui/Autocomplete";
import DatePickerUI from "../../components/ui/DatePicker";
import CheckboxUI from "../../components/ui/Checkbox";
import { formatCPF, isValidCpf } from "../../utils/formatters/formatCPF"
import { useAlert } from "../../hooks/useAlert";
import { isValidEmail } from "../../utils/formatters/formatEmail";

export default function Register() {
    const { showAlert } = useAlert();

    const [error, setError] = useState(false);
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
    const [errorMessage, setErrorMessage] = useState("");
    const [typeError, setTypeError] = useState("error");

    const navigate = useNavigate();

    // Regras senha
    const hasMinLength = (password) => password.length >= 8;
    const hasUppercase = (password) => /[A-Z]/.test(password);
    const hasLowercase = (password) => /[a-z]/.test(password);
    const hasNumber = (password) => /\d/.test(password);
    const hasSpecialChar = (password) => /[^A-Za-z\d]/.test(password);

    const rulesPassword = validatePassword(password);
    const repeatRulesPassword = validatePassword(repeatPassword);

    function validatePassword(password) {
        const result = {
            minLength: hasMinLength(password),
            upperCase: hasUppercase(password),
            lowerCase: hasLowercase(password),
            number: hasNumber(password),
            specialChar: hasSpecialChar(password)
        }

        return {
            ...result,
            isValid: Object.values(result).every(Boolean)
        };
    }

    const canRegister = () => {
        if (email == "" || CPF == "" || name == "" || userType == null || birthDate == null || password == "" || repeatPassword == "" || (userType.value === "saude" && advice == "")) {
            setError(true);
            setErrorMessage("Preencha todos os campos");
            setTypeError("error");
            return false;
        }

        if (!isValidCpf(CPF)) {
            setError(true);
            setErrorMessage("CPF incorreto");
            setTypeError("error");
            return false;

        }

        if (!isValidEmail(email)) {
            setError(true);
            setErrorMessage("Email inválido");
            setTypeError("error");
            return false;
        }

        if (!rulesPassword.isValid || !repeatRulesPassword.isValid) {
            setError(true);
            setErrorMessage("Verifique as regras de senha");
            setTypeError("error");
            return false;
        }

        if (password !== repeatPassword) {
            setError(true);
            setErrorMessage("As senhas devem ser iguais");
            setTypeError("error");
            return false;
        }

        setError(false);
        setErrorMessage("Sucesso");
        setTypeError("success");
        return true;

    };

    async function handleRegister() {

        if (!canRegister()) return;
        const data = {
            nome: name,
            email: email,
            senha: password,
            cpf: CPF,
            conselho: advice,
            tipo: userType.value, // "paciente", "responsavel" ou "saude"
            dataNascimento: birthDate, // formato "yyyy-MM-dd", ex: "2000-01-25"
            privCompartilharDiario: privateShareDaily,
            privCompartilharHabitos: privateShareHabits,
        };

        try {
            const response = await createUser(data);

            setError(true);
            setErrorMessage("Cadastro realizado com sucesso");
            setTypeError("success");

            console.log("Usuário criado:", response);

            showAlert("success", "Cadastro realizado com sucesso");

            navigate("/");

        } catch (error) {

            console.log(error);

            setError(true);
            setErrorMessage("Erro ao cadastrar usuário");
            setTypeError("error");
        }
    }

    const titlePasswordTooltip = (
        <Box>
            <Box style={{ color: rulesPassword.minLength ? "lightgreen" : "#ff6b6b" }}>
                • 8 caracteres
            </Box>
            <Box style={{ color: rulesPassword.upperCase ? "lightgreen" : "#ff6b6b" }}>
                • Letra maiúscula
            </Box>
            <Box style={{ color: rulesPassword.lowerCase ? "lightgreen" : "#ff6b6b" }}>
                • Letra minúscula
            </Box>
            <Box style={{ color: rulesPassword.number ? "lightgreen" : "#ff6b6b" }}>
                • Número
            </Box>
            <Box style={{ color: rulesPassword.specialChar ? "lightgreen" : "#ff6b6b" }}>
                • Caractere especial
            </Box>
        </Box>
    )

    const titleRepeatPasswordTooltip = (
        <Box>
            <Box style={{ color: repeatRulesPassword.minLength ? "lightgreen" : "#ff6b6b" }}>
                • 8 caracteres
            </Box>
            <Box style={{ color: repeatRulesPassword.upperCase ? "lightgreen" : "#ff6b6b" }}>
                • Letra maiúscula
            </Box>
            <Box style={{ color: repeatRulesPassword.lowerCase ? "lightgreen" : "#ff6b6b" }}>
                • Letra minúscula
            </Box>
            <Box style={{ color: repeatRulesPassword.number ? "lightgreen" : "#ff6b6b" }}>
                • Número
            </Box>
            <Box style={{ color: repeatRulesPassword.specialChar ? "lightgreen" : "#ff6b6b" }}>
                • Caractere especial
            </Box>
        </Box>
    )



    return (
        <Box>
            {error &&
                <AlertUI
                    type={typeError}
                    message={errorMessage}
                />
            }
            <Grid
                container
                justifyContent="center"
                alignItems="center"
                sx={{ minHeight: "95vh" }}
            >
                <Grid item>
                    <Paper elevation={5} sx={{ p: 4, width: 360, }}>
                        <Box
                            display="flex"
                            flexDirection="column"
                            alignItems="center"
                            gap={3}
                        >
                            <Typography variant="h4" gutterBottom>
                                Cadastro
                            </Typography>

                            <InputUI
                                label="Nome"
                                placeholder="Ex: João Silva"
                                type="string"
                                error={error && (name === "")}
                                value={name}
                                onChange={(e) => (
                                    setName(e.target.value),
                                    setError(false)
                                )}
                            >
                            </InputUI>

                            <InputUI
                                label="CPF"
                                placeholder="999.999.999-99"
                                limit={14}
                                error={error && (CPF === "" || !isValidCpf(CPF))}
                                value={formatCPF(CPF)}
                                onChange={(e) => {
                                    setCPF(e.target.value.replace(/\D/g, ""));
                                    setError(false);
                                }
                                }
                            >
                            </InputUI>

                            <AutocompleteUI
                                label="Tipo de usuário"
                                error={error && userType == null}
                                //helperText="Selecione um tipo válido"
                                value={userType}
                                onChange={(newValue) =>
                                    setUserType(newValue)
                                }
                                options={[
                                    { value: "paciente", label: "Paciente" },
                                    { value: "responsavel", label: "Responsável" },
                                    { value: "saude", label: "Saúde" }
                                ]}
                            />

                            {userType?.value === "saude" && <InputUI
                                label="Conselho"
                                placeholder="CRM, etc"
                                error={error && (advice === "")}
                                value={advice}
                                onChange={(e) => {
                                    setAdvice(e.target.value);
                                    setError(false);
                                }
                                }
                            >
                            </InputUI>}

                            <DatePickerUI
                                label="Data de nascimento"
                                error={error && birthDate == null}
                                value={birthDate}
                                onChange={setBirthDate}
                            />

                            <InputUI
                                label="Email"
                                placeholder="exemplo@gmail.com"
                                type="email"
                                error={error && (email === "" || !isValidEmail(email))}
                                value={email}
                                onChange={(e) => (
                                    setEmail(e.target.value),
                                    setError(false)
                                )}
                            >
                            </InputUI>

                            <Tooltip title={titlePasswordTooltip} placement="right" arrow>
                                <InputUI
                                    label="Senha"
                                    placeholder="Digite sua senha"
                                    type="password"
                                    showPasswordToggle={true}
                                    error={error && (password === "" || !rulesPassword.isValid || password !== repeatPassword)}
                                    value={password}
                                    onChange={(e) => (
                                        setPassword(e.target.value),
                                        setError(false)
                                    )}
                                >
                                </InputUI>
                            </Tooltip>

                            <Tooltip title={titleRepeatPasswordTooltip} placement="right" arrow>
                                <InputUI
                                    label="Repetir senha"
                                    placeholder="Repita sua senha"
                                    type="password"
                                    showPasswordToggle={true}
                                    error={error && (repeatPassword === "" || !repeatRulesPassword.isValid || password !== repeatPassword)}
                                    value={repeatPassword}
                                    onChange={(e) => (
                                        setRepeatPassword(e.target.value),
                                        setError(false)
                                    )}
                                >
                                </InputUI>
                            </Tooltip>
                            <FormGroup>
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
                            </FormGroup>

                            <ButtonUI
                                onClick={handleRegister}
                            >
                                Cadastrar
                            </ButtonUI>
                        </Box>
                    </Paper>
                </Grid>
            </Grid>
        </Box>
    )
}