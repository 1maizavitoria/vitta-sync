export function formatCPF(value) {
    const numeric = value.replace(/\D/g, "").slice(0, 11);

    return numeric
        .replace(/(\d{3})(\d)/, "$1.$2")
        .replace(/(\d{3})(\d)/, "$1.$2")
        .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
}

export function isValidCpf(cpf) {
    return cpf.length === 11;
};
