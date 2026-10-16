import { useMemo, useState } from "react";

import { useClinicalTimelineQuery } from "../application/useClinicalTimelineQuery";
import { useDashboardPreferences } from "../application/useDashboardPreferences";
import { useDashboardQuery } from "../application/useDashboardQuery";

function toApiDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");

    return `${year}-${month}-${day}`;
}

function getDateRange(days) {
    const end = new Date();
    const start = new Date();
    start.setDate(end.getDate() - (days - 1));

    return {
        inicio: toApiDate(start),
        fim: toApiDate(end)
    };
}

function isValidDateRange({ inicio, fim }) {
    const today = toApiDate(new Date());

    return Boolean(inicio && fim && inicio <= fim && fim <= today);
}

function getPeriodDays({ inicio, fim }) {
    if (!inicio || !fim) return 0;

    const start = new Date(`${inicio}T00:00:00`);
    const end = new Date(`${fim}T00:00:00`);

    return Math.floor((end - start) / 86400000) + 1;
}

export function useDashboardController({ cpf, preferencesRepository }) {
    const [period, setPeriod] = useState(7);
    const [customDateRange, setCustomDateRange] = useState(() => getDateRange(7));
    const [categoryFilter, setCategoryFilter] = useState("todas");
    const dateRange = useMemo(
        () => period === "custom" ? customDateRange : getDateRange(period),
        [customDateRange, period]
    );
    const dateRangeValid = isValidDateRange(dateRange);
    const periodDays = dateRangeValid ? getPeriodDays(dateRange) : 0;
    const dashboardPreferences = useDashboardPreferences(preferencesRepository);
    const dashboardQuery = useDashboardQuery({
        cpf,
        ...dateRange,
        categorias: categoryFilter === "todas" ? undefined : categoryFilter,
        enabled: dateRangeValid
    });
    const clinicalTimelineQuery = useClinicalTimelineQuery({
        cpf,
        ...dateRange,
        enabled: dateRangeValid
    });

    function updateCustomDateRange(field, value) {
        setCustomDateRange((current) => ({ ...current, [field]: value }));
    }

    return {
        ...dashboardQuery,
        clinicalTimeline: clinicalTimelineQuery,
        dashboardPreferences,
        period,
        periodDays,
        setPeriod,
        customDateRange,
        updateCustomDateRange,
        dateRangeValid,
        categoryFilter,
        setCategoryFilter
    };
}
