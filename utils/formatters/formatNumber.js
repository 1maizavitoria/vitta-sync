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

function normalizeUnit(unit) {
    return String(unit || "").trim().toLowerCase();
}

export function getDisplayUnit(unit, measurementSystem = "metric") {
    if (measurementSystem !== "imperial") return unit;

    const normalizedUnit = normalizeUnit(unit);

    if (normalizedUnit === "kg") return "lb";
    if (normalizedUnit === "°c" || normalizedUnit === "c" || normalizedUnit === "celsius") return "°F";
    if (normalizedUnit === "m" || normalizedUnit === "metro" || normalizedUnit === "meter") return "in";
    if (normalizedUnit === "cm" || normalizedUnit === "centimetro" || normalizedUnit === "centimeter") return "in";

    return unit;
}

export function convertMeasurementValue(value, unit, measurementSystem = "metric") {
    const number = Number(value);

    if (Number.isNaN(number) || measurementSystem !== "imperial") {
        return value;
    }

    const normalizedUnit = normalizeUnit(unit);

    if (normalizedUnit === "kg") return number * 2.2046226218;
    if (normalizedUnit === "°c" || normalizedUnit === "c" || normalizedUnit === "celsius") return (number * 9 / 5) + 32;
    if (normalizedUnit === "m" || normalizedUnit === "metro" || normalizedUnit === "meter") return number * 39.37007874;
    if (normalizedUnit === "cm" || normalizedUnit === "centimetro" || normalizedUnit === "centimeter") return number / 2.54;

    return value;
}

export function convertMeasurementToMetricValue(value, unit, measurementSystem = "metric") {
    const number = Number(value);

    if (Number.isNaN(number) || measurementSystem !== "imperial") {
        return value;
    }

    const normalizedUnit = normalizeUnit(unit);

    if (normalizedUnit === "kg") return number / 2.2046226218;
    if (normalizedUnit === "°c" || normalizedUnit === "c" || normalizedUnit === "celsius") return (number - 32) * 5 / 9;
    if (normalizedUnit === "m" || normalizedUnit === "metro" || normalizedUnit === "meter") return number / 39.37007874;
    if (normalizedUnit === "cm" || normalizedUnit === "centimetro" || normalizedUnit === "centimeter") return number * 2.54;

    return value;
}

export function formatMeasurement(value, unit, locale = "pt-BR", options = {}, measurementSystem = "metric") {
    const convertedValue = convertMeasurementValue(value, unit, measurementSystem);
    const displayUnit = getDisplayUnit(unit, measurementSystem);
    const formattedValue = formatNumber(convertedValue, locale, {
        maximumFractionDigits: 1,
        ...options
    });

    return displayUnit ? `${formattedValue} ${displayUnit}` : formattedValue;
}
