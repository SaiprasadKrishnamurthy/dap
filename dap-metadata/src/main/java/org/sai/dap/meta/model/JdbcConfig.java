package org.sai.dap.meta.model;

/**
 * Created by saipkri on 06/09/16.
 */

import lombok.Data;

@Data
public class JdbcConfig {
    private String configId;
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;
    private String jdbcDriver;
}
