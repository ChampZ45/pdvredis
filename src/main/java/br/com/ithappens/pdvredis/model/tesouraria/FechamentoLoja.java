package br.com.ithappens.pdvredis.model.tesouraria;

import br.com.ithappens.pdvredis.model.tesouraria.enums.FechamentoStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class FechamentoLoja implements Serializable {

    private Long id;
    private LocalDate dataMovimento;
    private Long filialId;
    private Integer statusId;

    public FechamentoStatus getStatus(){
        return FechamentoStatus.toEnum(statusId);
    }

    public void setStatus(FechamentoStatus status){
        this.statusId = status.getId();
    }
}
