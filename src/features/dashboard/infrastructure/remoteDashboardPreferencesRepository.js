import api from "../../../../services/api";
import { normalizeDashboardLayout } from "../domain/dashboardLayout";

const LEGACY_STORAGE_PREFIX = "vitta-dashboard-layout";

function legacyStorageKey(userId) {
    return `${LEGACY_STORAGE_PREFIX}:${userId || "anonymous"}`;
}

function readLegacyLayout(key) {
    const stored = localStorage.getItem(key);
    if (!stored) return null;

    try {
        return normalizeDashboardLayout(JSON.parse(stored));
    } catch {
        return null;
    }
}

export function createRemoteDashboardPreferencesRepository(userId) {
    const legacyKey = legacyStorageKey(userId);

    return {
        async load() {
            const response = await api.get("/preferencias-dashboard");

            if (response.status !== 204 && response.data) {
                return normalizeDashboardLayout(response.data);
            }

            const legacyLayout = readLegacyLayout(legacyKey);
            if (!legacyLayout) return normalizeDashboardLayout(null);

            const migrationResponse = await api.put("/preferencias-dashboard", legacyLayout);
            localStorage.removeItem(legacyKey);
            return normalizeDashboardLayout(migrationResponse.data);
        },

        async save(layout) {
            const normalized = normalizeDashboardLayout(layout);
            const response = await api.put("/preferencias-dashboard", normalized);
            localStorage.removeItem(legacyKey);
            return normalizeDashboardLayout(response.data);
        },

        async reset() {
            await api.delete("/preferencias-dashboard");
            localStorage.removeItem(legacyKey);
            return normalizeDashboardLayout(null);
        }
    };
}
