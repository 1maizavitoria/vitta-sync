export function classifyApiError(error) {
    const status = error?.response?.status ?? null;

    if (status === null) {
        return {
            type: "network",
            status: null,
            retryable: true
        };
    }

    const typeByStatus = {
        400: "invalidRequest",
        401: "unauthorized",
        403: "forbidden",
        404: "notFound",
        409: "conflict"
    };

    return {
        type: typeByStatus[status] || (status >= 500 ? "server" : "unknown"),
        status,
        retryable: status >= 500
    };
}
