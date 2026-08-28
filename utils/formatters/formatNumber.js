export function formatNumber(value, locale = "pt-BR", options = {}) {
    if (value == null || value === "") return "";

    const number = Number(value);
    if (Number.isNaN(number)) return String(value);

    return new Intl.NumberFormat(locale, options).format(number);
}

export function formatPercent(value, locale = "pt-BR", options = {}) {
    return formatNumber(value, locale, {
        maximumFractionDigits: 0,
        ...options
    });
}

export function formatMeasurement(value, unit, locale = "pt-BR", options = {}) {
    const formattedValue = formatNumber(value, locale, options);
    return unit ? `${formattedValue} ${unit}` : formattedValue;
}
