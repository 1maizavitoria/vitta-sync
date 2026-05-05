package br.com.vittasync.vittasync.Repository;

import br.com.vittasync.vittasync.Model.DiarioSintomas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiarioSintomasRepository extends JpaRepository<DiarioSintomas, Integer> {
    List<DiarioSintomas> findByPacienteCpf(String cpf);
}
