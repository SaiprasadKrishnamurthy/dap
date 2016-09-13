package org.sai.dap.meta.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by saipkri on 08/09/16.
 */
@Data
@AllArgsConstructor
public class TriggerRequest {
    private String configId;
    private TriggerType triggerType;
}
