package org.sai.dap.meta.model;

/**
 * Created by saipkri on 06/09/16.
 */

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class DataProcessorConfig {
    private String className;
}
