import { defineConfig } from "orval";

export default defineConfig({
    dashboard: {
        input: {
            target: "http://localhost:8080/v3/api-docs",
            override: {
                transformer: (specification) => {
                    const bearerAuth = specification.components?.securitySchemes?.bearerAuth;

                    if (bearerAuth?.type === "http") {
                        delete bearerAuth.name;
                    }

                    return specification;
                },
            },
            filters: {
                mode: "include",
                tags: ["dashboard-controller"],
            },
        },
        output: {
            target: "./src/features/dashboard/infrastructure/generated/dashboardApi.ts",
            schemas: "./src/features/dashboard/infrastructure/generated/models",
            client: "react-query",
            httpClient: "axios",
            clean: true,
            override: {
                mutator: {
                    path: "./services/api.js",
                    name: "customInstance",
                },
                query: {
                    useQuery: true,
                    signal: true,
                },
            },
        },
    },
});
