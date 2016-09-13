package org.sai.dap.meta.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by saipkri on 06/09/16.
 */
@Data
@Document
public class DataAcquisitionConfig {
    private String configId;
    private JdbcToElasticsearchConfig jdbcToElasticsearchConfig;
    private List<DataProcessorConfig> dataProcessors;
}
