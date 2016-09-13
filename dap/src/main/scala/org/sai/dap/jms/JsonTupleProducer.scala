package org.sai.dap.jms

import javax.jms.{Message, TextMessage}

import org.apache.storm.jms.JmsTupleProducer
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.tuple.{Fields, Values}

class JsonTupleProducer extends JmsTupleProducer {

  override def toTuple(msg: Message) = {
    new Values(msg.asInstanceOf[TextMessage].getText)
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer) {
    declarer.declare(new Fields("request"));
  }
}