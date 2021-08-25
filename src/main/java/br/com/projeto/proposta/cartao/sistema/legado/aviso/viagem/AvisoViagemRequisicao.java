package br.com.projeto.proposta.cartao.sistema.legado.aviso.viagem;

import java.time.LocalDate;

public class AvisoViagemRequisicao {

    private final String destino;

    private final LocalDate validoAte;

    public AvisoViagemRequisicao(final String destino, final LocalDate validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }

}
