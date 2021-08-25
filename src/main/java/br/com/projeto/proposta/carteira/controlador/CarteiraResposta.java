package br.com.projeto.proposta.carteira.controlador;

import br.com.projeto.proposta.carteira.Carteira;
import br.com.projeto.proposta.carteira.CarteiraDigital;
import br.com.projeto.proposta.carteira.StatusCarteira;

class CarteiraResposta {

    private final CarteiraDigital carteiraDigital;

    CarteiraResposta(final CarteiraDigital carteiraDigital) {
        this.carteiraDigital = carteiraDigital;
    }

    public Long getId(){
        return carteiraDigital.getId();
    }

    public Carteira getCarteira(){
        return carteiraDigital.getCarteira();
    }

    public StatusCarteira getStatus(){
        return carteiraDigital.getStatus();
    }

}
