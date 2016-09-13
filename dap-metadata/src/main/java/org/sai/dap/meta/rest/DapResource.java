package org.sai.dap.meta.rest;

import akka.actor.ActorRef;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.sai.dap.meta.actor.JmsProducerActor;
import org.sai.dap.meta.actor.MongoDaoActor;
import org.sai.dap.meta.config.ActorFactory;
import org.sai.dap.meta.model.*;
import org.sai.dap.meta.util.CallbackFunctionLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import scala.concurrent.Future;

import java.util.List;

/**
 * Created by saipkri on 08/07/16.
 */
@Api("Rest API for the data acquisition meta service")
@RestController
public class DapResource {

    private final ActorFactory actorFactory;

    @Autowired
    public DapResource(final ActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    @ApiOperation("Gets all the JDBC configs configured in the system")
    @CrossOrigin(methods = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS, RequestMethod.GET})
    @RequestMapping(value = "/jdbcconfigs", method = RequestMethod.GET, produces = "application/json")
    public DeferredResult<ResponseEntity<List<JdbcConfig>>> allJdbcConfigs() throws Exception {
        DeferredResult<ResponseEntity<List<JdbcConfig>>> deferredResult = new DeferredResult<>(1000L * 1000L);
        Future<Object> results = Patterns.ask(actorFactory.newActor(MongoDaoActor.class), new GetAllJdbcConfigsRequest(), MongoDaoActor.timeout_in_seconds);
        OnFailure failureCallback = CallbackFunctionLibrary.onFailure(t -> {
            deferredResult.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        });

        results.onSuccess(new OnSuccess<Object>() {
            public void onSuccess(final Object results) {
                deferredResult.setResult(new ResponseEntity<>((List<JdbcConfig>) results, HttpStatus.OK));
            }
        }, actorFactory.executionContext());
        results.onFailure(failureCallback, actorFactory.executionContext());
        return deferredResult;
    }

    @ApiOperation("Gets all the DataAcquisition configs configured in the system")
    @CrossOrigin(methods = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS, RequestMethod.GET})
    @RequestMapping(value = "/dapconfigs", method = RequestMethod.GET, produces = "application/json")
    public DeferredResult<ResponseEntity<List<DataAcquisitionConfig>>> allDataAcquisitionConfigs() throws Exception {
        DeferredResult<ResponseEntity<List<DataAcquisitionConfig>>> deferredResult = new DeferredResult<>(1000L * 1000L);
        Future<Object> results = Patterns.ask(actorFactory.newActor(MongoDaoActor.class), new GetAllAcquisitionConfigsRequest(), MongoDaoActor.timeout_in_seconds);
        OnFailure failureCallback = CallbackFunctionLibrary.onFailure(t -> {
            deferredResult.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        });

        results.onSuccess(new OnSuccess<Object>() {
            public void onSuccess(final Object results) {
                deferredResult.setResult(new ResponseEntity<>((List<DataAcquisitionConfig>) results, HttpStatus.OK));
            }
        }, actorFactory.executionContext());
        results.onFailure(failureCallback, actorFactory.executionContext());
        return deferredResult;
    }

    @ApiOperation("Saves or Updates the JDBC configs configured in the system")
    @CrossOrigin(methods = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS, RequestMethod.GET})
    @RequestMapping(value = "/jdbcconfig", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> saveJdbcConfig(@RequestBody final JdbcConfig jdbcConfig) throws Exception {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>(1000L * 1000L);
        Future<Object> results = Patterns.ask(actorFactory.newActor(MongoDaoActor.class), jdbcConfig, MongoDaoActor.timeout_in_seconds);
        OnFailure failureCallback = CallbackFunctionLibrary.onFailure(t -> {
            deferredResult.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        });

        results.onSuccess(new OnSuccess<Object>() {
            public void onSuccess(final Object results) {
                deferredResult.setResult(new ResponseEntity<>(HttpStatus.OK));
            }
        }, actorFactory.executionContext());
        results.onFailure(failureCallback, actorFactory.executionContext());
        return deferredResult;
    }

    @ApiOperation("Saves or Updates the Data Acquisition configs")
    @CrossOrigin(methods = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS, RequestMethod.GET})
    @RequestMapping(value = "/dapconfig", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> saveJdbcConfig(@RequestBody final DataAcquisitionConfig data) throws Exception {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>(1000L * 1000L);
        Future<Object> results = Patterns.ask(actorFactory.newActor(MongoDaoActor.class), data, MongoDaoActor.timeout_in_seconds);
        OnFailure failureCallback = CallbackFunctionLibrary.onFailure(t -> {
            deferredResult.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        });

        results.onSuccess(new OnSuccess<Object>() {
            public void onSuccess(final Object results) {
                deferredResult.setResult(new ResponseEntity<>(HttpStatus.OK));
            }
        }, actorFactory.executionContext());
        results.onFailure(failureCallback, actorFactory.executionContext());
        return deferredResult;
    }

    @ApiOperation("Deletes a DAP config by id")
    @CrossOrigin(methods = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS, RequestMethod.GET})
    @RequestMapping(value = "/dapconfig/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public DeferredResult<ResponseEntity<?>> deleteDapConfig(@PathVariable final String id) throws Exception {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>(1000L * 1000L);
        Future<Object> results = Patterns.ask(actorFactory.newActor(MongoDaoActor.class), new DeleteByIdRequest(id, DataAcquisitionConfig.class), MongoDaoActor.timeout_in_seconds);
        OnFailure failureCallback = CallbackFunctionLibrary.onFailure(t -> {
            deferredResult.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        });

        results.onSuccess(new OnSuccess<Object>() {
            public void onSuccess(final Object results) {
                deferredResult.setResult(new ResponseEntity<>(HttpStatus.OK));
            }
        }, actorFactory.executionContext());
        results.onFailure(failureCallback, actorFactory.executionContext());
        return deferredResult;
    }

    @ApiOperation("Creates a trigger that will then be processed by the processing pipelines")
    @CrossOrigin(methods = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS, RequestMethod.GET})
    @RequestMapping(value = "/trigger/{triggerType}/{dataAcquisitionConfigId}", method = RequestMethod.POST, produces = "application/json")
    public DeferredResult<ResponseEntity<?>> triggerJdbcbulkload(@PathVariable("triggerType") final TriggerType triggerType, @PathVariable("dataAcquisitionConfigId") final String dataAcquisitionConfigId) throws Exception {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>(1000L * 1000L);
        ActorRef mongoDaoActor = actorFactory.newActor(MongoDaoActor.class);
        ActorRef jmsProducerActor = actorFactory.newActor(JmsProducerActor.class);

        Future<Object> trigger = Patterns.ask(mongoDaoActor, new TriggerRequest(dataAcquisitionConfigId, triggerType), MongoDaoActor.timeout_in_seconds);
        Future<Object> result = Patterns.pipe(trigger, actorFactory.executionContext()).to(jmsProducerActor).future();

        result.onSuccess(new OnSuccess<Object>() {
            public void onSuccess(final Object results) {
                if (results instanceof FailedTrigger) {
                    deferredResult.setResult(new ResponseEntity<>(results, HttpStatus.BAD_REQUEST));
                } else {
                    deferredResult.setResult(new ResponseEntity<>(HttpStatus.OK));
                }
            }
        }, actorFactory.executionContext());
        return deferredResult;
    }
}
