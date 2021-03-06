package az.edadi.back.model.request;

import az.edadi.back.validation.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestModel {

    @NotBlank
    @Size(min = 1, max = 700)
    private String text;

    @NotBlank
    @PostType
    private String type;

    @NotNull
    private Long id;

}
