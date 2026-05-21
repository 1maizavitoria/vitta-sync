package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.ArquivoMedico;
import br.com.vittasync.vittasync.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArquivoMedicoRepository extends JpaRepository<ArquivoMedico, Integer> {
    List<ArquivoMedico> findByPaciente(Usuario paciente);
    List<ArquivoMedico> findByMedico(Usuario medico);
}
