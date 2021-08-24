package br.com.projeto.proposta.bloqueio.cartao;

import br.com.projeto.proposta.cartao.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BloqueioCartaoRepositorio extends JpaRepository<BloqueioCartao, Long> {

    Optional<BloqueioCartao> findIdByCartao( final Cartao cartao );

    List<BloqueioCartao> findFirst10ByStatus( final StatusBloqueio status );

}
