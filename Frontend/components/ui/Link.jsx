import { Link as RouterLink } from "react-router-dom";

export default function Link({ to, href, onClick, children, ...props }) {
    if (to) {
        return (
            <RouterLink to={to} {...props}>
                {children}
            </RouterLink>
        );
    }

    if (href) {
        return (
            <a href={href} {...props}>
                {children}
            </a>
        );
    }

    if (onClick) {
        return (
            <button
                onClick={onClick}
                style={{
                    background: "none",
                    border: "none",
                    padding: 0,
                    color: "blue",
                    cursor: "pointer",
                    textDecoration: "underline"
                }}
                {...props}
            >

                {children}
            </button>
        )
    }

    return <span>{children}</span>;
}