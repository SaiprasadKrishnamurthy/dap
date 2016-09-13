package org.sai.dap.meta.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by saipkri on 07/09/16.
 */
@Data
@AllArgsConstructor
public class DeleteByIdRequest {
    private String id;
    private Class<?> entityClass;
}
