import { createContext, createElement, useContext, useMemo, useState } from "react";

export const languages = [
    { code: "pt-BR", label: "PT" },
    { code: "en", label: "EN" },
    { code: "es", label: "ES" }
];

const dictionaries = {
    "pt-BR": {
        brand: "VittaSync",
        nav: {
            loginRegister: "Entrar / Registrar",
            enter: "Entrar",
            register: "Cadastrar",
            patient: "Paciente",
            user: "Usuário",
            language: "Idioma",
            theme: "Tema",
            group: "Grupo",
            dashboard: "Dashboard",
            records: "Registros",
            information: "Informações",
            documents: "Documentos",
            activity: "Atividade",
            goals: "Metas",
            patients: "Pacientes",
            openProfile: "Abrir perfil",
            logout: "Sair",
            userType: "Tipo de usuário",
            openSidebar: "Abrir menu",
            closeSidebar: "Fechar menu"
        },
        goals: {
            title: "Metas de acompanhamento", description: "Defina objetivos e acompanhe a evolução do paciente.", patient: "Paciente", new: "Nova meta", selectPatient: "Selecione um paciente para visualizar suas metas.", empty: "Nenhuma meta cadastrada", emptyHint: "Crie a primeira meta de acompanhamento.", progress: "Progresso", limitUsed: "Limite utilizado", limitExceeded: "Limite ultrapassado em", current: "Valor atual", target: "Valor-alvo", deadline: "Prazo", complete: "Concluir", updateValue: "Registrar avanço", manualTitle: "Registrar avanço", manualAmount: "Valor realizado agora", manualNewValue: "Novo acumulado", edit: "Editar", delete: "Excluir", deleteConfirm: "Deseja realmente excluir esta meta?",
            types: { sinais_vitais: "Sinais vitais", habitos: "Hábitos", personalizado: "Personalizado" },
            indicators: { peso: "Peso", horas_sono: "Horas de sono", minutos_exercicio: "Minutos de exercício", personalizado: "Meta personalizada" },
            directions: { aumentar: "Aumentar", reduzir: "Reduzir", reachMinimum: "Alcançar pelo menos", doNotExceed: "Não ultrapassar" },
            status: { em_andamento: "Em andamento", concluido: "Concluída", concluido_atrasado: "Concluída com atraso", nao_atingida: "Não atingida" },
            form: { createTitle: "Nova meta", editTitle: "Editar meta", name: "Nome", type: "Tipo de dado", indicator: "Indicador", direction: "Objetivo", unit: "Unidade", initialValue: "Valor inicial", target: "Valor-alvo", deadline: "Data-limite", cancel: "Cancelar", save: "Salvar", saving: "Salvando..." },
            alerts: { loadError: "Não foi possível carregar as metas.", createSuccess: "Meta criada com sucesso.", updateSuccess: "Meta atualizada com sucesso.", valueSuccess: "Progresso atualizado com sucesso.", completeSuccess: "Meta concluída com sucesso.", deleteSuccess: "Meta excluída com sucesso.", saveError: "Não foi possível salvar a meta.", actionError: "Não foi possível realizar esta ação." }
        },
        theme: {
            light: "Modo claro",
            dark: "Modo escuro",
            toggle: "Alternar tema"
        },
        userTypes: {
            paciente: "Paciente",
            responsavel: "Responsável",
            saude: "Saúde",
            user: "Usuário"
        },
        common: {
            save: "Salvar",
            cancel: "Cancelar",
            edit: "Editar",
            delete: "Excluir",
            remove: "Remover",
            download: "Baixar",
            view: "Visualizar",
            add: "Adicionar",
            confirm: "Confirmar"
        },
        landing: {
            badge: "Monitoramento preciso de saúde",
            titleStart: "Sua saúde em",
            titleHighlight: "sincronia",
            titleEnd: "com sua rotina",
            description:
                "Acompanhe sinais vitais, organize registros clínicos e compartilhe informações com profissionais de saúde em um ambiente seguro.",
            start: "Começar agora",
            demo: "Ver demonstração",
            stats: {
                activeUsers: "Usuários ativos",
                doctors: "Médicos parceiros",
                uptime: "Disponibilidade"
            },
            cardTitle: "Frequência cardíaca",
            cardSubtitle: "Últimos 7 dias",
            current: "Valor atual",
            normal: "Normal",
            min: "Mínimo",
            avg: "Média",
            max: "Máximo",
            vitalTitle: "Monitoramento completo dos sinais vitais",
            vitalDescription:
                "Dados organizados para decisões melhores, acompanhamento contínuo e conversas mais claras com a equipe de cuidado.",
            benefitsTitle: "Cuidado conectado, seguro e fácil de acompanhar",
            footerText:
                "Monitoramento inteligente de saúde conectado ao seu estilo de vida.",
            rights: "Todos os direitos reservados.",
            cards: [
                ["Pressão arterial", "Registre pressão sistólica e diastólica com histórico claro."],
                ["Frequência cardíaca", "Acompanhe batimentos em repouso, rotina e atividade."],
                ["Saturação de oxigênio", "Organize registros de SpO2 para acompanhamento respiratório."],
                ["Temperatura corporal", "Identifique padrões e variações ao longo do tempo."],
                ["Frequência respiratória", "Monitore respiração por minuto com leitura simples."],
                ["Relatórios clínicos", "Gere relatórios para consultas e acompanhamento profissional."]
            ],
            benefits: [
                ["Privacidade e segurança", "Dados protegidos e controle de acesso em primeiro lugar."],
                ["Rede de cuidado", "Conecte pacientes, responsáveis e profissionais de saúde."],
                ["Acompanhamento contínuo", "Rotina de saúde centralizada em uma experiência simples."]
            ],
            footerLinks: ["Funcionalidades", "Monitoramento", "Relatórios", "Privacidade"],
            supportLinks: ["Central de ajuda", "Documentação", "Termos de uso", "Cookies"]
        },
        auth: {
            loginTitle: "Acesse sua conta",
            loginSubtitle: "Entre com segurança para continuar seu acompanhamento.",
            registerTitle: "Crie sua conta",
            registerSubtitle: "Configure seu perfil para começar a sincronizar seu cuidado.",
            cpf: "CPF",
            password: "Senha",
            newPassword: "Nova senha",
            repeatPassword: "Repetir senha",
            email: "Email",
            name: "Nome",
            phone: "Telefone",
            birthDate: "Data de nascimento",
            userType: "Tipo de usuário",
            patient: "Paciente",
            responsible: "Responsável",
            healthProfessional: "Profissional da Saúde",
            initialWeight: "Peso inicial",
            height: "Altura",
            advice: "Conselho",
            receiveCodeBy: "Receber código por",
            forgotPassword: "Esqueceu a senha?",
            noAccount: "Não tem conta?",
            alreadyAccount: "Já tem conta?",
            loginAction: "Entrar",
            registering: "Cadastrando...",
            loggingIn: "Entrando...",
            registerAction: "Cadastrar",
            sendCode: "Enviar código",
            sending: "Enviando...",
            resendCode: "Reenviar código",
            wait: "Aguarde",
            confirm: "Confirmar",
            validating: "Validando...",
            changing: "Alterando...",
            changePassword: "Trocar senha",
            typeCode: "Digite seu código",
            code: "Código",
            placeholders: {
                cpf: "999.999.999-99",
                password: "Digite sua senha",
                newPassword: "Digite sua nova senha",
                code: "Digite o código",
                email: "exemplo@email.com",
                name: "Ex: João Silva",
                phone: "(11) 99999-9999",
                weight: "Ex: 70.5",
                height: "Ex: 1.75",
                advice: "CRM, COREN, etc"
            }
        },
        groupFunctions: {
            labels: {
                cuidador: "Cuidador",
                responsavel_legal: "Responsável Legal",
                acompanhante: "Acompanhante",
                contato_emergencia: "Contato de Emergência",
                tutor: "Tutor",
                medico_principal: "Médico Principal",
                especialista: "Especialista",
                consultor: "Consultor",
                acompanhamento_clinico: "Acompanhamento Clínico",
                equipe_assistencial: "Equipe Assistencial",
                responsavel: "Responsável",
                profissional_saude: "Profissional de Saúde"
            },
            descriptions: {
                cuidador: "Pessoa que acompanha cuidados diários do paciente.",
                responsavel_legal: "Pai, mãe, tutor ou curador legal do paciente.",
                acompanhante: "Pessoa que acompanha consultas e exames.",
                contato_emergencia: "Pessoa acionada em situações de urgência.",
                tutor: "Responsável por menores ou incapazes.",
                medico_principal: "Responsável principal pelo acompanhamento do paciente.",
                especialista: "Profissional consultado para uma área específica.",
                consultor: "Participa com orientações e pareceres ocasionais.",
                acompanhamento_clinico: "Auxilia no monitoramento contínuo da saúde.",
                equipe_assistencial: "Participa do cuidado multidisciplinar do paciente."
            }
        },
        patientHub: {
            alerts: {
                loadLinksError: "Erro ao carregar vínculos",
                linkRemovedSuccess: "Vínculo removido com sucesso",
                removeLinkError: "Erro ao remover vínculo",
                selectPatient: "Selecione um paciente",
                generateCodeError: "Erro ao gerar código",
                copiedSuccess: "Copiado com sucesso",
                copyError: "Erro ao copiar",
                selectRole: "Selecione uma função no grupo",
                linkCreatedSuccess: "Vínculo criado com sucesso",
                invalidCode: "Código inválido",
                emailAlreadyAdded: "Email já adicionado",
                addAtLeastOneEmail: "Adicione pelo menos um email",
                invitesSentSuccess: "Convites enviados com sucesso",
                sendInvitesError: "Erro ao enviar convites"
            },
            groupTitle: "Grupo de",
            groupSubtitle: "Pessoas que acompanham o paciente",
            groupActions: "Ações do grupo",
            groupActionsDescription: "Gerencie convites e participação no grupo.",
            inviteParticipant: "Convidar participante",
            joinWithCode: "Entrar com código",
            leaveGroup: "Sair do grupo",
            noLinksTitle: "Nenhum vínculo encontrado",
            noLinksDescription: "Gere um código ou entre com um código para criar vínculos.",
            patientSection: "Paciente",
            guardiansSection: "Responsáveis",
            doctorsSection: "Médicos",
            modules: "Módulos",
            recordsDescription: "Hábitos, sintomas, sinais vitais e lembretes.",
            informationDescription: "Dados gerais e informações do paciente.",
            documentsDescription: "Documentação médica e arquivos relacionados.",
            dashboard: "Dashboard",
            dashboardDescription: "Visualizações e métricas futuras.",
            goalsDescription: "Defina metas e acompanhe o progresso do paciente.",
            activityDescription: "Histórico de atividades e notificações do grupo.",
            generatedTitle: "Link e Código Gerados",
            codeTab: "Código",
            emailTab: "Enviar por Email",
            sendInviteByEmail: "Enviar convite por email",
            email: "Email",
            sendingInvites: "Enviando convites...",
            sendInvites: "Enviar Convites",
            roleInGroup: "Função no grupo",
            invitationCode: "Código de convite",
            removeLink: "Remover vínculo",
            leaveQuestion: "Deseja sair deste grupo?",
            removeQuestion: "Deseja remover este participante?",
            linkedAt: "Vinculado em"
        },
        dashboard: {
            title: "Dashboard de saúde",
            description: "Acompanhe a evolução dos sinais vitais e hábitos ao longo do tempo.",
            patient: "Paciente",
            noPatient: "Nenhum paciente",
            selectPatient: "Selecione um paciente vinculado para visualizar o dashboard.",
            category: "Categoria",
            lastDays: "Últimos {days} dias",
            latestValue: "Último valor do período",
            loadError: "Não foi possível carregar os dados do dashboard.",
            emptyPeriod: "Nenhum registro encontrado neste período.",
            categories: {
                todas: "Todas as categorias",
                pressao: "Pressão arterial",
                frequencia_cardiaca: "Frequência cardíaca",
                frequencia_respiratoria: "Frequência respiratória",
                temperatura: "Temperatura corporal",
                saturacao: "Saturação de oxigênio",
                peso: "Peso",
                sono: "Sono",
                exercicio: "Atividade física"
            },
            series: {
                sistolica: "Sistólica",
                diastolica: "Diastólica"
            }
        },
        healthTracker: {
            title: "Acompanhamento de saúde",
            description: "Registre sinais vitais, hábitos e sintomas em um fluxo simples.",
            selectedPatient: "Paciente",
            noPatient: "Nenhum paciente",
            tabs: {
                vitals: "Sinais vitais",
                habits: "Hábitos",
                symptoms: "Sintomas"
            },
            common: {
                cancel: "Cancelar",
                save: "Salvar",
                add: "Adicionar",
                edit: "Editar",
                notAvailable: "N/A",
                lastRecord: "Último registro",
                lastMeasurement: "Última medição",
                date: "Data",
                hours: "Horas",
                minutes: "Minutos"
            },
            habits: {
                title: "Hábitos",
                description: "Sono e exercícios registrados por dia.",
                add: "Adicionar hábitos",
                edit: "Editar hábitos",
                sleepTime: "Tempo de sono",
                exerciseTime: "Tempo de exercício",
                fillAll: "Preencha todos os campos",
                invalidExercise: "Exercício deve estar em minutos válidos (0-1440)",
                invalidSleep: "Sono deve estar em horas válidas (0-24)",
                registered: "Hábitos registrados com sucesso",
                edited: "Hábitos editados com sucesso",
                saveError: "Erro ao salvar hábitos"
            },
            symptoms: {
                title: "Sintomas",
                description: "Sintoma, intensidade e data de referência.",
                add: "Adicionar sintomas",
                edit: "Editar sintomas",
                symptom: "Sintoma",
                intensity: "Intensidade",
                invalidIntensity: "Intensidade deve estar em um valor válido (1-10)",
                registered: "Sintomas registrados com sucesso",
                edited: "Sintomas editados com sucesso",
                saveError: "Erro ao salvar sintomas"
            },
            vitals: {
                title: "Sinais vitais",
                description: "Última medição consolidada do paciente.",
                add: "Adicionar medições",
                edit: "Editar medições",
                weight: "Peso",
                heartRate: "Frequência cardíaca",
                respiratoryRate: "Frequência respiratória",
                oxygenSaturation: "Saturação de oxigênio",
                bodyTemperature: "Temperatura corporal",
                systolicPressure: "Pressão sistólica",
                diastolicPressure: "Pressão diastólica",
                fillAll: "Faça todas as medições antes de salvar",
                invalidHeartRate: "Frequência cardíaca inválida",
                invalidRespiratoryRate: "Frequência respiratória inválida",
                invalidSaturation: "Saturação deve estar entre 70% e 100%",
                invalidTemperature: "Temperatura inválida",
                invalidBloodPressure: "Pressão arterial inválida",
                invalidPressureOrder: "Sistólica deve ser maior que a diastólica",
                invalidWeight: "Peso inválido",
                registered: "Sinais vitais registrados com sucesso",
                edited: "Sinais vitais editados com sucesso",
                saveError: "Erro ao salvar sinais vitais"
            },
            reminders: {
                title: "Lembretes",
                new: "Novo lembrete",
                addTitle: "Adicionar Lembrete",
                weekDays: "Dias da semana",
                time: "Horário",
                chooseTime: "Escolha o horário",
                channel: "Canal de envio",
                both: "Ambos",
                measurement: "Fazer medição",
                chooseDayAndTime: "Escolha um dia e horário para o lembrete.",
                days: {
                    MONDAY: "Seg",
                    TUESDAY: "Ter",
                    WEDNESDAY: "Qua",
                    THURSDAY: "Qui",
                    FRIDAY: "Sex",
                    SATURDAY: "Sáb",
                    SUNDAY: "Dom",
                    Sunday: "Dom",
                    Monday: "Seg",
                    Tuesday: "Ter",
                    Wednesday: "Qua",
                    Thursday: "Qui",
                    Friday: "Sex",
                    Saturday: "Sáb"
                }
            }
        },
        activity: {
            title: "Atividade",
            descriptions: {
                patient: "Acompanhe suas atualizações, registros e notificações importantes.",
                linkedPatient: "Acompanhe atualizações, registros e notificações do paciente selecionado."
            },
            chips: {
                myActivities: "Minhas atividades",
                noPatientSelected: "Nenhum paciente selecionado"
            },
            history: "Histórico",
            historyDescription: "Eventos mais recentes aparecem primeiro.",
            emptyTitle: "Sem atividades",
            emptyDescriptionPatient: "Quando houver novas atualizações sobre você, elas aparecerão aqui.",
            emptyDescriptionLinkedPatient: "Quando houver novas atualizações do paciente selecionado, elas aparecerão aqui.",
            priorities: {
                critico: "Crítico",
                alta: "Alta",
                normal: "Normal"
            },
            events: {
                DOCUMENT_UPLOADED: {
                    title: "Documento enviado",
                    description: "{userName} enviou {documentName} para {patientName}."
                },
                DOCUMENT_REMOVED: {
                    title: "Documento removido",
                    description: "{userName} removeu {documentName}."
                },
                VITAL_SIGNS_CREATED: {
                    title: "Sinais vitais registrados",
                    description: "{userName} registrou sinais vitais de {patientName}."
                },
                VITAL_SIGNS_UPDATED: {
                    title: "Sinais vitais atualizados",
                    description: "{userName} atualizou sinais vitais de {patientName}."
                },
                VITAL_SIGNS_REMOVED: {
                    title: "Sinais vitais removidos",
                    description: "{userName} removeu um registro de sinais vitais de {patientName}."
                },
                HABITS_CREATED: {
                    title: "Hábitos registrados",
                    description: "{userName} registrou hábitos de {patientName}."
                },
                HABITS_UPDATED: {
                    title: "Hábitos atualizados",
                    description: "{userName} atualizou hábitos de {patientName}."
                },
                HABITS_REMOVED: {
                    title: "Hábitos removidos",
                    description: "{userName} removeu um registro de hábitos de {patientName}."
                },
                SYMPTOMS_CREATED: {
                    title: "Sintomas registrados",
                    description: "{userName} registrou sintomas de {patientName}."
                },
                SYMPTOMS_UPDATED: {
                    title: "Sintomas atualizados",
                    description: "{userName} atualizou sintomas de {patientName}."
                },
                SYMPTOMS_REMOVED: {
                    title: "Sintomas removidos",
                    description: "{userName} removeu um registro de sintomas de {patientName}."
                },
                LINK_CREATED: {
                    title: "Vínculo criado",
                    description: "{userName} criou um vínculo com {patientName}."
                },
                LINK_REMOVED: {
                    title: "Vínculo removido",
                    description: "{userName} removeu um vínculo de {patientName}."
                },
                REMINDER_CREATED: {
                    title: "Lembrete criado",
                    description: "{userName} criou um lembrete para {patientName}."
                },
                REMINDER_UPDATED: {
                    title: "Lembrete atualizado",
                    description: "{userName} atualizou um lembrete de {patientName}."
                },
                HIGH_BLOOD_PRESSURE: {
                    title: "Pressão arterial elevada",
                    description: "Foi registrada pressão arterial acima do normal."
                },
                FEVER_DETECTED: {
                    title: "Febre detectada",
                    description: "Foi registrada temperatura corporal elevada."
                },
                LOW_OXYGEN_SATURATION: {
                    title: "Saturação baixa detectada",
                    description: "Foi registrada saturação de oxigênio abaixo do normal."
                },
                CRITICAL_OXYGEN_SATURATION: {
                    title: "Saturação crítica detectada",
                    description: "Foi registrada saturação de oxigênio em nível crítico."
                },
                HIGH_HEART_RATE: {
                    title: "Frequência cardíaca elevada",
                    description: "Foi registrada frequência cardíaca acima do normal."
                },
                LOW_HEART_RATE: {
                    title: "Frequência cardíaca baixa",
                    description: "Foi registrada frequência cardíaca abaixo do normal."
                },
                HIGH_RESPIRATORY_RATE: {
                    title: "Frequência respiratória elevada",
                    description: "Foi registrada frequência respiratória acima do normal."
                },
                LOW_RESPIRATORY_RATE: {
                    title: "Frequência respiratória baixa",
                    description: "Foi registrada frequência respiratória abaixo do normal."
                },
                INTENSE_PAIN_DETECTED: {
                    title: "Dor intensa registrada",
                    description: "Foi registrada dor em nível elevado."
                },
                CRITICAL_PAIN_DETECTED: {
                    title: "Dor extrema registrada",
                    description: "Foi registrada dor em nível crítico."
                },
                CRITICAL_SLEEP: {
                    title: "Poucas horas de sono",
                    description: "Foi registrado período de sono muito abaixo do ideal."
                },
                LOW_PHYSICAL_ACTIVITY: {
                    title: "Baixo nível de atividade física",
                    description: "Foi registrado nível muito baixo de exercício físico."
                }
            }
        },
        documents: {
            title: "Documentos",
            tabs: {
                myDocuments: "Meus documentos",
                patientDocuments: "Documentos do paciente",
                upload: "Enviar documento"
            },
            descriptions: {
                patient: "Veja os documentos médicos enviados para você.",
                linkedPatient: "Consulte, envie e organize documentos médicos vinculados ao paciente selecionado."
            },
            status: {
                myDocuments: "Meus documentos",
                noPatientSelected: "Nenhum paciente selecionado"
            },
            emptyState: {
                selectPatientTitle: "Selecione um paciente",
                selectPatientDescription: "Os documentos aparecem quando um paciente vinculado estiver selecionado."
            },
            upload: {
                title: "Enviar documento",
                description: "Compartilhe PDFs médicos com o paciente selecionado.",
                selectPatient: "Selecione um paciente",
                fillNameAndPdf: "Informe nome e selecione um PDF",
                upload: "Enviar documento",
                onlyPdf: "Envie apenas PDFs",
                dropPdf: "Solte o PDF aqui",
                dragPdf: "Arraste um PDF para enviar",
                manualSelect: "Você também pode selecionar um arquivo manualmente.",
                documentName: "Nome do documento",
                selectPdf: "Selecionar PDF",
                removeFile: "Remover arquivo",
                sentDocuments: "Documentos enviados",
                updating: "Atualizando...",
                noSentDocuments: "Nenhum documento enviado",
                sentDocumentsDescription: "Seus envios para pacientes aparecerão aqui.",
                patientFallback: "Paciente",
                oldFile: "Arquivo antigo",
                remove: "Remover",
                download: "Baixar"
            },
            shared: {
                title: "Documentos médicos",
                patientDescription: "Documentos enviados pelos profissionais que acompanham você.",
                linkedPatientDescription: "Arquivos compartilhados pelos profissionais para o paciente selecionado.",
                patientEmptyDescription: "Quando um profissional enviar documentos para você, eles aparecerão aqui.",
                linkedPatientEmptyDescription: "Quando um profissional enviar documentos para este paciente, eles aparecerão aqui.",
                loading: "Carregando documentos...",
                noFiles: "Sem arquivos compartilhados",
                oldFile: "Arquivo antigo",
                view: "Visualizar",
                download: "Baixar",
                pdfUnavailable: "Não foi possível visualizar o PDF.",
                imageAlt: "Documento"
            }
        },
        reports: {
            title: "Informações",
            description: "Consulte e mantenha os dados essenciais organizados.",
            chips: {
                profile: "Meu perfil",
                myData: "Meus dados",
                selectedPatient: "Paciente selecionado"
            },
            profile: {
                profileTitle: "Meu perfil",
                myDataTitle: "Meus dados",
                patientInfoTitle: "Informações de",
                patientFallback: "Paciente",
                profileSubtitle: "Dados da conta logada",
                myDataSubtitle: "Dados pessoais de saúde",
                patientSubtitle: "Dados pessoais do paciente selecionado",
                edit: "Editar",
                deleteAccount: "Deletar conta",
                cancel: "Cancelar",
                save: "Salvar",
                name: "Nome",
                phone: "Telefone",
                birthDate: "Data de nascimento",
                email: "Email",
                cpf: "CPF",
                council: "Conselho",
                initialWeight: "Peso inicial (kg)",
                height: "Altura (m)",
                deleteConfirmation: "Tem certeza que deseja deletar sua conta? Essa ação não pode ser desfeita.",
                accountDeleted: "Conta deletada com sucesso",
                deleteError: "Erro ao deletar conta",
                updated: "Dados atualizados com sucesso",
                saveError: "Erro ao salvar dados",
                adultRequired: "precisa ser maior de idade"
            },
            emergencyContacts: {
                title: "Contatos de emergência",
                description: "Pessoas para contato rápido.",
                add: "Adicionar contato",
                loading: "Carregando contatos...",
                noContacts: "Nenhum contato cadastrado",
                saveContact: "Salvar contato",
                removeContact: "Remover contato",
                remove: "Remover",
                removeQuestion: "Deseja realmente remover este contato de emergência?",
                shortName: "Nome deve ter no mínimo 5 caracteres",
                invalidPhone: "Telefone inválido",
                saved: "Contato salvo com sucesso",
                saveError: "Erro ao salvar contato",
                removed: "Contato removido",
                removeError: "Erro ao remover contato",
                updated: "Contato atualizado",
                editError: "Erro ao editar contato"
            }
        },
        messages: {
            fillAll: "Preencha todos os campos",
            invalidCpf: "CPF inválido",
            wrongCpf: "CPF incorreto",
            invalidLogin: "CPF ou senha inválidos",
            invalidCode: "Código inválido",
            typeSentCode: "Digite o código enviado",
            codeBySms: "Um código foi enviado por SMS",
            codeByBoth: "Um código foi enviado por email e SMS",
            codeByEmail: "Um código foi enviado para seu email",
            invalidEmail: "Email inválido",
            fillEmail: "Preencha o email",
            codeSendError: "Erro ao enviar o código",
            passwordRules: "Verifique as regras de senha",
            passwordChanged: "Senha alterada com sucesso",
            passwordChangeError: "Erro ao trocar senha",
            shortName: "O nome deve ter pelo menos 5 caracteres",
            invalidPhone: "Telefone inválido",
            invalidWeight: "Peso inválido",
            invalidHeight: "Altura inválida",
            passwordMismatch: "As senhas devem ser iguais",
            adultRequired: "precisa ser maior de idade",
            registerSuccess: "Cadastro realizado com sucesso",
            duplicatedUser: "CPF ou Email já cadastrados",
            registerError: "Erro ao cadastrar usuário"
        }
    },
    en: {
        brand: "VittaSync",
        nav: {
            loginRegister: "Sign in / Sign up",
            enter: "Sign in",
            register: "Sign up",
            patient: "Patient",
            user: "User",
            language: "Language",
            theme: "Theme",
            group: "Group",
            dashboard: "Dashboard",
            records: "Records",
            information: "Information",
            documents: "Documents",
            activity: "Activity",
            goals: "Goals",
            patients: "Patients",
            openProfile: "Open profile",
            logout: "Sign out",
            userType: "User type",
            openSidebar: "Open menu",
            closeSidebar: "Close menu"
        },
        goals: {
            title: "Tracking goals", description: "Set objectives and track the patient's progress.", patient: "Patient", new: "New goal", selectPatient: "Select a patient to view their goals.", empty: "No goals yet", emptyHint: "Create the first tracking goal.", progress: "Progress", limitUsed: "Limit used", limitExceeded: "Limit exceeded by", current: "Current value", target: "Target value", deadline: "Deadline", complete: "Complete", updateValue: "Log progress", manualTitle: "Log progress", manualAmount: "Amount completed now", manualNewValue: "New total", edit: "Edit", delete: "Delete", deleteConfirm: "Do you really want to delete this goal?",
            types: { sinais_vitais: "Vital signs", habitos: "Habits", personalizado: "Custom" },
            indicators: { peso: "Weight", horas_sono: "Sleep hours", minutos_exercicio: "Exercise minutes", personalizado: "Custom goal" },
            directions: { aumentar: "Increase", reduzir: "Reduce", reachMinimum: "Reach at least", doNotExceed: "Do not exceed" },
            status: { em_andamento: "In progress", concluido: "Completed", concluido_atrasado: "Completed late", nao_atingida: "Not achieved" },
            form: { createTitle: "New goal", editTitle: "Edit goal", name: "Name", type: "Data type", indicator: "Indicator", direction: "Objective", unit: "Unit", initialValue: "Initial value", target: "Target value", deadline: "Deadline", cancel: "Cancel", save: "Save", saving: "Saving..." },
            alerts: { loadError: "Goals could not be loaded.", createSuccess: "Goal created successfully.", updateSuccess: "Goal updated successfully.", valueSuccess: "Progress updated successfully.", completeSuccess: "Goal completed successfully.", deleteSuccess: "Goal deleted successfully.", saveError: "Goal could not be saved.", actionError: "This action could not be completed." }
        },
        theme: {
            light: "Light mode",
            dark: "Dark mode",
            toggle: "Toggle theme"
        },
        userTypes: {
            paciente: "Patient",
            responsavel: "Guardian",
            saude: "Healthcare",
            user: "User"
        },
        common: {
            save: "Save",
            cancel: "Cancel",
            edit: "Edit",
            delete: "Delete",
            remove: "Remove",
            download: "Download",
            view: "View",
            add: "Add",
            confirm: "Confirm"
        },
        landing: {
            badge: "Precise health monitoring",
            titleStart: "Your health",
            titleHighlight: "in sync",
            titleEnd: "with your routine",
            description:
                "Track vital signs, organize clinical records and share information with healthcare professionals in a secure environment.",
            start: "Get started",
            demo: "View demo",
            stats: {
                activeUsers: "Active users",
                doctors: "Partner doctors",
                uptime: "Availability"
            },
            cardTitle: "Heart rate",
            cardSubtitle: "Last 7 days",
            current: "Current value",
            normal: "Normal",
            min: "Minimum",
            avg: "Average",
            max: "Maximum",
            vitalTitle: "Complete vital sign monitoring",
            vitalDescription:
                "Organized data for better decisions, continuous tracking and clearer conversations with your care team.",
            benefitsTitle: "Connected, secure and easy-to-follow care",
            footerText: "Smart health monitoring connected to your lifestyle.",
            rights: "All rights reserved.",
            cards: [
                ["Blood pressure", "Record systolic and diastolic pressure with clear history."],
                ["Heart rate", "Track beats at rest, during routines and activities."],
                ["Oxygen saturation", "Organize SpO2 records for respiratory tracking."],
                ["Body temperature", "Identify patterns and variations over time."],
                ["Respiratory rate", "Monitor breaths per minute with a simple view."],
                ["Clinical reports", "Generate reports for appointments and professional follow-up."]
            ],
            benefits: [
                ["Privacy and security", "Protected data and access control first."],
                ["Care network", "Connect patients, guardians and healthcare professionals."],
                ["Continuous tracking", "Centralize your health routine in a simple experience."]
            ],
            footerLinks: ["Features", "Monitoring", "Reports", "Privacy"],
            supportLinks: ["Help center", "Documentation", "Terms", "Cookies"]
        },
        auth: {
            loginTitle: "Access your account",
            loginSubtitle: "Sign in securely to continue your care tracking.",
            registerTitle: "Create your account",
            registerSubtitle: "Set up your profile to start syncing your care.",
            cpf: "CPF",
            password: "Password",
            newPassword: "New password",
            repeatPassword: "Repeat password",
            email: "Email",
            name: "Name",
            phone: "Phone",
            birthDate: "Birth date",
            userType: "User type",
            patient: "Patient",
            responsible: "Guardian",
            healthProfessional: "Healthcare professional",
            initialWeight: "Initial weight",
            height: "Height",
            advice: "License",
            receiveCodeBy: "Receive code by",
            forgotPassword: "Forgot password?",
            noAccount: "No account?",
            alreadyAccount: "Already have an account?",
            loginAction: "Sign in",
            registering: "Signing up...",
            loggingIn: "Signing in...",
            registerAction: "Sign up",
            sendCode: "Send code",
            sending: "Sending...",
            resendCode: "Resend code",
            wait: "Wait",
            confirm: "Confirm",
            validating: "Validating...",
            changing: "Changing...",
            changePassword: "Change password",
            typeCode: "Enter your code",
            code: "Code",
            placeholders: {
                cpf: "999.999.999-99",
                password: "Type your password",
                newPassword: "Type your new password",
                code: "Type the code",
                email: "example@email.com",
                name: "Ex: John Silva",
                phone: "(11) 99999-9999",
                weight: "Ex: 70.5",
                height: "Ex: 1.75",
                advice: "CRM, COREN, etc"
            }
        },
        groupFunctions: {
            labels: {
                cuidador: "Caregiver",
                responsavel_legal: "Legal Guardian",
                acompanhante: "Companion",
                contato_emergencia: "Emergency Contact",
                tutor: "Tutor",
                medico_principal: "Primary Doctor",
                especialista: "Specialist",
                consultor: "Consultant",
                acompanhamento_clinico: "Clinical Monitoring",
                equipe_assistencial: "Care Team",
                responsavel: "Guardian",
                profissional_saude: "Healthcare Professional"
            },
            descriptions: {
                cuidador: "Person who helps with the patient's daily care.",
                responsavel_legal: "Parent, tutor or legal guardian of the patient.",
                acompanhante: "Person who accompanies appointments and exams.",
                contato_emergencia: "Person contacted in urgent situations.",
                tutor: "Person responsible for minors or dependent patients.",
                medico_principal: "Main professional responsible for patient follow-up.",
                especialista: "Professional consulted for a specific area.",
                consultor: "Provides occasional guidance and opinions.",
                acompanhamento_clinico: "Helps with continuous health monitoring.",
                equipe_assistencial: "Participates in the patient's multidisciplinary care."
            }
        },
        patientHub: {
            alerts: {
                loadLinksError: "Error loading links",
                linkRemovedSuccess: "Link removed successfully",
                removeLinkError: "Error removing link",
                selectPatient: "Select a patient",
                generateCodeError: "Error generating code",
                copiedSuccess: "Copied successfully",
                copyError: "Error copying",
                selectRole: "Select a group role",
                linkCreatedSuccess: "Link created successfully",
                invalidCode: "Invalid code",
                emailAlreadyAdded: "Email already added",
                addAtLeastOneEmail: "Add at least one email",
                invitesSentSuccess: "Invites sent successfully",
                sendInvitesError: "Error sending invites"
            },
            groupTitle: "Group of",
            groupSubtitle: "People who follow the patient",
            groupActions: "Group actions",
            groupActionsDescription: "Manage invitations and group participation.",
            inviteParticipant: "Invite participant",
            joinWithCode: "Join with code",
            leaveGroup: "Leave group",
            noLinksTitle: "No links found",
            noLinksDescription: "Generate a code or join with a code to create links.",
            patientSection: "Patient",
            guardiansSection: "Guardians",
            doctorsSection: "Doctors",
            modules: "Modules",
            recordsDescription: "Habits, symptoms, vital signs and reminders.",
            informationDescription: "General data and patient information.",
            documentsDescription: "Medical documentation and related files.",
            dashboard: "Dashboard",
            dashboardDescription: "Future visualizations and metrics.",
            goalsDescription: "Set goals and track the patient's progress.",
            activityDescription: "Group activity history and notifications.",
            generatedTitle: "Generated Link and Code",
            codeTab: "Code",
            emailTab: "Send by Email",
            sendInviteByEmail: "Send invitation by email",
            email: "Email",
            sendingInvites: "Sending invites...",
            sendInvites: "Send Invites",
            roleInGroup: "Group role",
            invitationCode: "Invitation code",
            removeLink: "Remove link",
            leaveQuestion: "Do you want to leave this group?",
            removeQuestion: "Do you want to remove this participant?",
            linkedAt: "Linked on"
        },
        dashboard: {
            title: "Health dashboard",
            description: "Track changes in vital signs and habits over time.",
            patient: "Patient",
            noPatient: "No patient",
            selectPatient: "Select a linked patient to view the dashboard.",
            category: "Category",
            lastDays: "Last {days} days",
            latestValue: "Latest value in the period",
            loadError: "Dashboard data could not be loaded.",
            emptyPeriod: "No records were found in this period.",
            categories: {
                todas: "All categories",
                pressao: "Blood pressure",
                frequencia_cardiaca: "Heart rate",
                frequencia_respiratoria: "Respiratory rate",
                temperatura: "Body temperature",
                saturacao: "Oxygen saturation",
                peso: "Weight",
                sono: "Sleep",
                exercicio: "Physical activity"
            },
            series: {
                sistolica: "Systolic",
                diastolica: "Diastolic"
            }
        },
        healthTracker: {
            title: "Health tracking",
            description: "Record vital signs, habits and symptoms in a simple flow.",
            selectedPatient: "Patient",
            noPatient: "No patient",
            tabs: {
                vitals: "Vital signs",
                habits: "Habits",
                symptoms: "Symptoms"
            },
            common: {
                cancel: "Cancel",
                save: "Save",
                add: "Add",
                edit: "Edit",
                notAvailable: "N/A",
                lastRecord: "Last record",
                lastMeasurement: "Last measurement",
                date: "Date",
                hours: "Hours",
                minutes: "Minutes"
            },
            habits: {
                title: "Habits",
                description: "Sleep and exercise recorded by day.",
                add: "Add habits",
                edit: "Edit habits",
                sleepTime: "Sleep time",
                exerciseTime: "Exercise time",
                fillAll: "Fill in all fields",
                invalidExercise: "Exercise must be valid minutes (0-1440)",
                invalidSleep: "Sleep must be valid hours (0-24)",
                registered: "Habits recorded successfully",
                edited: "Habits edited successfully",
                saveError: "Error saving habits"
            },
            symptoms: {
                title: "Symptoms",
                description: "Symptom, intensity and reference date.",
                add: "Add symptoms",
                edit: "Edit symptoms",
                symptom: "Symptom",
                intensity: "Intensity",
                invalidIntensity: "Intensity must be a valid value (1-10)",
                registered: "Symptoms recorded successfully",
                edited: "Symptoms edited successfully",
                saveError: "Error saving symptoms"
            },
            vitals: {
                title: "Vital signs",
                description: "Latest consolidated patient measurement.",
                add: "Add measurements",
                edit: "Edit measurements",
                weight: "Weight",
                heartRate: "Heart rate",
                respiratoryRate: "Respiratory rate",
                oxygenSaturation: "Oxygen saturation",
                bodyTemperature: "Body temperature",
                systolicPressure: "Systolic pressure",
                diastolicPressure: "Diastolic pressure",
                fillAll: "Take all measurements before saving",
                invalidHeartRate: "Invalid heart rate",
                invalidRespiratoryRate: "Invalid respiratory rate",
                invalidSaturation: "Saturation must be between 70% and 100%",
                invalidTemperature: "Invalid temperature",
                invalidBloodPressure: "Invalid blood pressure",
                invalidPressureOrder: "Systolic must be greater than diastolic",
                invalidWeight: "Invalid weight",
                registered: "Vital signs recorded successfully",
                edited: "Vital signs edited successfully",
                saveError: "Error saving vital signs"
            },
            reminders: {
                title: "Reminders",
                new: "New reminder",
                addTitle: "Add Reminder",
                weekDays: "Weekdays",
                time: "Time",
                chooseTime: "Choose the time",
                channel: "Delivery channel",
                both: "Both",
                measurement: "Take measurement",
                chooseDayAndTime: "Choose a day and time for the reminder.",
                days: {
                    MONDAY: "Mon",
                    TUESDAY: "Tue",
                    WEDNESDAY: "Wed",
                    THURSDAY: "Thu",
                    FRIDAY: "Fri",
                    SATURDAY: "Sat",
                    SUNDAY: "Sun",
                    Sunday: "Sun",
                    Monday: "Mon",
                    Tuesday: "Tue",
                    Wednesday: "Wed",
                    Thursday: "Thu",
                    Friday: "Fri",
                    Saturday: "Sat"
                }
            }
        },
        activity: {
            title: "Activity",
            descriptions: {
                patient: "Track your updates, records and important notifications.",
                linkedPatient: "Track updates, records and notifications for the selected patient."
            },
            chips: {
                myActivities: "My activity",
                noPatientSelected: "No patient selected"
            },
            history: "History",
            historyDescription: "Most recent events appear first.",
            emptyTitle: "No activity",
            emptyDescriptionPatient: "When there are new updates about you, they will appear here.",
            emptyDescriptionLinkedPatient: "When there are new updates for the selected patient, they will appear here.",
            priorities: {
                critico: "Critical",
                alta: "High",
                normal: "Normal"
            },
            events: {
                DOCUMENT_UPLOADED: {
                    title: "Document uploaded",
                    description: "{userName} uploaded {documentName} for {patientName}."
                },
                DOCUMENT_REMOVED: {
                    title: "Document removed",
                    description: "{userName} removed {documentName}."
                },
                VITAL_SIGNS_CREATED: {
                    title: "Vital signs recorded",
                    description: "{userName} recorded vital signs for {patientName}."
                },
                VITAL_SIGNS_UPDATED: {
                    title: "Vital signs updated",
                    description: "{userName} updated vital signs for {patientName}."
                },
                VITAL_SIGNS_REMOVED: {
                    title: "Vital signs removed",
                    description: "{userName} removed a vital signs record for {patientName}."
                },
                HABITS_CREATED: {
                    title: "Habits recorded",
                    description: "{userName} recorded habits for {patientName}."
                },
                HABITS_UPDATED: {
                    title: "Habits updated",
                    description: "{userName} updated habits for {patientName}."
                },
                HABITS_REMOVED: {
                    title: "Habits removed",
                    description: "{userName} removed a habits record for {patientName}."
                },
                SYMPTOMS_CREATED: {
                    title: "Symptoms recorded",
                    description: "{userName} recorded symptoms for {patientName}."
                },
                SYMPTOMS_UPDATED: {
                    title: "Symptoms updated",
                    description: "{userName} updated symptoms for {patientName}."
                },
                SYMPTOMS_REMOVED: {
                    title: "Symptoms removed",
                    description: "{userName} removed a symptoms record for {patientName}."
                },
                LINK_CREATED: {
                    title: "Link created",
                    description: "{userName} created a link with {patientName}."
                },
                LINK_REMOVED: {
                    title: "Link removed",
                    description: "{userName} removed a link from {patientName}."
                },
                REMINDER_CREATED: {
                    title: "Reminder created",
                    description: "{userName} created a reminder for {patientName}."
                },
                REMINDER_UPDATED: {
                    title: "Reminder updated",
                    description: "{userName} updated a reminder for {patientName}."
                },
                HIGH_BLOOD_PRESSURE: {
                    title: "High blood pressure",
                    description: "Blood pressure above the normal range was recorded."
                },
                FEVER_DETECTED: {
                    title: "Fever detected",
                    description: "Elevated body temperature was recorded."
                },
                LOW_OXYGEN_SATURATION: {
                    title: "Low oxygen saturation detected",
                    description: "Oxygen saturation below the normal range was recorded."
                },
                CRITICAL_OXYGEN_SATURATION: {
                    title: "Critical oxygen saturation",
                    description: "Oxygen saturation at a critical level was recorded."
                },
                HIGH_HEART_RATE: {
                    title: "High heart rate",
                    description: "Heart rate above the normal range was recorded."
                },
                LOW_HEART_RATE: {
                    title: "Low heart rate",
                    description: "Heart rate below the normal range was recorded."
                },
                HIGH_RESPIRATORY_RATE: {
                    title: "High respiratory rate",
                    description: "Respiratory rate above the normal range was recorded."
                },
                LOW_RESPIRATORY_RATE: {
                    title: "Low respiratory rate",
                    description: "Respiratory rate below the normal range was recorded."
                },
                INTENSE_PAIN_DETECTED: {
                    title: "Intense pain recorded",
                    description: "Pain at an elevated level was recorded."
                },
                CRITICAL_PAIN_DETECTED: {
                    title: "Extreme pain recorded",
                    description: "Pain at a critical level was recorded."
                },
                CRITICAL_SLEEP: {
                    title: "Very low sleep duration",
                    description: "A sleep period far below the ideal range was recorded."
                },
                LOW_PHYSICAL_ACTIVITY: {
                    title: "Low physical activity",
                    description: "A very low level of physical exercise was recorded."
                }
            }
        },
        documents: {
            title: "Documents",
            tabs: {
                myDocuments: "My documents",
                patientDocuments: "Patient documents",
                upload: "Upload document"
            },
            descriptions: {
                patient: "View the medical documents sent to you.",
                linkedPatient: "View, upload and organize medical documents linked to the selected patient."
            },
            status: {
                myDocuments: "My documents",
                noPatientSelected: "No patient selected"
            },
            emptyState: {
                selectPatientTitle: "Select a patient",
                selectPatientDescription: "Documents appear when a linked patient is selected."
            },
            upload: {
                title: "Upload document",
                description: "Share medical PDFs with the selected patient.",
                selectPatient: "Select a patient",
                fillNameAndPdf: "Enter a name and select a PDF",
                upload: "Upload document",
                onlyPdf: "Upload PDFs only",
                dropPdf: "Drop the PDF here",
                dragPdf: "Drag a PDF to upload",
                manualSelect: "You can also select a file manually.",
                documentName: "Document name",
                selectPdf: "Select PDF",
                removeFile: "Remove file",
                sentDocuments: "Uploaded documents",
                updating: "Updating...",
                noSentDocuments: "No documents uploaded",
                sentDocumentsDescription: "Your uploads for patients will appear here.",
                patientFallback: "Patient",
                oldFile: "Old file",
                remove: "Remove",
                download: "Download"
            },
            shared: {
                title: "Medical documents",
                patientDescription: "Documents sent by the professionals who follow you.",
                linkedPatientDescription: "Files shared by professionals for the selected patient.",
                patientEmptyDescription: "When a professional sends documents to you, they will appear here.",
                linkedPatientEmptyDescription: "When a professional sends documents to this patient, they will appear here.",
                loading: "Loading documents...",
                noFiles: "No shared files",
                oldFile: "Old file",
                view: "View",
                download: "Download",
                pdfUnavailable: "Could not preview the PDF.",
                imageAlt: "Document"
            }
        },
        reports: {
            title: "Information",
            description: "View and keep essential data organized.",
            chips: {
                profile: "My profile",
                myData: "My data",
                selectedPatient: "Selected patient"
            },
            profile: {
                profileTitle: "My profile",
                myDataTitle: "My data",
                patientInfoTitle: "Information for",
                patientFallback: "Patient",
                profileSubtitle: "Logged account data",
                myDataSubtitle: "Personal health data",
                patientSubtitle: "Personal data for the selected patient",
                edit: "Edit",
                deleteAccount: "Delete account",
                cancel: "Cancel",
                save: "Save",
                name: "Name",
                phone: "Phone",
                birthDate: "Date of birth",
                email: "Email",
                cpf: "CPF",
                council: "Council",
                initialWeight: "Initial weight (kg)",
                height: "Height (m)",
                deleteConfirmation: "Are you sure you want to delete your account? This action cannot be undone.",
                accountDeleted: "Account deleted successfully",
                deleteError: "Error deleting account",
                updated: "Data updated successfully",
                saveError: "Error saving data",
                adultRequired: "must be of legal age"
            },
            emergencyContacts: {
                title: "Emergency contacts",
                description: "People for quick contact.",
                add: "Add contact",
                loading: "Loading contacts...",
                noContacts: "No contacts registered",
                saveContact: "Save contact",
                removeContact: "Remove contact",
                remove: "Remove",
                removeQuestion: "Do you really want to remove this emergency contact?",
                shortName: "Name must be at least 5 characters",
                invalidPhone: "Invalid phone",
                saved: "Contact saved successfully",
                saveError: "Error saving contact",
                removed: "Contact removed",
                removeError: "Error removing contact",
                updated: "Contact updated",
                editError: "Error editing contact"
            }
        },
        messages: {
            fillAll: "Fill in all fields",
            invalidCpf: "Invalid CPF",
            wrongCpf: "Incorrect CPF",
            invalidLogin: "Invalid CPF or password",
            invalidCode: "Invalid code",
            typeSentCode: "Type the sent code",
            codeBySms: "A code was sent by SMS",
            codeByBoth: "A code was sent by email and SMS",
            codeByEmail: "A code was sent to your email",
            invalidEmail: "Invalid email",
            fillEmail: "Fill in the email",
            codeSendError: "Error sending the code",
            passwordRules: "Check the password rules",
            passwordChanged: "Password changed successfully",
            passwordChangeError: "Error changing password",
            shortName: "The name must have at least 5 characters",
            invalidPhone: "Invalid phone",
            invalidWeight: "Invalid weight",
            invalidHeight: "Invalid height",
            passwordMismatch: "Passwords must match",
            adultRequired: "must be an adult",
            registerSuccess: "Registration completed successfully",
            duplicatedUser: "CPF or Email already registered",
            registerError: "Error creating user"
        }
    },
    es: {
        brand: "VittaSync",
        nav: {
            loginRegister: "Entrar / Registrarse",
            enter: "Entrar",
            register: "Registrarse",
            patient: "Paciente",
            user: "Usuario",
            language: "Idioma",
            theme: "Tema",
            group: "Grupo",
            dashboard: "Dashboard",
            records: "Registros",
            information: "Información",
            documents: "Documentos",
            activity: "Actividad",
            goals: "Metas",
            patients: "Pacientes",
            openProfile: "Abrir perfil",
            logout: "Salir",
            userType: "Tipo de usuario",
            openSidebar: "Abrir menú",
            closeSidebar: "Cerrar menú"
        },
        goals: {
            title: "Metas de seguimiento", description: "Define objetivos y acompaña la evolución del paciente.", patient: "Paciente", new: "Nueva meta", selectPatient: "Selecciona un paciente para ver sus metas.", empty: "No hay metas registradas", emptyHint: "Crea la primera meta de seguimiento.", progress: "Progreso", limitUsed: "Límite utilizado", limitExceeded: "Límite superado por", current: "Valor actual", target: "Valor objetivo", deadline: "Fecha límite", complete: "Completar", updateValue: "Registrar avance", manualTitle: "Registrar avance", manualAmount: "Valor realizado ahora", manualNewValue: "Nuevo acumulado", edit: "Editar", delete: "Eliminar", deleteConfirm: "¿Realmente deseas eliminar esta meta?",
            types: { sinais_vitais: "Signos vitales", habitos: "Hábitos", personalizado: "Personalizado" },
            indicators: { peso: "Peso", horas_sono: "Horas de sueño", minutos_exercicio: "Minutos de ejercicio", personalizado: "Meta personalizada" },
            directions: { aumentar: "Aumentar", reduzir: "Reducir", reachMinimum: "Alcanzar al menos", doNotExceed: "No superar" },
            status: { em_andamento: "En progreso", concluido: "Completada", concluido_atrasado: "Completada con retraso", nao_atingida: "No alcanzada" },
            form: { createTitle: "Nueva meta", editTitle: "Editar meta", name: "Nombre", type: "Tipo de dato", indicator: "Indicador", direction: "Objetivo", unit: "Unidad", initialValue: "Valor inicial", target: "Valor objetivo", deadline: "Fecha límite", cancel: "Cancelar", save: "Guardar", saving: "Guardando..." },
            alerts: { loadError: "No se pudieron cargar las metas.", createSuccess: "Meta creada correctamente.", updateSuccess: "Meta actualizada correctamente.", valueSuccess: "Progreso actualizado correctamente.", completeSuccess: "Meta completada correctamente.", deleteSuccess: "Meta eliminada correctamente.", saveError: "No se pudo guardar la meta.", actionError: "No se pudo realizar esta acción." }
        },
        theme: {
            light: "Modo claro",
            dark: "Modo oscuro",
            toggle: "Alternar tema"
        },
        userTypes: {
            paciente: "Paciente",
            responsavel: "Responsable",
            saude: "Salud",
            user: "Usuario"
        },
        common: {
            save: "Guardar",
            cancel: "Cancelar",
            edit: "Editar",
            delete: "Eliminar",
            remove: "Quitar",
            download: "Descargar",
            view: "Ver",
            add: "Agregar",
            confirm: "Confirmar"
        },
        landing: {
            badge: "Monitoreo preciso de salud",
            titleStart: "Tu salud en",
            titleHighlight: "sincronía",
            titleEnd: "con tu rutina",
            description:
                "Acompaña signos vitales, organiza registros clínicos y comparte información con profesionales de salud en un entorno seguro.",
            start: "Comenzar ahora",
            demo: "Ver demostración",
            stats: {
                activeUsers: "Usuarios activos",
                doctors: "Médicos asociados",
                uptime: "Disponibilidad"
            },
            cardTitle: "Frecuencia cardíaca",
            cardSubtitle: "Últimos 7 días",
            current: "Valor actual",
            normal: "Normal",
            min: "Mínimo",
            avg: "Promedio",
            max: "Máximo",
            vitalTitle: "Monitoreo completo de signos vitales",
            vitalDescription:
                "Datos organizados para mejores decisiones, seguimiento continuo y conversaciones más claras con el equipo de cuidado.",
            benefitsTitle: "Cuidado conectado, seguro y fácil de seguir",
            footerText: "Monitoreo inteligente de salud conectado a tu estilo de vida.",
            rights: "Todos los derechos reservados.",
            cards: [
                ["Presión arterial", "Registra presión sistólica y diastólica con historial claro."],
                ["Frecuencia cardíaca", "Acompaña latidos en reposo, rutina y actividad."],
                ["Saturación de oxígeno", "Organiza registros de SpO2 para seguimiento respiratorio."],
                ["Temperatura corporal", "Identifica patrones y variaciones con el tiempo."],
                ["Frecuencia respiratoria", "Monitorea respiraciones por minuto con lectura simple."],
                ["Informes clínicos", "Genera informes para consultas y seguimiento profesional."]
            ],
            benefits: [
                ["Privacidad y seguridad", "Datos protegidos y control de acceso primero."],
                ["Red de cuidado", "Conecta pacientes, responsables y profesionales de salud."],
                ["Seguimiento continuo", "Centraliza tu rutina de salud en una experiencia simple."]
            ],
            footerLinks: ["Funcionalidades", "Monitoreo", "Informes", "Privacidad"],
            supportLinks: ["Centro de ayuda", "Documentación", "Términos", "Cookies"]
        },
        auth: {
            loginTitle: "Accede a tu cuenta",
            loginSubtitle: "Entra con seguridad para continuar tu seguimiento.",
            registerTitle: "Crea tu cuenta",
            registerSubtitle: "Configura tu perfil para comenzar a sincronizar tu cuidado.",
            cpf: "CPF",
            password: "Contraseña",
            newPassword: "Nueva contraseña",
            repeatPassword: "Repetir contraseña",
            email: "Email",
            name: "Nombre",
            phone: "Teléfono",
            birthDate: "Fecha de nacimiento",
            userType: "Tipo de usuario",
            patient: "Paciente",
            responsible: "Responsable",
            healthProfessional: "Profesional de salud",
            initialWeight: "Peso inicial",
            height: "Altura",
            advice: "Consejo",
            receiveCodeBy: "Recibir código por",
            forgotPassword: "¿Olvidaste la contraseña?",
            noAccount: "¿No tienes cuenta?",
            alreadyAccount: "¿Ya tienes cuenta?",
            loginAction: "Entrar",
            registering: "Registrando...",
            loggingIn: "Entrando...",
            registerAction: "Registrarse",
            sendCode: "Enviar código",
            sending: "Enviando...",
            resendCode: "Reenviar código",
            wait: "Espera",
            confirm: "Confirmar",
            validating: "Validando...",
            changing: "Cambiando...",
            changePassword: "Cambiar contraseña",
            typeCode: "Introduce tu código",
            code: "Código",
            placeholders: {
                cpf: "999.999.999-99",
                password: "Escribe tu contraseña",
                newPassword: "Escribe tu nueva contraseña",
                code: "Escribe el código",
                email: "ejemplo@email.com",
                name: "Ej: Juan Silva",
                phone: "(11) 99999-9999",
                weight: "Ej: 70.5",
                height: "Ej: 1.75",
                advice: "CRM, COREN, etc"
            }
        },
        groupFunctions: {
            labels: {
                cuidador: "Cuidador",
                responsavel_legal: "Responsable Legal",
                acompanhante: "Acompañante",
                contato_emergencia: "Contacto de Emergencia",
                tutor: "Tutor",
                medico_principal: "Médico Principal",
                especialista: "Especialista",
                consultor: "Consultor",
                acompanhamento_clinico: "Seguimiento Clínico",
                equipe_assistencial: "Equipo Asistencial",
                responsavel: "Responsable",
                profissional_saude: "Profesional de Salud"
            },
            descriptions: {
                cuidador: "Persona que acompaña los cuidados diarios del paciente.",
                responsavel_legal: "Padre, madre, tutor o curador legal del paciente.",
                acompanhante: "Persona que acompaña consultas y exámenes.",
                contato_emergencia: "Persona contactada en situaciones de urgencia.",
                tutor: "Responsable por menores o personas dependientes.",
                medico_principal: "Responsable principal del seguimiento del paciente.",
                especialista: "Profesional consultado para un área específica.",
                consultor: "Participa con orientaciones y opiniones ocasionales.",
                acompanhamento_clinico: "Ayuda en el monitoreo continuo de la salud.",
                equipe_assistencial: "Participa en el cuidado multidisciplinario del paciente."
            }
        },
        patientHub: {
            alerts: {
                loadLinksError: "Error al cargar vínculos",
                linkRemovedSuccess: "Vínculo eliminado con éxito",
                removeLinkError: "Error al eliminar vínculo",
                selectPatient: "Selecciona un paciente",
                generateCodeError: "Error al generar código",
                copiedSuccess: "Copiado con éxito",
                copyError: "Error al copiar",
                selectRole: "Selecciona una función en el grupo",
                linkCreatedSuccess: "Vínculo creado con éxito",
                invalidCode: "Código inválido",
                emailAlreadyAdded: "Email ya agregado",
                addAtLeastOneEmail: "Agrega al menos un email",
                invitesSentSuccess: "Invitaciones enviadas con éxito",
                sendInvitesError: "Error al enviar invitaciones"
            },
            groupTitle: "Grupo de",
            groupSubtitle: "Personas que acompañan al paciente",
            groupActions: "Acciones del grupo",
            groupActionsDescription: "Gestiona invitaciones y participación en el grupo.",
            inviteParticipant: "Invitar participante",
            joinWithCode: "Entrar con código",
            leaveGroup: "Salir del grupo",
            noLinksTitle: "No se encontraron vínculos",
            noLinksDescription: "Genera un código o entra con un código para crear vínculos.",
            patientSection: "Paciente",
            guardiansSection: "Responsables",
            doctorsSection: "Médicos",
            modules: "Módulos",
            recordsDescription: "Hábitos, síntomas, signos vitales y recordatorios.",
            informationDescription: "Datos generales e información del paciente.",
            documentsDescription: "Documentación médica y archivos relacionados.",
            dashboard: "Dashboard",
            dashboardDescription: "Visualizaciones y métricas futuras.",
            goalsDescription: "Define metas y acompaña el progreso del paciente.",
            activityDescription: "Historial de actividades y notificaciones del grupo.",
            generatedTitle: "Link y Código Generados",
            codeTab: "Código",
            emailTab: "Enviar por Email",
            sendInviteByEmail: "Enviar invitación por email",
            email: "Email",
            sendingInvites: "Enviando invitaciones...",
            sendInvites: "Enviar Invitaciones",
            roleInGroup: "Función en el grupo",
            invitationCode: "Código de invitación",
            removeLink: "Eliminar vínculo",
            leaveQuestion: "¿Deseas salir de este grupo?",
            removeQuestion: "¿Deseas eliminar este participante?",
            linkedAt: "Vinculado el"
        },
        dashboard: {
            title: "Panel de salud",
            description: "Acompaña la evolución de los signos vitales y hábitos a lo largo del tiempo.",
            patient: "Paciente",
            noPatient: "Ningún paciente",
            selectPatient: "Selecciona un paciente vinculado para visualizar el panel.",
            category: "Categoría",
            lastDays: "Últimos {days} días",
            latestValue: "Último valor del período",
            loadError: "No fue posible cargar los datos del panel.",
            emptyPeriod: "No se encontraron registros en este período.",
            categories: {
                todas: "Todas las categorías",
                pressao: "Presión arterial",
                frequencia_cardiaca: "Frecuencia cardíaca",
                frequencia_respiratoria: "Frecuencia respiratoria",
                temperatura: "Temperatura corporal",
                saturacao: "Saturación de oxígeno",
                peso: "Peso",
                sono: "Sueño",
                exercicio: "Actividad física"
            },
            series: {
                sistolica: "Sistólica",
                diastolica: "Diastólica"
            }
        },
        healthTracker: {
            title: "Seguimiento de salud",
            description: "Registra signos vitales, hábitos y síntomas en un flujo simple.",
            selectedPatient: "Paciente",
            noPatient: "Ningún paciente",
            tabs: {
                vitals: "Signos vitales",
                habits: "Hábitos",
                symptoms: "Síntomas"
            },
            common: {
                cancel: "Cancelar",
                save: "Guardar",
                add: "Agregar",
                edit: "Editar",
                notAvailable: "N/A",
                lastRecord: "Último registro",
                lastMeasurement: "Última medición",
                date: "Fecha",
                hours: "Horas",
                minutes: "Minutos"
            },
            habits: {
                title: "Hábitos",
                description: "Sueño y ejercicios registrados por día.",
                add: "Agregar hábitos",
                edit: "Editar hábitos",
                sleepTime: "Tiempo de sueño",
                exerciseTime: "Tiempo de ejercicio",
                fillAll: "Completa todos los campos",
                invalidExercise: "El ejercicio debe estar en minutos válidos (0-1440)",
                invalidSleep: "El sueño debe estar en horas válidas (0-24)",
                registered: "Hábitos registrados con éxito",
                edited: "Hábitos editados con éxito",
                saveError: "Error al guardar hábitos"
            },
            symptoms: {
                title: "Síntomas",
                description: "Síntoma, intensidad y fecha de referencia.",
                add: "Agregar síntomas",
                edit: "Editar síntomas",
                symptom: "Síntoma",
                intensity: "Intensidad",
                invalidIntensity: "La intensidad debe ser un valor válido (1-10)",
                registered: "Síntomas registrados con éxito",
                edited: "Síntomas editados con éxito",
                saveError: "Error al guardar síntomas"
            },
            vitals: {
                title: "Signos vitales",
                description: "Última medición consolidada del paciente.",
                add: "Agregar mediciones",
                edit: "Editar mediciones",
                weight: "Peso",
                heartRate: "Frecuencia cardíaca",
                respiratoryRate: "Frecuencia respiratoria",
                oxygenSaturation: "Saturación de oxígeno",
                bodyTemperature: "Temperatura corporal",
                systolicPressure: "Presión sistólica",
                diastolicPressure: "Presión diastólica",
                fillAll: "Realiza todas las mediciones antes de guardar",
                invalidHeartRate: "Frecuencia cardíaca inválida",
                invalidRespiratoryRate: "Frecuencia respiratoria inválida",
                invalidSaturation: "La saturación debe estar entre 70% y 100%",
                invalidTemperature: "Temperatura inválida",
                invalidBloodPressure: "Presión arterial inválida",
                invalidPressureOrder: "La sistólica debe ser mayor que la diastólica",
                invalidWeight: "Peso inválido",
                registered: "Signos vitales registrados con éxito",
                edited: "Signos vitales editados con éxito",
                saveError: "Error al guardar signos vitales"
            },
            reminders: {
                title: "Recordatorios",
                new: "Nuevo recordatorio",
                addTitle: "Agregar Recordatorio",
                weekDays: "Días de la semana",
                time: "Horario",
                chooseTime: "Elige el horario",
                channel: "Canal de envío",
                both: "Ambos",
                measurement: "Hacer medición",
                chooseDayAndTime: "Elige un día y horario para el recordatorio.",
                days: {
                    MONDAY: "Lun",
                    TUESDAY: "Mar",
                    WEDNESDAY: "Mié",
                    THURSDAY: "Jue",
                    FRIDAY: "Vie",
                    SATURDAY: "Sáb",
                    SUNDAY: "Dom",
                    Sunday: "Dom",
                    Monday: "Lun",
                    Tuesday: "Mar",
                    Wednesday: "Mié",
                    Thursday: "Jue",
                    Friday: "Vie",
                    Saturday: "Sáb"
                }
            }
        },
        activity: {
            title: "Actividad",
            descriptions: {
                patient: "Acompaña tus actualizaciones, registros y notificaciones importantes.",
                linkedPatient: "Acompaña actualizaciones, registros y notificaciones del paciente seleccionado."
            },
            chips: {
                myActivities: "Mis actividades",
                noPatientSelected: "Ningún paciente seleccionado"
            },
            history: "Historial",
            historyDescription: "Los eventos más recientes aparecen primero.",
            emptyTitle: "Sin actividades",
            emptyDescriptionPatient: "Cuando haya nuevas actualizaciones sobre ti, aparecerán aquí.",
            emptyDescriptionLinkedPatient: "Cuando haya nuevas actualizaciones del paciente seleccionado, aparecerán aquí.",
            priorities: {
                critico: "Crítico",
                alta: "Alta",
                normal: "Normal"
            },
            events: {
                DOCUMENT_UPLOADED: {
                    title: "Documento enviado",
                    description: "{userName} envió {documentName} para {patientName}."
                },
                DOCUMENT_REMOVED: {
                    title: "Documento eliminado",
                    description: "{userName} eliminó {documentName}."
                },
                VITAL_SIGNS_CREATED: {
                    title: "Signos vitales registrados",
                    description: "{userName} registró signos vitales de {patientName}."
                },
                VITAL_SIGNS_UPDATED: {
                    title: "Signos vitales actualizados",
                    description: "{userName} actualizó signos vitales de {patientName}."
                },
                VITAL_SIGNS_REMOVED: {
                    title: "Signos vitales eliminados",
                    description: "{userName} eliminó un registro de signos vitales de {patientName}."
                },
                HABITS_CREATED: {
                    title: "Hábitos registrados",
                    description: "{userName} registró hábitos de {patientName}."
                },
                HABITS_UPDATED: {
                    title: "Hábitos actualizados",
                    description: "{userName} actualizó hábitos de {patientName}."
                },
                HABITS_REMOVED: {
                    title: "Hábitos eliminados",
                    description: "{userName} eliminó un registro de hábitos de {patientName}."
                },
                SYMPTOMS_CREATED: {
                    title: "Síntomas registrados",
                    description: "{userName} registró síntomas de {patientName}."
                },
                SYMPTOMS_UPDATED: {
                    title: "Síntomas actualizados",
                    description: "{userName} actualizó síntomas de {patientName}."
                },
                SYMPTOMS_REMOVED: {
                    title: "Síntomas eliminados",
                    description: "{userName} eliminó un registro de síntomas de {patientName}."
                },
                LINK_CREATED: {
                    title: "Vínculo creado",
                    description: "{userName} creó un vínculo con {patientName}."
                },
                LINK_REMOVED: {
                    title: "Vínculo eliminado",
                    description: "{userName} eliminó un vínculo de {patientName}."
                },
                REMINDER_CREATED: {
                    title: "Recordatorio creado",
                    description: "{userName} creó un recordatorio para {patientName}."
                },
                REMINDER_UPDATED: {
                    title: "Recordatorio actualizado",
                    description: "{userName} actualizó un recordatorio de {patientName}."
                },
                HIGH_BLOOD_PRESSURE: {
                    title: "Presión arterial elevada",
                    description: "Se registró presión arterial por encima del rango normal."
                },
                FEVER_DETECTED: {
                    title: "Fiebre detectada",
                    description: "Se registró temperatura corporal elevada."
                },
                LOW_OXYGEN_SATURATION: {
                    title: "Saturación baja detectada",
                    description: "Se registró saturación de oxígeno por debajo del rango normal."
                },
                CRITICAL_OXYGEN_SATURATION: {
                    title: "Saturación crítica detectada",
                    description: "Se registró saturación de oxígeno en un nivel crítico."
                },
                HIGH_HEART_RATE: {
                    title: "Frecuencia cardíaca elevada",
                    description: "Se registró frecuencia cardíaca por encima del rango normal."
                },
                LOW_HEART_RATE: {
                    title: "Frecuencia cardíaca baja",
                    description: "Se registró frecuencia cardíaca por debajo del rango normal."
                },
                HIGH_RESPIRATORY_RATE: {
                    title: "Frecuencia respiratoria elevada",
                    description: "Se registró frecuencia respiratoria por encima del rango normal."
                },
                LOW_RESPIRATORY_RATE: {
                    title: "Frecuencia respiratoria baja",
                    description: "Se registró frecuencia respiratoria por debajo del rango normal."
                },
                INTENSE_PAIN_DETECTED: {
                    title: "Dolor intenso registrado",
                    description: "Se registró dolor en un nivel elevado."
                },
                CRITICAL_PAIN_DETECTED: {
                    title: "Dolor extremo registrado",
                    description: "Se registró dolor en un nivel crítico."
                },
                CRITICAL_SLEEP: {
                    title: "Pocas horas de sueño",
                    description: "Se registró un período de sueño muy por debajo de lo ideal."
                },
                LOW_PHYSICAL_ACTIVITY: {
                    title: "Bajo nivel de actividad física",
                    description: "Se registró un nivel muy bajo de ejercicio físico."
                }
            }
        },
        documents: {
            title: "Documentos",
            tabs: {
                myDocuments: "Mis documentos",
                patientDocuments: "Documentos del paciente",
                upload: "Enviar documento"
            },
            descriptions: {
                patient: "Ve los documentos médicos enviados para ti.",
                linkedPatient: "Consulta, envía y organiza documentos médicos vinculados al paciente seleccionado."
            },
            status: {
                myDocuments: "Mis documentos",
                noPatientSelected: "Ningún paciente seleccionado"
            },
            emptyState: {
                selectPatientTitle: "Selecciona un paciente",
                selectPatientDescription: "Los documentos aparecen cuando un paciente vinculado está seleccionado."
            },
            upload: {
                title: "Enviar documento",
                description: "Comparte PDFs médicos con el paciente seleccionado.",
                selectPatient: "Selecciona un paciente",
                fillNameAndPdf: "Informa el nombre y selecciona un PDF",
                upload: "Enviar documento",
                onlyPdf: "Envía solo PDFs",
                dropPdf: "Suelta el PDF aquí",
                dragPdf: "Arrastra un PDF para enviar",
                manualSelect: "También puedes seleccionar un archivo manualmente.",
                documentName: "Nombre del documento",
                selectPdf: "Seleccionar PDF",
                removeFile: "Quitar archivo",
                sentDocuments: "Documentos enviados",
                updating: "Actualizando...",
                noSentDocuments: "Ningún documento enviado",
                sentDocumentsDescription: "Tus envíos para pacientes aparecerán aquí.",
                patientFallback: "Paciente",
                oldFile: "Archivo antiguo",
                remove: "Eliminar",
                download: "Descargar"
            },
            shared: {
                title: "Documentos médicos",
                patientDescription: "Documentos enviados por los profesionales que te acompañan.",
                linkedPatientDescription: "Archivos compartidos por profesionales para el paciente seleccionado.",
                patientEmptyDescription: "Cuando un profesional te envíe documentos, aparecerán aquí.",
                linkedPatientEmptyDescription: "Cuando un profesional envíe documentos para este paciente, aparecerán aquí.",
                loading: "Cargando documentos...",
                noFiles: "Sin archivos compartidos",
                oldFile: "Archivo antiguo",
                view: "Visualizar",
                download: "Descargar",
                pdfUnavailable: "No fue posible visualizar el PDF.",
                imageAlt: "Documento"
            }
        },
        reports: {
            title: "Información",
            description: "Consulta y mantén los datos esenciales organizados.",
            chips: {
                profile: "Mi perfil",
                myData: "Mis datos",
                selectedPatient: "Paciente seleccionado"
            },
            profile: {
                profileTitle: "Mi perfil",
                myDataTitle: "Mis datos",
                patientInfoTitle: "Información de",
                patientFallback: "Paciente",
                profileSubtitle: "Datos de la cuenta conectada",
                myDataSubtitle: "Datos personales de salud",
                patientSubtitle: "Datos personales del paciente seleccionado",
                edit: "Editar",
                deleteAccount: "Eliminar cuenta",
                cancel: "Cancelar",
                save: "Guardar",
                name: "Nombre",
                phone: "Teléfono",
                birthDate: "Fecha de nacimiento",
                email: "Email",
                cpf: "CPF",
                council: "Consejo",
                initialWeight: "Peso inicial (kg)",
                height: "Altura (m)",
                deleteConfirmation: "¿Seguro que deseas eliminar tu cuenta? Esta acción no se puede deshacer.",
                accountDeleted: "Cuenta eliminada con éxito",
                deleteError: "Error al eliminar cuenta",
                updated: "Datos actualizados con éxito",
                saveError: "Error al guardar datos",
                adultRequired: "debe ser mayor de edad"
            },
            emergencyContacts: {
                title: "Contactos de emergencia",
                description: "Personas para contacto rápido.",
                add: "Agregar contacto",
                loading: "Cargando contactos...",
                noContacts: "No hay contactos registrados",
                saveContact: "Guardar contacto",
                removeContact: "Eliminar contacto",
                remove: "Eliminar",
                removeQuestion: "¿Deseas realmente eliminar este contacto de emergencia?",
                shortName: "El nombre debe tener al menos 5 caracteres",
                invalidPhone: "Teléfono inválido",
                saved: "Contacto guardado con éxito",
                saveError: "Error al guardar contacto",
                removed: "Contacto eliminado",
                removeError: "Error al eliminar contacto",
                updated: "Contacto actualizado",
                editError: "Error al editar contacto"
            }
        },
        messages: {
            fillAll: "Completa todos los campos",
            invalidCpf: "CPF inválido",
            wrongCpf: "CPF incorrecto",
            invalidLogin: "CPF o contraseña inválidos",
            invalidCode: "Código inválido",
            typeSentCode: "Escribe el código enviado",
            codeBySms: "Se envió un código por SMS",
            codeByBoth: "Se envió un código por email y SMS",
            codeByEmail: "Se envió un código a tu email",
            invalidEmail: "Email inválido",
            fillEmail: "Completa el email",
            codeSendError: "Error al enviar el código",
            passwordRules: "Verifica las reglas de contraseña",
            passwordChanged: "Contraseña cambiada con éxito",
            passwordChangeError: "Error al cambiar contraseña",
            shortName: "El nombre debe tener al menos 5 caracteres",
            invalidPhone: "Teléfono inválido",
            invalidWeight: "Peso inválido",
            invalidHeight: "Altura inválida",
            passwordMismatch: "Las contraseñas deben coincidir",
            adultRequired: "debe ser mayor de edad",
            registerSuccess: "Registro completado con éxito",
            duplicatedUser: "CPF o Email ya registrados",
            registerError: "Error al registrar usuario"
        }
    }
};

const I18nContext = createContext(null);

function readPath(source, path) {
    return path.split(".").reduce((value, key) => value?.[key], source);
}

export function LanguageProvider({ children }) {
    const [language, setLanguageState] = useState(() => localStorage.getItem("vitta-language") || "pt-BR");

    const value = useMemo(() => {
        const dictionary = dictionaries[language] || dictionaries["pt-BR"];

        return {
            language,
            languages,
            setLanguage: (nextLanguage) => {
                localStorage.setItem("vitta-language", nextLanguage);
                setLanguageState(nextLanguage);
            },
            t: (path) => readPath(dictionary, path) ?? readPath(dictionaries["pt-BR"], path) ?? path
        };
    }, [language]);

    return createElement(I18nContext.Provider, { value }, children);
}

export function useI18n() {
    const context = useContext(I18nContext);

    if (!context) {
        throw new Error("useI18n must be used inside LanguageProvider");
    }

    return context;
}
