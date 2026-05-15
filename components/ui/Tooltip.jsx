import { Box } from "@mui/material";

export default function PasswordTooltip({ rules }) {
    const getColor = (valid) => (valid ? "lightgreen" : "#ff6b6b");

    return (
        <Box>
            <Box style={{ color: getColor(rules.minLength) }}>
                • 8 caracteres
            </Box>
            <Box style={{ color: getColor(rules.upperCase) }}>
                • Letra maiúscula
            </Box>
            <Box style={{ color: getColor(rules.lowerCase) }}>
                • Letra minúscula
            </Box>
            <Box style={{ color: getColor(rules.number) }}>
                • Número
            </Box>
            <Box style={{ color: getColor(rules.specialChar) }}>
                • Caractere especial
            </Box>
        </Box>
    );
}