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

export function useDashboardController({ cpf, preferencesRepository }) {
    const [period, setPeriod] = useState(7);
    const [categoryFilter, setCategoryFilter] = useState("todas");
    const dateRange = useMemo(() => getDateRange(period), [period]);
    const dashboardPreferences = useDashboardPreferences(preferencesRepository);
    const dashboardQuery = useDashboardQuery({
        cpf,
        ...dateRange,
        categorias: categoryFilter === "todas" ? undefined : categoryFilter
    });
    const clinicalTimelineQuery = useClinicalTimelineQuery({
        cpf,
        ...dateRange
    });

    return {
        ...dashboardQuery,
        clinicalTimeline: clinicalTimelineQuery,
        dashboardPreferences,
        period,
        setPeriod,
        categoryFilter,
        setCategoryFilter
    };
}
