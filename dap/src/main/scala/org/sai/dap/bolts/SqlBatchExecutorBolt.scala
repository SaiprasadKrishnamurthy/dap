package org.sai.dap.bolts

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.dbcp2.BasicDataSource
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichBolt
import org.apache.storm.tuple.{Fields, Tuple, Values}
import org.sai.dap.meta.model.DataAcquisitionConfig
import org.springframework.jdbc.core.JdbcTemplate

import scala.annotation.tailrec

/**
  * Created by saipkri on 30/08/16.
  */
class SqlBatchExecutorBolt extends BaseRichBolt {

  private[this] var outputCollector: OutputCollector = null
  private[this] var boltId: Int = 0
  private[this] val mapper = new ObjectMapper()

  override def declareOutputFields(outputFieldsDeclarer: OutputFieldsDeclarer): Unit = outputFieldsDeclarer.declare(new Fields("config", "data"))

  override def execute(tuple: Tuple): Unit = {
    val ds = new BasicDataSource
    val batchStartAndEnd = tuple.getValueByField("batchStartAndEnd").asInstanceOf[(Int, Int)]
    val config = tuple.getValueByField("config").asInstanceOf[DataAcquisitionConfig]
    val sourceSqlQuery = config.getJdbcToElasticsearchConfig.getSourceSql

    // set up the datasource
    ds.setUrl(config.getJdbcToElasticsearchConfig.getJdbcConfig.getJdbcUrl)
    ds.setUsername(config.getJdbcToElasticsearchConfig.getJdbcConfig.getJdbcUser)
    ds.setPassword(config.getJdbcToElasticsearchConfig.getJdbcConfig.getJdbcPassword)
    ds.setDriverClassName(config.getJdbcToElasticsearchConfig.getJdbcConfig.getJdbcDriver)

    val jdbcTemplate = new JdbcTemplate(ds)

    println("Batch coordinates: "+batchStartAndEnd)

    val batchedSql = s"select * from (select q.*, rownum rn from (${sourceSqlQuery}) q ) where rn between ${batchStartAndEnd._1} AND ${batchStartAndEnd._2}"
    val rowsInABatch = jdbcTemplate.queryForList(batchedSql)
    outputCollector.emit(tuple, new Values(config, rowsInABatch))
    outputCollector.ack(tuple)
  }

  def batches(totalCount: Int, batchSize: Int) = {
    @tailrec
    def run(start: Int, end: Int, running: List[(Int, Int)]): List[(Int, Int)] = {
      if (end >= totalCount) running :+(start, totalCount)
      else run(end + 1, end + batchSize, running :+(start, end))
    }
    run(1, batchSize, List())
  }

  override def prepare(map: util.Map[_, _], topologyContext: TopologyContext, outputCollector: OutputCollector): Unit = {
    this.outputCollector = outputCollector
    this.boltId = topologyContext.getThisTaskId
  }
}
