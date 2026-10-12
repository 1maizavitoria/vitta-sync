import { useConsultar } from "../infrastructure/generated/dashboardApi";
import { classifyApiError } from "../../../../utils/api/classifyApiError";

const DASHBOARD_STALE_TIME = 60 * 1000;
const DASHBOARD_CACHE_TIME = 5 * 60 * 1000;

function shouldRetryDashboardRequest(failureCount, error) {
    return classifyApiError(error).retryable && failureCount < 1;
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
    const apiError = query.isError ? classifyApiError(query.error) : null;

    return {
        dashboard: query.data ?? null,
        loading: Boolean(cpf) && query.isPending,
        error: query.isError,
        errorType: apiError?.type ?? null,
        errorStatus: apiError?.status ?? null,
        refreshing: query.isFetching && !query.isPending
    };
}
