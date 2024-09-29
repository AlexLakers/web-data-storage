package com.alex.web.data.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is entity that is based on the one row from the table 'role'.
 * The database  is 'files_repository', the schema is 'files_storage'.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    private Long id;
    private RoleName roleName;

}