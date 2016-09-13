package org.sai.dap.bolts

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichBolt
import org.apache.storm.tuple.{Fields, Tuple, Values}
import org.sai.dap.meta.model.DataAcquisitionConfig

/**
  * Created by saipkri on 30/08/16.
  */
class DataPostProcessingBolt extends BaseRichBolt {

  private[this] var outputCollector: OutputCollector = null
  private[this] var boltId: Int = 0
  private[this] val mapper = new ObjectMapper()

  override def declareOutputFields(outputFieldsDeclarer: OutputFieldsDeclarer): Unit = outputFieldsDeclarer.declare(new Fields("config", "data", "configId"))

  override def execute(tuple: Tuple): Unit = {
    val data = tuple.getValueByField("data").asInstanceOf[java.util.List[java.util.Map[String, Object]]]
    val config = tuple.getValueByField("config").asInstanceOf[DataAcquisitionConfig]
    outputCollector.emit(tuple, new Values(config, data, config.getConfigId))
    outputCollector.ack(tuple)
  }

  override def prepare(map: util.Map[_, _], topologyContext: TopologyContext, outputCollector: OutputCollector): Unit = {
    this.outputCollector = outputCollector
    this.boltId = topologyContext.getThisTaskId
  }
}
