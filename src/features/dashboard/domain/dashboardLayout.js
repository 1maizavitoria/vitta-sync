export const DASHBOARD_WIDGETS = [
    { id: "stability", labelKey: "dashboard.customization.widgets.stability" },
    { id: "baseline", labelKey: "dashboard.customization.widgets.baseline" },
    { id: "latestValues", labelKey: "dashboard.customization.widgets.latestValues" },
    { id: "charts", labelKey: "dashboard.customization.widgets.charts" },
    { id: "timeline", labelKey: "dashboard.customization.widgets.timeline" }
];

export const DEFAULT_DASHBOARD_LAYOUT = {
    version: 1,
    widgets: DASHBOARD_WIDGETS.map(({ id }) => ({ id, visible: true }))
};

export function normalizeDashboardLayout(layout) {
    const availableIds = new Set(DASHBOARD_WIDGETS.map(({ id }) => id));
    const source = Array.isArray(layout?.widgets) ? layout.widgets : [];
    const normalized = [];
    const includedIds = new Set();

    source.forEach((widget) => {
        if (!availableIds.has(widget?.id) || includedIds.has(widget.id)) return;

        normalized.push({
            id: widget.id,
            visible: widget.visible !== false
        });
        includedIds.add(widget.id);
    });

    DASHBOARD_WIDGETS.forEach(({ id }) => {
        if (!includedIds.has(id)) {
            normalized.push({ id, visible: true });
        }
    });

    if (!normalized.some((widget) => widget.visible) && normalized.length > 0) {
        normalized[0] = { ...normalized[0], visible: true };
    }

    return {
        version: DEFAULT_DASHBOARD_LAYOUT.version,
        widgets: normalized
    };
}

export function setWidgetVisibility(layout, widgetId, visible) {
    const normalized = normalizeDashboardLayout(layout);
    const nextWidgets = normalized.widgets.map((widget) =>
        widget.id === widgetId ? { ...widget, visible } : widget
    );

    if (!nextWidgets.some((widget) => widget.visible)) {
        return normalized;
    }

    return { ...normalized, widgets: nextWidgets };
}

export function moveDashboardWidget(layout, widgetId, direction) {
    const normalized = normalizeDashboardLayout(layout);
    const currentIndex = normalized.widgets.findIndex((widget) => widget.id === widgetId);
    const targetIndex = direction === "up" ? currentIndex - 1 : currentIndex + 1;

    if (currentIndex < 0 || targetIndex < 0 || targetIndex >= normalized.widgets.length) {
        return normalized;
    }

    const widgets = [...normalized.widgets];
    [widgets[currentIndex], widgets[targetIndex]] = [widgets[targetIndex], widgets[currentIndex]];

    return { ...normalized, widgets };
}

