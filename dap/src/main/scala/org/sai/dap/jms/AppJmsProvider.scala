package org.sai.dap.jms

import javax.jms.{ConnectionFactory, Destination}

import org.apache.storm.jms.JmsProvider
import org.springframework.context.ApplicationContext

class AppJmsProvider extends JmsProvider {
  private[this] var _connectionFactory: ConnectionFactory = null
  private[this] var _destination: Destination = null

  def this(springContext: ApplicationContext, destinationBeanId: String) {
    this()
    this._connectionFactory = springContext.getBean(classOf[ConnectionFactory])
    this._destination = springContext.getBean(destinationBeanId).asInstanceOf[Destination]
  }

  override def connectionFactory(): ConnectionFactory = this._connectionFactory

  override def destination(): Destination = this._destination
}