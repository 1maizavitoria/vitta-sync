import {
    Box,
    Button,
    Typography,
    Paper,
    Grid
} from "@mui/material";

import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import MonitorHeartOutlinedIcon from "@mui/icons-material/MonitorHeartOutlined";
import OpacityOutlinedIcon from "@mui/icons-material/OpacityOutlined";
import DeviceThermostatOutlinedIcon from "@mui/icons-material/DeviceThermostatOutlined";
import AirOutlinedIcon from "@mui/icons-material/AirOutlined";
import DescriptionOutlinedIcon from "@mui/icons-material/DescriptionOutlined";

import ShieldOutlinedIcon from "@mui/icons-material/ShieldOutlined";
import GroupOutlinedIcon from "@mui/icons-material/GroupOutlined";
import AccessTimeOutlinedIcon from "@mui/icons-material/AccessTimeOutlined";

import FacebookOutlinedIcon from "@mui/icons-material/FacebookOutlined";
import TwitterIcon from "@mui/icons-material/Twitter";
import InstagramIcon from "@mui/icons-material/Instagram";
import LinkedInIcon from "@mui/icons-material/LinkedIn";

import EmailOutlinedIcon from "@mui/icons-material/EmailOutlined";
import PhoneOutlinedIcon from "@mui/icons-material/PhoneOutlined";
import LocationOnOutlinedIcon from "@mui/icons-material/LocationOnOutlined";

