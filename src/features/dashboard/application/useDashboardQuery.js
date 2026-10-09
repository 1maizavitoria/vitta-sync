import { useConsultar } from "../infrastructure/generated/dashboardApi";

const DASHBOARD_STALE_TIME = 60 * 1000;
const DASHBOARD_CACHE_TIME = 5 * 60 * 1000;

function shouldRetryDashboardRequest(failureCount, error) {
    const status = error?.response?.status;

    if (status && status < 500) {
        return false;
    }

    return failureCount < 1;
}

export function useDashboardQuery({ cpf, inicio, fim, categorias }) {
    const query = useConsultar(
        cpf || "",
        {
            inicio,
            fim,
            ...(categorias ? { categorias } : {})
        },
        {
            query: {
                enabled: Boolean(cpf),
                staleTime: DASHBOARD_STALE_TIME,
                gcTime: DASHBOARD_CACHE_TIME,
                retry: shouldRetryDashboardRequest
            }
        }
    );

    return {
        dashboard: query.data ?? null,
        loading: Boolean(cpf) && query.isPending,
        error: query.isError,
        errorStatus: query.error?.response?.status ?? null,
        networkError: query.isError && !query.error?.response,
        refreshing: query.isFetching && !query.isPending
    };
}
