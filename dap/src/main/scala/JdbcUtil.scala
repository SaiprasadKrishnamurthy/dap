import java.io.{FileWriter, PrintWriter}

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.jdbc.core.JdbcTemplate

import scala.collection.JavaConversions._

/**
  * Created by saipkri on 07/09/16.
  */
object JdbcUtil extends App {
  /*val ds = new BasicDataSource
  ds.setUrl("jdbc:oracle:thin:@//172.20.87.119:1522/wcs")
  ds.setUsername("wcsdba")
  ds.setPassword("i83Oe7UHp5LjRZqT")
  ds.setDriverClassName("oracle.jdbc.OracleDriver")
  println(ds)
  val jdbcTemplate = new JdbcTemplate(ds)
  val sql = "select count(*)  from ClientSessionInfo ClientSessionInfo\n \n                               left outer join BaseStation BaseStation on \n                                 (BaseStation.macAddress=ClientSessionInfo.clientMacAddress) \n                               left outer join WirelessAccessPoint WirelessAccessPoint on \n                                 (WirelessAccessPoint.macAddress=ClientSessionInfo.associatedAP) \n                               left outer join BaseRadio BaseRadio on \n                                 (BaseRadio.macAddress=ClientSessionInfo.associatedAP and BaseRadio.slotId=ClientSessionInfo.associatedAPSlotId) \n                               left outer join LradIfDot11n LradIfDot11n \n                                 on (BaseRadio.id = LradIfDot11n.parentId and BaseRadio.slotId = LradIfDot11n.slotId) \n                               left outer join NetworkElement NetworkElement on \n                                 (NetworkELement.NetworkElement_id = WirelessAccessPoint.rawmanagingmne_id and NetworkElement.mngmntaddrss_address=ClientSessionInfo.deviceIpAddress) \n                               left outer join NetworkResource NetworkResource on \n                                 (NetworkResource.id= NetworkElement.NetworkElement_id) \n                               left outer join ClientSessionIpAddress ClientSessionIpAddress on  \n                                  (ClientSessionIpAddress.clientMacAddress = ClientSessionInfo.clientMacAddress and  \n                                 ClientSessionIpAddress.sessionStartTime = ClientSessionInfo.sessionStartTime)              "
  val count = jdbcTemplate.queryForObject(sql, classOf[Integer])
  println(count)*/

  val ds = new BasicDataSource
  ds.setUrl("jdbc:oracle:thin:@//10.126.218.251:1522/wcs")
  ds.setUsername("wcsdba")
  ds.setPassword("HlTWDQALyoxR4gi4")
  ds.setDriverClassName("oracle.jdbc.OracleDriver")
  println(ds)

