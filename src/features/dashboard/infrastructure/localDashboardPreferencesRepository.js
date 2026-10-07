import { normalizeDashboardLayout } from "../domain/dashboardLayout";

const STORAGE_PREFIX = "vitta-dashboard-layout";

function storageKey(userId) {
    return `${STORAGE_PREFIX}:${userId || "anonymous"}`;
}

export function createLocalDashboardPreferencesRepository(userId) {
    const key = storageKey(userId);

    return {
        async load() {
            try {
                const stored = localStorage.getItem(key);
                return normalizeDashboardLayout(stored ? JSON.parse(stored) : null);
            } catch {
                return normalizeDashboardLayout(null);
            }
        },

        async save(layout) {
            const normalized = normalizeDashboardLayout(layout);
            localStorage.setItem(key, JSON.stringify(normalized));
            return normalized;
        },

        async reset() {
            localStorage.removeItem(key);
            return normalizeDashboardLayout(null);
        }
    };
}

