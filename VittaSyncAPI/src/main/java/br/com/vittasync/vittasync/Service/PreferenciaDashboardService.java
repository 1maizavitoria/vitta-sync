package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.DashboardLayoutDTO;
import br.com.vittasync.vittasync.DTO.DashboardWidgetDTO;
import br.com.vittasync.vittasync.Exception.DadosInvalidosException;
import br.com.vittasync.vittasync.Model.PreferenciaDashboard;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.PreferenciaDashboardRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class PreferenciaDashboardService {

    private final PreferenciaDashboardRepository repository;
    private final ObjectMapper objectMapper;

    public PreferenciaDashboardService(
            PreferenciaDashboardRepository repository,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public Optional<DashboardLayoutDTO> buscar(Usuario usuario) {
        return repository.findByUsuario(usuario).map(this::toDTO);
    }

    @Transactional
    public DashboardLayoutDTO salvar(Usuario usuario, DashboardLayoutDTO dto) {
        validar(dto);

        PreferenciaDashboard preferencia = repository.findByUsuario(usuario)
                .orElseGet(PreferenciaDashboard::new);

        preferencia.setUsuario(usuario);
        preferencia.setVersao(dto.getVersion());
        preferencia.setLayoutJson(toJson(dto));
        preferencia.setDataModificacao(LocalDateTime.now());

        return toDTO(repository.save(preferencia));
    }

    @Transactional
    public void excluir(Usuario usuario) {
        repository.deleteByUsuario(usuario);
    }

    private void validar(DashboardLayoutDTO dto) {
        if (dto == null || dto.getVersion() == null || dto.getVersion() < 1) {
            throw new DadosInvalidosException("A versão da preferência do dashboard é inválida");
        }

        if (dto.getWidgets() == null || dto.getWidgets().isEmpty()) {
            throw new DadosInvalidosException("A preferência deve possuir ao menos um widget");
        }

        Set<String> ids = new HashSet<>();
        boolean possuiWidgetVisivel = false;

        for (DashboardWidgetDTO widget : dto.getWidgets()) {
            if (widget == null || widget.getId() == null || widget.getId().isBlank()) {
                throw new DadosInvalidosException("O identificador do widget é obrigatório");
            }
            if (widget.getVisible() == null) {
                throw new DadosInvalidosException("A visibilidade do widget é obrigatória");
            }
            if (!ids.add(widget.getId())) {
                throw new DadosInvalidosException("A preferência possui widgets duplicados");
            }
            possuiWidgetVisivel = possuiWidgetVisivel || widget.getVisible();
        }

        if (!possuiWidgetVisivel) {
            throw new DadosInvalidosException("Ao menos um widget deve permanecer visível");
        }
    }

    private String toJson(DashboardLayoutDTO dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Não foi possível serializar a preferência do dashboard", e);
        }
    }

    private DashboardLayoutDTO toDTO(PreferenciaDashboard preferencia) {
        try {
            return objectMapper.readValue(preferencia.getLayoutJson(), DashboardLayoutDTO.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Não foi possível ler a preferência do dashboard", e);
        }
    }
}
