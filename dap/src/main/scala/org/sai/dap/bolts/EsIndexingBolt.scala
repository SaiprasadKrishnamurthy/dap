package org.sai.dap.bolts

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichBolt
import org.apache.storm.tuple.{Fields, Tuple, Values}
import org.sai.dap.meta.model.DataAcquisitionConfig
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate

import scala.collection.JavaConversions._

/**
  * Created by saipkri on 30/08/16.
  */
class EsIndexingBolt extends BaseRichBolt {

  private[this] var outputCollector: OutputCollector = null
  private[this] var boltId: Int = 0
  private[this] val mapper = new ObjectMapper()
  private[this] val documentsBuffer = new util.ArrayList[String]()

  override def declareOutputFields(outputFieldsDeclarer: OutputFieldsDeclarer): Unit = outputFieldsDeclarer.declare(new Fields("config", "data"))

  override def execute(tuple: Tuple): Unit = {
    val restClient = new RestTemplate()
    val data = tuple.getValueByField("data").asInstanceOf[java.util.List[java.util.Map[String, Object]]]
    val config = tuple.getValueByField("config").asInstanceOf[DataAcquisitionConfig]
    val esDocBatchSize = config.getJdbcToElasticsearchConfig.getEsBatchSize
    val esDocumentNaturalIdField = config.getJdbcToElasticsearchConfig.getEsIdFieldName

    val docToBeIndexed = data.map(doc => "{\"index\": {\"_id\": \"" + doc.get(esDocumentNaturalIdField.trim) + "\"}}" + "\n" + mapper.writeValueAsString(doc) + "\n")
    documentsBuffer.addAll(docToBeIndexed)
    if (documentsBuffer.size() >= esDocBatchSize) {
      println("Going to Bulk index the data to ES: " + config.getJdbcToElasticsearchConfig.getEsIndexType + s" (${boltId})")
      restClient.postForObject(config.getJdbcToElasticsearchConfig.getEsUrl + s"/${config.getJdbcToElasticsearchConfig.getEsIndexName}/${config.getJdbcToElasticsearchConfig.getEsIndexType}/_bulk", documentsBuffer.mkString(""), classOf[java.util.Map[String, Object]])
      documentsBuffer.clear()
    }
    outputCollector.emit(tuple, new Values(config, data))
    outputCollector.ack(tuple)
  }

  override def prepare(map: util.Map[_, _], topologyContext: TopologyContext, outputCollector: OutputCollector): Unit = {
    this.outputCollector = outputCollector
    this.boltId = topologyContext.getThisTaskId
  }
}
