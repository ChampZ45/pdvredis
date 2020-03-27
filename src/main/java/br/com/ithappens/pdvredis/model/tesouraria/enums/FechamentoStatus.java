package br.com.ithappens.pdvredis.model.tesouraria.enums;

public enum FechamentoStatus {
    ABERTO              (0 ),
    LOTE_CRIADO         (1 ),
    FECHADO             (2 ),
    SOBRESCRITO         (4 ),
    DIVERGENCIA_VALORES (5 ),
    ERRO                (6 );

    private Integer id;

    private FechamentoStatus(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public static FechamentoStatus toEnum(Integer id){
        if(id == null)
            return  null;
        for(FechamentoStatus status : FechamentoStatus.values()){
            if(id.equals(status.getId()))
                return status;
        }
        throw new IllegalArgumentException("Id StatusPedido inválido!");
    }
}
