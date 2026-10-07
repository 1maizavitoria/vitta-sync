import { useCallback, useEffect, useState } from "react";

import {
    DEFAULT_DASHBOARD_LAYOUT,
    moveDashboardWidget,
    normalizeDashboardLayout,
    setWidgetVisibility
} from "../domain/dashboardLayout";

export function useDashboardPreferences(repository) {
    const [layout, setLayout] = useState(DEFAULT_DASHBOARD_LAYOUT);
    const [draft, setDraft] = useState(DEFAULT_DASHBOARD_LAYOUT);
    const [open, setOpen] = useState(false);
    const [loadingPreferences, setLoadingPreferences] = useState(true);
    const [savingPreferences, setSavingPreferences] = useState(false);
    const [preferenceError, setPreferenceError] = useState(false);

    useEffect(() => {
        let active = true;

        async function loadPreferences() {
            setLoadingPreferences(true);
            setPreferenceError(false);

            try {
                const storedLayout = await repository.load();
                if (active) {
                    const normalized = normalizeDashboardLayout(storedLayout);
                    setLayout(normalized);
                    setDraft(normalized);
                }
            } catch {
                if (active) {
                    setLayout(DEFAULT_DASHBOARD_LAYOUT);
                    setDraft(DEFAULT_DASHBOARD_LAYOUT);
                    setPreferenceError(true);
                }
            } finally {
                if (active) setLoadingPreferences(false);
            }
        }

        loadPreferences();

        return () => {
            active = false;
        };
    }, [repository]);

    const openCustomization = useCallback(() => {
        setDraft(layout);
        setPreferenceError(false);
        setOpen(true);
    }, [layout]);

    const cancelCustomization = useCallback(() => {
        setDraft(layout);
        setPreferenceError(false);
        setOpen(false);
    }, [layout]);

    const toggleWidget = useCallback((widgetId, visible) => {
        setDraft((current) => setWidgetVisibility(current, widgetId, visible));
    }, []);

    const moveWidget = useCallback((widgetId, direction) => {
        setDraft((current) => moveDashboardWidget(current, widgetId, direction));
    }, []);

    const restoreDefault = useCallback(() => {
        setDraft(normalizeDashboardLayout(null));
        setPreferenceError(false);
    }, []);

    const saveCustomization = useCallback(async () => {
        setSavingPreferences(true);
        setPreferenceError(false);

        try {
            const savedLayout = await repository.save(draft);
            const normalized = normalizeDashboardLayout(savedLayout);
            setLayout(normalized);
            setDraft(normalized);
            setOpen(false);
            return true;
        } catch {
            setPreferenceError(true);
            return false;
        } finally {
            setSavingPreferences(false);
        }
    }, [draft, repository]);

    return {
        layout,
        draft,
        open,
        loadingPreferences,
        savingPreferences,
        preferenceError,
        openCustomization,
        cancelCustomization,
        toggleWidget,
        moveWidget,
        restoreDefault,
        saveCustomization
    };
}
