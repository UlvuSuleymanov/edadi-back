package az.edadi.back.model.request;

import az.edadi.back.validation.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequestModel {

    @NotBlank
    @VoteType
    private String type;

    @NotNull
    private Long id;
}
