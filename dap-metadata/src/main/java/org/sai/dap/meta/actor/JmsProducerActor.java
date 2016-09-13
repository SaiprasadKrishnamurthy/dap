package org.sai.dap.meta.actor;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sai.dap.meta.model.FailedTrigger;
import org.sai.dap.meta.model.JdbcConfig;
import org.sai.dap.meta.model.Trigger;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by saipkri on 08/09/16.
 */
public class JmsProducerActor extends UntypedActor {

    private final JmsTemplate jmsTemplate;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public JmsProducerActor(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void onReceive(final Object message) throws Throwable {
        if (message instanceof Trigger && !(message instanceof FailedTrigger)) {
            jmsTemplate.send("triggerQueue", session -> message(message, session));
        }
        getSender().tell(message, getSelf());
    }

    private Message message(final Object message, final Session session) throws JMSException {
        try {
            return session.createTextMessage(MAPPER.writeValueAsString(message));
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
