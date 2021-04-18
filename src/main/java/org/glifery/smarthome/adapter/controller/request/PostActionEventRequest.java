package org.glifery.smarthome.adapter.controller.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.glifery.smarthome.domain.model.megad.SingleAction;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PostActionEventRequest {
    public String megaD;
    public Integer port;
    public SingleAction.Action action;
}
