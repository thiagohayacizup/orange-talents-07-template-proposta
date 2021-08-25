package br.com.projeto.proposta.carteira;

import br.com.projeto.proposta.cartao.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarteiraDigitalRepositorio extends JpaRepository<CarteiraDigital, Long> {

    Optional<CarteiraDigital> findByCarteiraAndCartaoAndStatus(final Carteira carteira, final Cartao cartao, final StatusCarteira status);

}
