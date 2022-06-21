package de.blaumeise03.projectmanager.userManagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionInfoPOJO {
    private String name = "N/A";
    private Long uid = null;
    private String mainCharName = "N/A";
    private Integer mid = null;
    private Integer cid = null;
    private String cTag = null;
}
