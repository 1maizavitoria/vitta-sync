package br.com.vittasync.vittasync.Repository;


import br.com.vittasync.vittasync.Model.SinaisVitais;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface SinaisVitaisRepository extends JpaRepository<SinaisVitais, Integer> {
    List<SinaisVitais> findByPacienteCpf(String cpf);
}
