package pharmacie.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class AdressePostale {
    @NotBlank
    private String rue;
    @NotBlank
    private String codePostal;
    @NotBlank
    private String ville;
    @NotBlank
    private String pays;
    @NotBlank
    private String region;
}
