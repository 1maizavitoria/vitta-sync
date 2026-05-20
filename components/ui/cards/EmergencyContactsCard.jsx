import { Box, Typography, Paper, Button, Grid, TextField } from "@mui/material";
import { useEffect, useState } from "react";
import AddIcon from "@mui/icons-material/Add";
import { getEmergencyContacts, createEmergencyContact, deleteEmergencyContact, editEmergencyContact } from "../../../services/contactEmergencyService";
import { usePatient } from "../../../context/PatientContext";
import ButtonUI from "../Button";
import DeleteIcon from "@mui/icons-material/Delete";
import InputUI from "../Input";
import { useAlert } from "../../../hooks/useAlert";
import { formatPhone, isValidPhone } from "../../../utils/formatters/formatPhone";
import DialogUI from "../Dialog";
import PersonIcon from "@mui/icons-material/Person";
import PhoneIcon from "@mui/icons-material/Phone";
import EditIcon from "@mui/icons-material/Edit";

export default function EmergencyContacts() {
    const { showAlert } = useAlert();
    const { selectedPatient } = usePatient();
    const [contacts, setContacts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [adding, setAdding] = useState(false);
    const [formData, setFormData] = useState({
        nome: "",
        telefone: ""
    });
    const userType = localStorage.getItem("tipo")?.toLowerCase();
    const loggedCpf = localStorage.getItem("CPF");
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
    const [selectedContactId, setSelectedContactId] = useState(null);

    const [errors,
        setErrors] =
        useState({
            nome: "",
            telefone: ""
        });
    const [editingId,
        setEditingId] =
        useState(null);

    const [editData,
        setEditData] =
        useState({
            nome: "",
            telefone: ""
        });

    function validateForm() {

        const newErrors = {
            nome: "",
            telefone: ""
        };

        if (
            formData.nome
                .trim()
                .length < 5
        ) {

            newErrors.nome =
                "Nome deve ter no mínimo 5 caracteres";
        }

        if (
            !isValidPhone(
                formData.telefone
            )
        ) {

            newErrors.telefone =
                "Telefone inválido";
        }

        setErrors(newErrors);

        return !(
            newErrors.nome
            ||
            newErrors.telefone
        );
    }

    function startEdit(contact) {

        setEditingId(contact.id);

        setEditData({
            nome: contact.nome,
            telefone: contact.telefone
        });
    }

    async function handleSaveContact() {
        if (!validateForm()) {
            return;
        }

        try {
            const created =
                await createEmergencyContact(
                    selectedPatient.cpf,
                    formData
                );

            setContacts((prev) => [
                ...prev,
                created
            ]);

            setFormData({
                nome: "",
                telefone: ""
            });

            setAdding(false);

            showAlert(
                "success",
                "Contato salvo com sucesso"
            );

        } catch (error) {
            console.log(error);
            showAlert(
                "error",
                "Erro ao salvar contato"
            );
        }
    }

    async function confirmDeleteContact() {

        try {

            await deleteEmergencyContact(
                selectedContactId,
                selectedPatient.cpf
            );

            setContacts((prev) =>
                prev.filter(
                    contact =>
                        contact.id !==
                        selectedContactId
                )
            );

            showAlert(
                "success",
                "Contato removido"
            );

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                "Erro ao remover contato"
            );

        } finally {

            setOpenDeleteDialog(false);

            setSelectedContactId(null);
        }
    }

    async function saveEdit(id) {

        try {

            const updated =
                await editEmergencyContact(
                    id,
                    selectedPatient.cpf,
                    editData
                );

            setContacts((prev) =>

                prev.map((contact) =>

                    contact.id === id
                        ? updated
                        : contact
                )
            );

            showAlert(
                "success",
                "Contato atualizado"
            );

            setEditingId(null);

        } catch (error) {

            console.error(error);

            showAlert(
                "error",
                "Erro ao editar contato"
            );
        }
    }
    const isPersonalContext =
        selectedPatient?.cpf
        === loggedCpf;

    const canEdit = (userType === "paciente" && isPersonalContext)
        || (userType === "responsavel" && !isPersonalContext);

    const shouldShowContacts = userType === "paciente" || (userType !== "paciente" && !isPersonalContext);

    useEffect(() => {

        if (!selectedPatient?.cpf) {
            return;
        }

        async function fetchContacts() {

            try {
                setLoading(true);
                const data = await getEmergencyContacts(selectedPatient.cpf);
                setContacts(data);

            } catch (error) {
                console.error(error);

            } finally {
                setLoading(false);

            }
        }
        setAdding(false);

        setFormData({
            nome: "",
            telefone: ""
        });

        fetchContacts();

    }, [selectedPatient?.cpf]);


    if (!shouldShowContacts) {
        return null;
    }

    return (

        <Paper
            elevation={3}
            sx={{
                p: 3,
                borderRadius: 3,
                mt: 3,
                display: "flex",
                flexDirection: "column",
                gap: 2
            }}
        >

            <Typography
                variant="h5"
                fontWeight={700}
            >
                Contatos de Emergência
            </Typography>

            <Typography
                variant="body2"
                color="text.secondary"
                mb={3}
            >
                Pessoas para contato rápido
                em situações importantes.
            </Typography>


            {loading && <Typography
                color="text.secondary"
            >
                Carregando contatos...
            </Typography>
            }

            {contacts.map((contact) => (

                <Grid
                    item
                    xs={12}
                    md={6}
                    key={contact.id}
                >

                    <Paper
                        elevation={1}
                        sx={{
                            p: 2,
                            borderRadius: 3,
                            display: "flex",
                            flexDirection: "column",
                            justifyContent: "space-between"
                        }}
                    >
                        <Box
                            display="flex"
                            justifyContent="space-between"
                            alignItems="center"
                            gap={2}
                        >
                            <Box
                                display="flex"

                                flexDirection="column"
                                gap={1.5}
                            >
                                {editingId === contact.id ? (

                                    <Box
                                        display="flex"
                                        justifyContent="space-between"
                                        gap={2}
                                    >
                                        <Box
                                            display="flex"
                                            flexDirection="row"
                                            gap={2}
                                        >
                                            <InputUI
                                                label="Nome"
                                                value={editData.nome}
                                                onChange={(e) =>
                                                    setEditData({
                                                        ...editData,
                                                        nome:
                                                            e.target.value
                                                    })
                                                }
                                            />

                                            <InputUI
                                                label="Telefone"
                                                value={formatPhone(
                                                    editData.telefone
                                                )}
                                                onChange={(e) => {

                                                    const rawValue =
                                                        e.target.value
                                                            .replace(/\D/g, "");

                                                    setEditData({
                                                        ...editData,
                                                        telefone:
                                                            rawValue
                                                    });
                                                }}
                                            />
                                        </Box>

                                        <Box
                                            display="flex"
                                            alignItems="center"
                                            gap={2}
                                        >

                                            <ButtonUI
                                                onClick={() =>
                                                    saveEdit(
                                                        contact.id
                                                    )
                                                }
                                            >
                                                Salvar
                                            </ButtonUI>

                                            <ButtonUI
                                                variant="outlined"
                                                onClick={() =>
                                                    setEditingId(null)
                                                }
                                            >
                                                Cancelar
                                            </ButtonUI>

                                        </Box>

                                    </Box>

                                ) : (

                                    <Box>
                                        <Box
                                            display="flex"
                                            alignItems="center"
                                            gap={1}
                                        >

                                            <PersonIcon
                                                sx={{
                                                    fontSize: 20,
                                                    color: "#666"
                                                }}
                                            />

                                            <Typography
                                                fontWeight={700}
                                            >
                                                {contact.nome}

                                            </Typography>


                                        </Box>

                                        <Box
                                            display="flex"
                                            alignItems="center"
                                            gap={1}
                                        >

                                            <PhoneIcon
                                                sx={{
                                                    fontSize: 20,
                                                    color: "#666"
                                                }}
                                            />

                                            <Typography
                                                color="text.secondary"
                                            >
                                                {formatPhone(
                                                    contact.telefone
                                                )}
                                            </Typography>

                                        </Box>
                                    </Box>
                                )}
                            </Box>

                            <Box
                                gap={5}
                            >
                                {canEdit && <EditIcon
                                    onClick={() =>
                                        startEdit(contact)
                                    }
                                    sx={{
                                        color: "#555",
                                        cursor: "pointer",

                                        "&:hover": {
                                            opacity: 0.7
                                        }
                                    }}
                                />}

                                {canEdit &&
                                    <DeleteIcon
                                        onClick={() => {

                                            setSelectedContactId(
                                                contact.id
                                            );

                                            setOpenDeleteDialog(true);
                                        }}
                                        sx={{
                                            color: "#d32f2f",
                                            cursor: "pointer",

                                            "&:hover": {
                                                opacity: 0.7
                                            }
                                        }}
                                    />
                                }

                            </Box>

                        </Box>
                    </Paper>

                </Grid>
            ))}

            {!loading && contacts.length === 0 && (

                <Box
                    sx={{
                        width: "100%",
                        py: 4,
                        textAlign: "center"
                    }}
                >

                    <Typography
                        color="text.secondary"
                    >
                        Nenhum contato cadastrado
                    </Typography>

                </Box>

            )}

            {canEdit && !adding && contacts.length < 3 && (

                <Box
                    sx={{
                        border:
                            "2px dashed #ddd",
                        borderRadius: "16px",
                        height: "100%",
                        width: "100%",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        cursor: "pointer",
                        transition: "0.2s",
                        "&:hover": {
                            backgroundColor:
                                "#fafafa"
                        }
                    }}
                >

                    <Button
                        onClick={() =>
                            setAdding(true)
                        }
                        sx={{
                            fontSize: 32, height: 120,
                            width: "100%",

                        }}
                    >
                        +
                    </Button>


                </Box>
            )}

            {adding && (

                <Paper
                    elevation={0}
                    sx={{
                        mt: 2,
                        p: 2,
                        borderRadius: 3,
                        border: "1px solid #eee",
                    }}
                >
                    <Box
                        display="flex"
                        justifyContent="space-between"
                        alignItems="center"
                    >
                        <Grid
                            container
                            spacing={2}
                        >

                            <Grid item xs={12} md={6}>

                                <InputUI
                                    label="Nome"
                                    error={!!errors.nome}
                                    helperText={errors.nome}
                                    fullWidth
                                    value={formData.nome}
                                    onChange={(e) => {

                                        setErrors({
                                            ...errors,
                                            nome: ""
                                        });

                                        setFormData({
                                            ...formData,
                                            nome: e.target.value
                                        });
                                    }}
                                />

                            </Grid>

                            <Grid item xs={12} md={6}>

                                <InputUI
                                    label="Telefone"
                                    error={!!errors.telefone}
                                    helperText={errors.telefone}
                                    fullWidth
                                    limit={15}
                                    value={formatPhone(
                                        formData.telefone
                                    )}
                                    onChange={(e) => {

                                        const rawValue =
                                            e.target.value
                                                .replace(/\D/g, "");

                                        setErrors({
                                            ...errors,
                                            telefone: ""
                                        });

                                        setFormData({
                                            ...formData,
                                            telefone: rawValue
                                        });
                                    }}
                                />

                            </Grid>

                        </Grid>


                        <Box
                            display="flex"
                            gap={1}
                            mt={2}
                        >

                            <ButtonUI
                                variant="outlined"
                                onClick={() => {

                                    setAdding(false);

                                    setFormData({
                                        nome: "",
                                        telefone: ""
                                    });
                                }}
                            >
                                Cancelar
                            </ButtonUI>

                            <ButtonUI
                                variant="contained"
                                onClick={handleSaveContact}
                            >
                                Salvar contato
                            </ButtonUI>

                        </Box>
                    </Box>

                </Paper>


            )}

            <DialogUI
                open={openDeleteDialog}
                onClose={() => {
                    setOpenDeleteDialog(false);
                    setSelectedContactId(null);
                }}
                title="Remover contato"
                description="Deseja realmente remover este contato de emergência?"
                onConfirm={confirmDeleteContact}
                confirmText="Remover"
                cancelText="Cancelar"
            />

        </Paper>

    );
}