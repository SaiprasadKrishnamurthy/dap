package org.sai.dap.util

import org.apache.commons.dbcp2.BasicDataSource

/**
  * Created by saipkri on 10/09/16.
  */
object SqlExecutor {
  val ds = new BasicDataSource

  def _init(jdbcUrl: String, userName: String, password: String, driverClassName: String) = {
    ds.setUrl(jdbcUrl)
    ds.setUsername(userName)
    ds.setPassword(password)
    ds.setDriverClassName(driverClassName)
  }
}
