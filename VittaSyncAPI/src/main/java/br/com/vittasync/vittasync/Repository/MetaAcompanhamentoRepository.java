package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.MetaAcompanhamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface MetaAcompanhamentoRepository extends JpaRepository<MetaAcompanhamento, Long> {

    List<MetaAcompanhamento> findByPacienteId(Integer pacienteId);

    List<MetaAcompanhamento> findByPacienteCpf(String cpf);

    List<MetaAcompanhamento> findByStatus(String status);
}
