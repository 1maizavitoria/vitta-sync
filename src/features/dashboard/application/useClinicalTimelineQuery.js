import { useQuery } from "@tanstack/react-query";

import { getClinicalTimeline } from "../../../../services/timelineService";
import { classifyApiError } from "../../../../utils/api/classifyApiError";

const TIMELINE_STALE_TIME = 60 * 1000;
const TIMELINE_CACHE_TIME = 5 * 60 * 1000;

function shouldRetryTimelineRequest(failureCount, error) {
    return classifyApiError(error).retryable && failureCount < 1;
}

export function useClinicalTimelineQuery({ cpf, inicio, fim, enabled = true }) {
    const query = useQuery({
        queryKey: ["clinical-timeline", cpf, inicio, fim],
        queryFn: ({ signal }) => getClinicalTimeline({ cpf, inicio, fim, signal }),
        enabled: Boolean(cpf) && enabled,
        staleTime: TIMELINE_STALE_TIME,
        gcTime: TIMELINE_CACHE_TIME,
        retry: shouldRetryTimelineRequest
    });
    const apiError = query.isError ? classifyApiError(query.error) : null;

    return {
        items: query.data?.itens ?? [],
        loading: Boolean(cpf) && enabled && query.isPending,
        error: query.isError,
        errorType: apiError?.type ?? null,
        errorStatus: apiError?.status ?? null,
        refreshing: query.isFetching && !query.isPending
    };
}
