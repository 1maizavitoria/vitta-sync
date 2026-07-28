import { Link as RouterLink } from "react-router-dom";

const baseStyle = {
    fontFamily: 'Inter, sans-serif',
    fontSize: '14px',
    color: '#50635f',
};

const actionStyle = {
    color: '#16a34a',
    fontWeight: 700,
    cursor: 'pointer',
    marginLeft: '4px',
    transition: 'all 0.2s ease',
    textDecoration: 'none'
};

export default function LinkUI({ to, href, onClick, children, variant = "default", ...props }) {
    const style = {
        ...baseStyle,
        ...(variant === "action" ? actionStyle : {}),
    };

    if (to) {
        return (
            <RouterLink to={to} style={style} {...props}>
                {children}
            </RouterLink>
        );
    }

    if (href) {
        return (
            <a href={href} style={style} {...props}>
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
                    color: "#16a34a",
                    cursor: "pointer",
                    textDecoration: "none",
                    ...style,
                }}
                {...props}
            >
                {children}
            </button>
        )
    }

    return <span>{children}</span>;
}
