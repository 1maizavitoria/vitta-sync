import { formatNumber } from "./formatNumber";

export function formatCurrency(value, locale = "pt-BR", currency = "BRL", options = {}) {
    return formatNumber(value, locale, {
        style: "currency",
        currency,
        ...options
    });
}
