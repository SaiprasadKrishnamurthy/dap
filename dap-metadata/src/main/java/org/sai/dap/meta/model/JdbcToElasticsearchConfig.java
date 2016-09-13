package org.sai.dap.meta.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by saipkri on 06/09/16.
 */
@Data
@Document
public class JdbcToElasticsearchConfig {
    private JdbcConfig jdbcConfig;
    private String readFrequencyCron;
    private String esUrl;
    private String esClusterName;
    private String sourceSql;
    private String totalCountSql;
    private String esIndexName;
    private String esIndexType;
    private String esIdFieldName;
    private String esMappingsJson;
    private int dbBatchSize;
    private int esBatchSize;
}
