package org.sai.dap.meta.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by saipkri on 08/09/16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FailedTrigger extends Trigger {
    private String failureMessage;

}
