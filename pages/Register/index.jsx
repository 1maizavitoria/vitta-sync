import { Box, FormGroup, Grid, Paper, Tooltip, Typography } from "@mui/material";
import ButtonUI from "../../components/ui/Button";
import InputUI from "../../components/ui/Input";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { createUser } from "../../services/userService"
import AutocompleteUI from "../../components/ui/Autocomplete";
import DatePickerUI from "../../components/ui/DatePicker";
import CheckboxUI from "../../components/ui/Checkbox";
import { formatCPF, isValidCpf } from "../../utils/formatters/formatCPF"
import { useAlert } from "../../hooks/useAlert";
import { isValidEmail } from "../../utils/formatters/formatEmail";
import { validatePassword } from "../../utils/validators/passwordValidator";
import PasswordTooltip from "../../components/ui/Tooltip";
import { getDateLimit, isUnder18 } from "../../utils/validators/dateValidator";

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
    const [dateLimit, setDateLimit] = useState()

    const navigate = useNavigate();

    const rulesPassword = validatePassword(password);
    const repeatRulesPassword = validatePassword(repeatPassword);

    const canRegister = () => {
        if (email == "" || CPF == "" || name == "" || userType == null || birthDate == null || password == "" || repeatPassword == "" || (userType.value === "saude" && advice == "")) {
            setError(true);
            showAlert("error", "Preencha todos os campos");
            return false;
        }

        if (name.length < 5) {
            setError(true);
            showAlert("error", "O nome deve ter pelo menos 5 caracteres");
            return false;
        }

        if (!isValidCpf(CPF)) {
            setError(true);
            showAlert("error", "CPF inválido");
            return false;

        }

        if (!isValidEmail(email)) {
            setError(true);
            showAlert("error", "Email inválido");
            return false;
        }

        if (!rulesPassword.isValid || !repeatRulesPassword.isValid) {
            setError(true);
            showAlert("error", "Verifique as regras de senha");
            return false;
        }

        if (password !== repeatPassword) {
            setError(true);
            showAlert("error", "As senhas devem ser iguais");
            return false;
        }

        if (
            (userType?.value === "responsavel" || userType?.value === "saude") &&
            isUnder18(birthDate)
        ) {
            showAlert(
                "error",
                `"${userType?.label}" precisa ser maior de idade`
            );
            setError(true);
            return;
        }

        setError(false);
        //showAlert("success", "Sucesso");
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

            console.log("Usuário criado:", response);

            showAlert("success", "Cadastro realizado com sucesso");

            navigate("/login");

        } catch (error) {

            console.log("Erro que retorna do backend: ", error.response.data.value);

            setError(true);
            if (error.response.data.value === "duplicateEmail") {
                showAlert("error", "CPF ou Email já cadastrados");
                return;
            }
            showAlert("error", "Erro ao cadastrar usuário");
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
                                    { value: "saude", label: "Profissional da Saúde" }
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
                                dateLimit={dateLimit}
                                error={error && birthDate == null}
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
                                    error={error && (password === "" || !rulesPassword.isValid || password !== repeatPassword)}
                                    value={password}
                                    onChange={(e) => (
                                        setPassword(e.target.value),
                                        setError(false)
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
                                    error={error && (repeatPassword === "" || !repeatRulesPassword.isValid || password !== repeatPassword)}
                                    value={repeatPassword}
                                    onChange={(e) => (
                                        setRepeatPassword(e.target.value),
                                        setError(false)
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