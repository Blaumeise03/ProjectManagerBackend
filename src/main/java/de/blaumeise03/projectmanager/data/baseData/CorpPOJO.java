package de.blaumeise03.projectmanager.data.baseData;

import de.blaumeise03.projectmanager.utils.POJO;
import de.blaumeise03.projectmanager.utils.POJOData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@POJO(mappingClass = Corp.class)
public class CorpPOJO {
    @POJOData
    private Integer cid;
    @POJOData
    private String tag;
    @POJOData
    private String name;
}
