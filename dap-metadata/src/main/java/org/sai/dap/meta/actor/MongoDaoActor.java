package org.sai.dap.meta.actor;

import akka.actor.UntypedActor;
import akka.util.Timeout;
import org.sai.dap.meta.model.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import scala.concurrent.duration.Duration;

/**
 * Created by saipkri on 06/09/16.
 */
public class MongoDaoActor extends UntypedActor {

    private final MongoTemplate mongoTemplate;
    public static final Timeout timeout_in_seconds = new Timeout(Duration.create(5, "seconds"));


    public MongoDaoActor(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void onReceive(final Object message) throws Throwable {
        if (message instanceof GetAllAcquisitionConfigsRequest) {
            getSender().tell(mongoTemplate.findAll(DataAcquisitionConfig.class), getSelf());
        } else if (message instanceof GetAllJdbcConfigsRequest) {
            if (getSender() != null && !getSender().isTerminated()) {
                getSender().tell(mongoTemplate.findAll(JdbcConfig.class), getSelf());
            }
        } else if (message instanceof JdbcConfig) {
            JdbcConfig jdbcConfig = (JdbcConfig) message;
            Query query = new Query();
            query.addCriteria(Criteria.where("configId").is(jdbcConfig.getConfigId()));
            Update update = new Update();
            update.set("jdbcUrl", jdbcConfig.getJdbcUrl());
            update.set("jdbcUser", jdbcConfig.getJdbcUser());
            update.set("jdbcPassword", jdbcConfig.getJdbcPassword());
            update.set("jdbcDriver", jdbcConfig.getJdbcDriver());
            mongoTemplate.upsert(query, update, JdbcConfig.class);
            getSender().tell("finished", getSelf());
        } else if (message instanceof DataAcquisitionConfig) {
            DataAcquisitionConfig config = (DataAcquisitionConfig) message;
            Query query = new Query();
            query.addCriteria(Criteria.where("configId").is(config.getConfigId()));
            mongoTemplate.remove(query, DataAcquisitionConfig.class);
            mongoTemplate.save(config);
            getSender().tell("finished", getSelf());
        } else if (message instanceof GetAllAcquisitionConfigsRequest) {
            if (getSender() != null && !getSender().isTerminated()) {
                getSender().tell(mongoTemplate.findAll(GetAllAcquisitionConfigsRequest.class), getSelf());
            }
        } else if (message instanceof DeleteByIdRequest) {
            DeleteByIdRequest config = (DeleteByIdRequest) message;
            Query query = new Query();
            query.addCriteria(Criteria.where("configId").is(config.getId()));
            mongoTemplate.remove(query, config.getEntityClass());
            getSender().tell("finished", getSelf());
        } else if (message instanceof TriggerRequest) {
            TriggerRequest config = (TriggerRequest) message;
            Query query = new Query();
            query.addCriteria(Criteria.where("configId").is(config.getConfigId()));
            DataAcquisitionConfig dataAcquisitionConfig = mongoTemplate.findOne(query, DataAcquisitionConfig.class);
            if (dataAcquisitionConfig == null) {
                FailedTrigger msg = new FailedTrigger("Trigger creation failed. No config found for id: " + config.getConfigId());
                getSender().tell(msg, getSelf());
            }
            getSender().tell(new Trigger(config.getTriggerType(), dataAcquisitionConfig), getSelf());
        }
    }
}