export default function Landing() {

    const cards = [
        {
            icon: <MonitorHeartOutlinedIcon />,
            title: "Pressão Arterial",
            description:
                "Monitore sua pressão sistólica e diastólica com registros precisos e histórico completo."
        },
        {
            icon: <FavoriteBorderIcon />,
            title: "Frequência Cardíaca",
            description:
                "Acompanhe seus batimentos cardíacos em repouso e durante atividades físicas."
        },
        {
            icon: <OpacityOutlinedIcon />,
            title: "Saturação de Oxigênio (SpO2)",
            description:
                "Registre os níveis de oxigênio no sangue para um acompanhamento respiratório completo."
        },
        {
            icon: <DeviceThermostatOutlinedIcon />,
            title: "Temperatura Corporal",
            description:
                "Mantenha um histórico preciso da sua temperatura para identificar padrões e variações."
        },
        {
            icon: <AirOutlinedIcon />,
            title: "Frequência Respiratória",
            description:
                "Monitore sua respiração por minuto com dados estatísticos e análises de tendências."
        },
        {
            icon: <DescriptionOutlinedIcon />,
            title: "Relatórios Clínicos",
            description:
                "Exporte relatórios profissionais em PDF para compartilhar com seus médicos e especialistas."
        }
    ];

    const benefits = [
        {
            icon: <ShieldOutlinedIcon />,
            title: "Privacidade e Segurança",
            description:
                "Seus dados são criptografados e você controla quem tem acesso."
        },
        {
            icon: <GroupOutlinedIcon />,
            title: "Vínculo com Médicos",
            description:
                "Conecte-se diretamente com profissionais de saúde."
        },
        {
            icon: <AccessTimeOutlinedIcon />,
            title: "Monitoramento Contínuo",
            description:
                "Acompanhamento 24/7 com alertas personalizados."
        }
    ];

    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center"
            }}
        >

            {/* health monitoring */}
            <Box
                sx={{
                    width: "100%",
                    maxWidth: "1400px",
                    display: "flex",
                    // gap: 8,
                    alignItems: "center",
                    justifyContent: "space-between"
                }}
            >

                {/* LEFT */}
                <Box sx={{ flex: 1 }}>

                    {/* Badge */}
                    <Box
                        sx={{
                            display: "inline-flex",
                            alignItems: "center",
                            bgcolor: "#d8efc7",
                            px: 2,
                            py: 1,
                            borderRadius: "999px",
                            mb: 5
                        }}
                    >
                        <Typography
                            sx={{
                                fontSize: 14,
                                fontWeight: 600,
                                color: "#4c8c42"
                            }}
                        >
                            ✨ Monitoramento Preciso de Saúde
                        </Typography>
                    </Box>

                    {/* Title */}
                    <Typography
                        sx={{
                            fontSize: {
                                xs: 42,
                                md: 72
                            },
                            lineHeight: 1.05,
                            fontWeight: 800,
                            color: "#08142b",
                            maxWidth: "620px",
                            mb: 4
                        }}
                    >
                        Sua saúde em{" "}
                        <Box
                            component="span"
                            sx={{ color: "#4c9a45" }}
                        >
                            sincronia
                        </Box>{" "}
                        com você
                    </Typography>

                    {/* Description */}
                    <Typography
                        sx={{
                            fontSize: 22,
                            color: "#5f6b7a",
                            lineHeight: 1.7,
                            maxWidth: "700px",
                            mb: 5
                        }}
                    >
                        Monitore seus sinais vitais em tempo real,
                        receba alertas personalizados e compartilhe
                        dados com seus médicos de forma segura e sincronizada.
                    </Typography>

                    {/* Buttons */}
                    <Box
                        sx={{
                            display: "flex",
                            gap: 2,
                            mb: 7
                        }}
                    >

                        <Button
                            variant="contained"
                            sx={{
                                bgcolor: "#b7d995",
                                color: "#08142b",
                                px: 4,
                                py: 2,
                                borderRadius: "16px",
                                textTransform: "none",
                                fontWeight: 700,
                                fontSize: 18,
                                boxShadow: "none",
                                "&:hover": {
                                    bgcolor: "#a7ca84",
                                    boxShadow: "none"
                                }
                            }}
                        >
                            Começar Agora →
                        </Button>

                        <Button
                            variant="outlined"
                            sx={{
                                borderColor: "#d6d6d6",
                                color: "#08142b",
                                px: 4,
                                py: 2,
                                borderRadius: "16px",
                                textTransform: "none",
                                fontWeight: 600,
                                fontSize: 18
                            }}
                        >
                            ▶ Ver Demonstração
                        </Button>

                    </Box>

                    {/* Stats */}
                    <Box
                        sx={{
                            display: "flex",
                            gap: 8,
                            pt: 4,
                            borderTop: "1px solid #dddddd"
                        }}
                    >

                        {[
                            ["10K+", "Usuários Ativos"],
                            ["500+", "Médicos Parceiros"],
                            ["99.9%", "Uptime"]
                        ].map(([value, label]) => (
                            <Box key={label}>
                                <Typography
                                    sx={{
                                        fontSize: 42,
                                        fontWeight: 800,
                                        color: "#08142b"
                                    }}
                                >
                                    {value}
                                </Typography>

                                <Typography
                                    sx={{
                                        color: "#6f7785"
                                    }}
                                >
                                    {label}
                                </Typography>
                            </Box>
                        ))}

                    </Box>

                </Box>

                {/* RIGHT */}
                <Paper
                    elevation={0}
                    sx={{
                        width: 520,
                        borderRadius: "28px",
                        p: 4,
                        bgcolor: "white",
                        boxShadow: "0 20px 50px rgba(0,0,0,0.10)"
                    }}
                >

                    <Typography
                        sx={{
                            fontSize: 28,
                            fontWeight: 700,
                            color: "#08142b",
                            mb: 1
                        }}
                    >
                        Frequência Cardíaca
                    </Typography>

                    <Typography
                        sx={{
                            color: "#6c7480",
                            mb: 4
                        }}
                    >
                        Últimos 7 dias - Tendência Semanal
                    </Typography>

                    {/* KPI */}
                    <Box
                        sx={{
                            background:
                                "linear-gradient(90deg, #c6eee6 0%, #b6d98e 100%)",
                            borderRadius: "20px",
                            p: 3,
                            mb: 4
                        }}
                    >

                        <Typography
                            sx={{
                                fontSize: 56,
                                fontWeight: 800,
                                color: "#08142b"
                            }}
                        >
                            72
                            <Box
                                component="span"
                                sx={{
                                    fontSize: 24,
                                    ml: 1,
                                    fontWeight: 500
                                }}
                            >
                                bpm
                            </Box>
                        </Typography>

                        <Typography
                            sx={{
                                color: "#42505e"
                            }}
                        >
                            Valor atual - Normal
                        </Typography>

                    </Box>

                    {/* Fake Chart */}
                    <Box
                        sx={{
                            height: 240,
                            borderRadius: "20px",
                            bgcolor: "#fafafa",
                            mb: 4,
                            position: "relative",
                            overflow: "hidden"
                        }}
                    >

                        {/* linhas */}
                        {[1, 2, 3, 4].map((i) => (
                            <Box
                                key={i}
                                sx={{
                                    position: "absolute",
                                    left: 0,
                                    right: 0,
                                    top: `${i * 20}%`,
                                    borderTop: "1px dashed #d8d8d8"
                                }}
                            />
                        ))}

                        {/* linha fake */}
                        <svg
                            width="100%"
                            height="100%"
                            viewBox="0 0 500 240"
                            style={{
                                position: "absolute",
                                inset: 0
                            }}
                        >
                            <path
                                d="M20 140
                                   C 80 110, 120 160, 180 135
                                   S 280 90, 340 130
                                   S 430 145, 480 110"
                                fill="none"
                                stroke="#a6d38c"
                                strokeWidth="4"
                                strokeLinecap="round"
                            />
                        </svg>

                    </Box>

                    {/* Bottom stats */}
                    <Box
                        sx={{
                            display: "flex",
                            gap: 2
                        }}
                    >

                        {[
                            ["Mínimo", "65"],
                            ["Média", "72"],
                            ["Máximo", "81"]
                        ].map(([label, value]) => (
                            <Box
                                key={label}
                                sx={{
                                    flex: 1,
                                    bgcolor: "#f8f8f8",
                                    borderRadius: "18px",
                                    py: 3,
                                    textAlign: "center"
                                }}
                            >

                                <Typography
                                    sx={{
                                        color: "#7b8592",
                                        mb: 1
                                    }}
                                >
                                    {label}
                                </Typography>

                                <Typography
                                    sx={{
                                        fontSize: 34,
                                        fontWeight: 800,
                                        color: "#08142b"
                                    }}
                                >
                                    {value}
                                </Typography>

                            </Box>
                        ))}

                    </Box>

                </Paper>

            </Box>

            {/* 5 vital signs */}
            <Box
                sx={{
                    pt: 12
                }}
            >

                {/* Header */}
                <Box
                    sx={{
                        textAlign: "center",
                        mb: 8
                    }}
                >

                    <Typography
                        sx={{
                            fontSize: {
                                xs: 36,
                                md: 58
                            },
                            fontWeight: 800,
                            color: "#08142b",
                            lineHeight: 1.1,
                            mb: 2
                        }}
                    >
                        Monitoramento Completo dos{" "}
                        <Box
                            component="span"
                            sx={{
                                color: "#5da14f"
                            }}
                        >
                            5 Sinais Vitais
                        </Box>
                    </Typography>

                    <Typography sx={{ fontSize: 22, color: "#5f6b7a" }} >
                        Registre, analise e compartilhe seus dados de saúde
                        com precisão médica
                    </Typography>

                </Box>


                <Box
                    sx={{
                        width: "100%",
                        maxWidth: "1400px",
                        mx: "auto",
                        display: "grid",
                        gridTemplateColumns: {
                            xs: "1fr",
                            md: "repeat(5, 1fr)"
                        },
                        gap: 3
                    }}
                >
                    {cards.map((card) => (
                        <Grid
                            item
                            xs={12}
                            sm={6}
                            md={4}
                            key={card.title}
                        >

                            <Paper
                                elevation={0}
                                sx={{
                                    p: 3,
                                    borderRadius: "24px",
                                    border: "1px solid #e7e7e7",
                                    transition: "0.25s ease",
                                    cursor: "pointer",
                                    minHeight: 260,

                                    "&:hover": {
                                        transform: "translateY(-4px)",
                                        boxShadow:
                                            "0 10px 25px rgba(0,0,0,0.06)"
                                    }
                                }}
                            >

                                {/* Icon */}
                                <Box
                                    sx={{
                                        width: 56,
                                        height: 56,
                                        borderRadius: "16px",
                                        background:
                                            "linear-gradient(135deg, #c7ebd8 0%, #b4d89d 100%)",
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center",
                                        color: "white",
                                        mb: 3,

                                        "& svg": {
                                            fontSize: 28
                                        }
                                    }}
                                >
                                    {card.icon}
                                </Box>

                                {/* Title */}
                                <Typography
                                    sx={{
                                        fontSize: 18,
                                        fontWeight: 700,
                                        color: "#08142b",
                                        mb: 2,
                                        lineHeight: 1.3
                                    }}
                                >
                                    {card.title}
                                </Typography>

                                {/* Description */}
                                <Typography
                                    sx={{
                                        color: "#5f6b7a",
                                        lineHeight: 1.7,
                                        fontSize: 15
                                    }}
                                >
                                    {card.description}
                                </Typography>

                            </Paper>

                        </Grid>
                    ))}
                </Box>


            </Box>

            {/* BENEFITS */}
            <Box
                sx={{
                    width: "100%",
                    py: 8
                }}
            >

                <Box
                    sx={{
                        width: "100%",
                        maxWidth: "1200px",
                        mx: "auto",
                        display: "grid",
                        gridTemplateColumns: {
                            xs: "1fr",
                            md: "repeat(3, 1fr)"
                        },
                        gap: 3
                    }}
                >

                    {benefits.map((item) => (
                        <Paper
                            key={item.title}
                            elevation={0}
                            sx={{
                                p: 3,
                                borderRadius: "20px",
                                border: "1px solid #dce7dc",
                                display: "flex",
                                alignItems: "flex-start",
                                gap: 2
                            }}
                        >

                            {/* Icon */}
                            <Box
                                sx={{
                                    minWidth: 48,
                                    width: 48,
                                    height: 48,
                                    borderRadius: "14px",
                                    background:
                                        "linear-gradient(135deg, #c7ebd8 0%, #b4d89d 100%)",
                                    display: "flex",
                                    alignItems: "center",
                                    justifyContent: "center",
                                    color: "white",

                                    "& svg": {
                                        fontSize: 24
                                    }
                                }}
                            >
                                {item.icon}
                            </Box>

                            {/* Content */}
                            <Box>

                                <Typography
                                    sx={{
                                        fontWeight: 700,
                                        fontSize: 18,
                                        color: "#08142b",
                                        mb: 1
                                    }}
                                >
                                    {item.title}
                                </Typography>

                                <Typography
                                    sx={{
                                        color: "#5f6b7a",
                                        lineHeight: 1.6,
                                        fontSize: 15
                                    }}
                                >
                                    {item.description}
                                </Typography>

                            </Box>

                        </Paper>
                    ))}

                </Box>

            </Box>

            {/* FOOTER */}
            <Box
                component="footer"
                sx={{
                    width: "100%",
                    bgcolor: "#07152d",
                    color: "white",
                    px: 3,
                    py: 8,
                }}
            >

                <Box
                    sx={{
                        width: "100%",
                        maxWidth: "1200px",
                        mx: "auto"
                    }}
                >

                    {/* TOP */}
                    <Box
                        sx={{
                            display: "grid",
                            gridTemplateColumns: {
                                xs: "1fr",
                                sm: "repeat(2, 1fr)",
                                md: "2fr 1fr 1fr 1.3fr"
                            },
                            gap: 6,
                            mb: 6
                        }}
                    >

                        {/* BRAND */}
                        <Box>

                            <Box
                                sx={{
                                    display: "flex",
                                    alignItems: "center",
                                    gap: 1.5,
                                    mb: 3
                                }}
                            >

                                <Box
                                    sx={{
                                        width: 38,
                                        height: 38,
                                        borderRadius: "12px",
                                        background:
                                            "linear-gradient(135deg, #c7ebd8 0%, #b4d89d 100%)",
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "center"
                                    }}
                                >
                                    <MonitorHeartOutlinedIcon />
                                </Box>

                                <Typography
                                    sx={{
                                        fontSize: 34,
                                        fontWeight: 800
                                    }}
                                >
                                    VittaSync
                                </Typography>

                            </Box>

                            <Typography
                                sx={{
                                    color: "#a7b3c5",
                                    lineHeight: 1.8,
                                    maxWidth: 320,
                                    mb: 4
                                }}
                            >
                                Monitoramento preciso de saúde
                                que sincroniza com seu estilo de vida.
                            </Typography>

                            {/* Social */}
                            <Box
                                sx={{
                                    display: "flex",
                                    gap: 1.5
                                }}
                            >

                                {[
                                    <FacebookOutlinedIcon />,
                                    <TwitterIcon />,
                                    <InstagramIcon />,
                                    <LinkedInIcon />
                                ].map((icon, index) => (
                                    <Box
                                        key={index}
                                        sx={{
                                            width: 42,
                                            height: 42,
                                            borderRadius: "12px",
                                            bgcolor: "rgba(255,255,255,0.08)",
                                            display: "flex",
                                            alignItems: "center",
                                            justifyContent: "center",
                                            cursor: "pointer",
                                            transition: "0.2s",

                                            "&:hover": {
                                                bgcolor: "rgba(255,255,255,0.16)"
                                            }
                                        }}
                                    >
                                        {icon}
                                    </Box>
                                ))}

                            </Box>

                        </Box>

                        {/* LINKS */}
                        <Box>

                            <Typography
                                sx={{
                                    fontWeight: 700,
                                    mb: 3,
                                    fontSize: 18
                                }}
                            >
                                Links Rápidos
                            </Typography>

                            {[
                                "Funcionalidades",
                                "Monitoramento",
                                "Análise",
                                "Relatórios",
                                "Sobre Nós"
                            ].map((item) => (
                                <Typography
                                    key={item}
                                    sx={{
                                        color: "#d4d9e2",
                                        mb: 2,
                                        cursor: "pointer",
                                        "&:hover": {
                                            color: "white"
                                        }
                                    }}
                                >
                                    {item}
                                </Typography>
                            ))}

                        </Box>

                        {/* SUPORTE */}
                        <Box>

                            <Typography
                                sx={{
                                    fontWeight: 700,
                                    mb: 3,
                                    fontSize: 18
                                }}
                            >
                                Suporte
                            </Typography>

                            {[
                                "Central de Ajuda",
                                "Documentação",
                                "FAQ",
                                "Privacidade",
                                "Termos de Uso"
                            ].map((item) => (
                                <Typography
                                    key={item}
                                    sx={{
                                        color: "#d4d9e2",
                                        mb: 2,
                                        cursor: "pointer",
                                        "&:hover": {
                                            color: "white"
                                        }
                                    }}
                                >
                                    {item}
                                </Typography>
                            ))}

                        </Box>

                        {/* CONTACT */}
                        <Box>

                            <Typography
                                sx={{
                                    fontWeight: 700,
                                    mb: 3,
                                    fontSize: 18
                                }}
                            >
                                Contato
                            </Typography>

                            {[
                                {
                                    icon: <EmailOutlinedIcon />,
                                    text: "contato@vittasync.com.br"
                                },
                                {
                                    icon: <PhoneOutlinedIcon />,
                                    text: "(11) 4000-0000"
                                },
                                {
                                    icon: <LocationOnOutlinedIcon />,
                                    text: "São Paulo, SP - Brasil"
                                }
                            ].map((item) => (
                                <Box
                                    key={item.text}
                                    sx={{
                                        display: "flex",
                                        alignItems: "center",
                                        gap: 1.5,
                                        mb: 3,
                                        color: "#d4d9e2"
                                    }}
                                >
                                    {item.icon}

                                    <Typography>
                                        {item.text}
                                    </Typography>

                                </Box>
                            ))}

                        </Box>

                    </Box>

                    {/* BOTTOM */}
                    <Box
                        sx={{
                            borderTop: "1px solid rgba(255,255,255,0.08)",
                            pt: 4,
                            display: "flex",
                            justifyContent: "space-between",
                            flexWrap: "wrap",
                            gap: 2
                        }}
                    >

                        <Typography
                            sx={{
                                color: "#a7b3c5"
                            }}
                        >
                            © 2026 VittaSync. Todos os direitos reservados.
                        </Typography>

                        <Box
                            sx={{
                                display: "flex",
                                gap: 3,
                                flexWrap: "wrap"
                            }}
                        >

                            {[
                                "Política de Privacidade",
                                "Termos de Serviço",
                                "Cookies"
                            ].map((item) => (
                                <Typography
                                    key={item}
                                    sx={{
                                        color: "#d4d9e2",
                                        cursor: "pointer",
                                        "&:hover": {
                                            color: "white"
                                        }
                                    }}
                                >
                                    {item}
                                </Typography>
                            ))}

                        </Box>

                    </Box>

                </Box>

            </Box>

        </Box>

    );
}