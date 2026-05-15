export function daysMap() {
    return {
        MONDAY: "Segunda-feira",
        TUESDAY: "Terça-feira",
        WEDNESDAY: "Quarta-feira",
        THURSDAY: "Quinta-feira",
        FRIDAY: "Sexta-feira",
        SATURDAY: "Sábado",
        SUNDAY: "Domingo",
    }
};

// export function formatDate(dateString) {
//     const [year, month, day] = dateString.split('-');

//     return `${day}/${month}/${year}`;
// }

export function formatDate(dateString) {

    if (!dateString) {
        return "";
    }

    const date =
        new Date(dateString);

    return date.toLocaleDateString("pt-BR");
}