package org.sai.dap.topology

import javax.jms.Session

import extensions.AppJmsSpout
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.tuple.Fields
import org.apache.storm.{Config, LocalCluster}
import org.sai.dap.bolts._
import org.sai.dap.jms.{AppJmsProvider, JsonTupleProducer}
import org.springframework.context.support.ClassPathXmlApplicationContext

/**
  * Created by saipkri on 30/08/16.
  */
object DapTopology extends App {

  // Spring context
  val springContext = new ClassPathXmlApplicationContext("appContext.xml")

  val noOfExecutors = Runtime.getRuntime.availableProcessors

  private val numOfTasks: Integer = 30

  // JMS Queue Provider
  val jmsQueueProvider = new AppJmsProvider(springContext, "triggerQueue")
  val config = new Config
  config.put(Config.TOPOLOGY_DEBUG, Boolean.box(false))
  config.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, new Integer(10000))


  val builder = new TopologyBuilder
  val queueSpout = new AppJmsSpout()
  queueSpout.setJmsProvider(jmsQueueProvider)
  queueSpout.setJmsAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE)
  queueSpout.setDistributed(true); // allow multiple instances
  queueSpout.setJmsTupleProducer(new JsonTupleProducer)
  builder.setSpout("triggerSpout", queueSpout, noOfExecutors).setNumTasks(numOfTasks)

  val triggerReceiverBolt = new TriggerReceiverBolt
  val sqlCountBatchBolt = new SqlCountBatchBolt
  val sqlBatchExecutorBolt = new SqlBatchExecutorBolt
  val dataPostProcessingBolt = new DataPostProcessingBolt
  val esIndexingBolt = new EsIndexingBolt

  // Wiring.
  builder.setBolt("triggerReceiverBolt", triggerReceiverBolt, noOfExecutors).setNumTasks(numOfTasks).shuffleGrouping("triggerSpout")
  builder.setBolt("sqlCountBatchBolt", sqlCountBatchBolt, noOfExecutors).setNumTasks(numOfTasks).shuffleGrouping("triggerReceiverBolt")
  builder.setBolt("sqlBatchExecutorBolt", sqlBatchExecutorBolt, noOfExecutors).setNumTasks(numOfTasks).shuffleGrouping("sqlCountBatchBolt")
  builder.setBolt("dataPostProcessingBolt", dataPostProcessingBolt, noOfExecutors).setNumTasks(numOfTasks).shuffleGrouping("sqlBatchExecutorBolt")
  builder.setBolt("esIndexingBolt", esIndexingBolt, noOfExecutors).setNumTasks(numOfTasks).fieldsGrouping("dataPostProcessingBolt", new Fields("configId"))

  val cluster = new LocalCluster
  cluster.submitTopology("DapTopology", config, builder.createTopology)

  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    override def run(): Unit = cluster.shutdown
  }))
}