  val jdbcTemplate = new JdbcTemplate(ds)
  //  val sql = "select * from (select apmodel, count(1) ndevices from (select TRIM(model) apmodel from WirelessAccessPoint WirelessAccessPoint left outer join NetworkResource NetworkResource on (NetworkResource.id=WirelessAccessPoint.wirelessaccesspoint_id) where model IS NOT NULL and serialnumber IS NOT NULL) group by apmodel) "
  //val sql = "select * from (select APTYPE_VALUE, count(1) APTYPE_VALUE_count from (select TRIM(APTYPE_VALUE) APTYPE_VALUE from WirelessAccessPoint WirelessAccessPoint left outer join NetworkResource NetworkResource on (NetworkResource.id=WirelessAccessPoint.wirelessaccesspoint_id) where APTYPE_VALUE IS NOT NULL and serialnumber IS NOT NULL) group by APTYPE_VALUE)"
  //    val sql = "select * from NetworkElement"
  //    val sql = "select * from ADClientSessionInfo"
  //    val sql = "select * from ClientSessionInfo left outer join ClientSessionIpAddress ClientSessionIpAddress on (clientSessionIpAddress.clientMacAddress = ClientSessionInfo.clientMacAddress and ClientSessionIpAddress.sessionStartTime = ClientSessionInfo.sessionStartTime)"
  //val sql = "select * from ClientSessionInfo left outer join BaseStation BaseStation on (BaseStation.macAddress=ClientSessionInfo.clientMacAddress) left outer join WirelessAccessPoint WirelessAccessPoint on (WirelessAccessPoint.macAddress=ClientSessionInfo.associatedAP) left outer join BaseRadio BaseRadio on (BaseRadio.macAddress=ClientSessionInfo.associatedAP and BaseRadio.slotId=ClientSessionInfo.associatedAPSlotId) left outer join LradIfDot11n LradIfDot11n on (BaseRadio.id = LradIfDot11n.parentId and BaseRadio.slotId = LradIfDot11n.slotId) left outer join NetworkElement NetworkElement on (NetworkELement.NetworkElement_id = WirelessAccessPoint.rawmanagingmne_id and NetworkElement.mngmntaddrss_address=ClientSessionInfo.deviceIpAddress) left outer join ClientSessionIpAddress ClientSessionIpAddress on (ClientSessionIpAddress.clientMacAddress = ClientSessionInfo.clientMacAddress and ClientSessionIpAddress.sessionStartTime = ClientSessionInfo.sessionStartTime) where ClientSessionInfo.connectionType in (0,1)"
//  val sql = "select count(*) from ClientSessionInfo left outer join BaseStation BaseStation on (BaseStation.macAddress=ClientSessionInfo.clientMacAddress) left outer join WirelessAccessPoint WirelessAccessPoint on (WirelessAccessPoint.macAddress=ClientSessionInfo.associatedAP) left outer join BaseRadio BaseRadio on (BaseRadio.macAddress=ClientSessionInfo.associatedAP and BaseRadio.slotId=ClientSessionInfo.associatedAPSlotId) left outer join LradIfDot11n LradIfDot11n on (BaseRadio.id = LradIfDot11n.parentId and BaseRadio.slotId = LradIfDot11n.slotId) left outer join NetworkElement NetworkElement on (NetworkELement.NetworkElement_id = WirelessAccessPoint.rawmanagingmne_id and NetworkElement.mngmntaddrss_address=ClientSessionInfo.deviceIpAddress) left outer join ClientSessionIpAddress ClientSessionIpAddress on (ClientSessionIpAddress.clientMacAddress = ClientSessionInfo.clientMacAddress and ClientSessionIpAddress.sessionStartTime = ClientSessionInfo.sessionStartTime) where ClientSessionInfo.connectionType in (0,1)"
  //  val sql = "select * from WirelessAccessPoint WirelessAccessPoint left outer join NetworkResource NetworkResource on (NetworkResource.id=WirelessAccessPoint.wirelessaccesspoint_id)"
   val sql = "select * from (select * from ClientSessionInfo left outer join BaseStation BaseStation on (BaseStation.macAddress=ClientSessionInfo.clientMacAddress) left outer join WirelessAccessPoint WirelessAccessPoint on (WirelessAccessPoint.macAddress=ClientSessionInfo.associatedAP) left outer join BaseRadio BaseRadio on (BaseRadio.macAddress=ClientSessionInfo.associatedAP and BaseRadio.slotId=ClientSessionInfo.associatedAPSlotId) left outer join LradIfDot11n LradIfDot11n on (BaseRadio.id = LradIfDot11n.parentId and BaseRadio.slotId = LradIfDot11n.slotId) left outer join NetworkElement NetworkElement on (NetworkELement.NetworkElement_id = WirelessAccessPoint.rawmanagingmne_id and NetworkElement.mngmntaddrss_address=ClientSessionInfo.deviceIpAddress) left outer join ClientSessionIpAddress ClientSessionIpAddress on (ClientSessionIpAddress.clientMacAddress = ClientSessionInfo.clientMacAddress and ClientSessionIpAddress.sessionStartTime = ClientSessionInfo.sessionStartTime) where ClientSessionInfo.connectionType in (0,1)) where ROWNUM >= 1 and ROWNUM < 2000"
  val res = jdbcTemplate.queryForList(sql)

  println(res.size())
  /*val out = new PrintWriter(new FileWriter("clientSessions1.json"), true)
  val om = new ObjectMapper()
  res.zipWithIndex.foreach(tuple => {
    val index = tuple._2
    val doc = tuple._1
    val id = "{\"index\":{\"_id\":\""+index+"\"}}"
    val body = om.writeValueAsString(doc)
    out.println(id)
    out.println(body)
  })*/

}
