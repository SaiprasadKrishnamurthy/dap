package org.sai.dap.meta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sai.dap.meta.model.DataAcquisitionConfig;
import org.sai.dap.meta.model.JdbcConfig;
import org.sai.dap.meta.model.JdbcToElasticsearchConfig;

/**
 * Created by saipkri on 07/09/16.
 */
public class Scratch {


   /* ds.setUrl("jdbc:oracle:thin:@//10.126.218.251:1522/wcs")
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
    val sql = "select count(*) from ClientSessionInfo left outer join BaseStation BaseStation on (BaseStation.macAddress=ClientSessionInfo.clientMacAddress) left outer join WirelessAccessPoint WirelessAccessPoint on (WirelessAccessPoint.macAddress=ClientSessionInfo.associatedAP) left outer join BaseRadio BaseRadio on (BaseRadio.macAddress=ClientSessionInfo.associatedAP and BaseRadio.slotId=ClientSessionInfo.associatedAPSlotId) left outer join LradIfDot11n LradIfDot11n on (BaseRadio.id = LradIfDot11n.parentId and BaseRadio.slotId = LradIfDot11n.slotId) left outer join NetworkElement NetworkElement on (NetworkELement.NetworkElement_id = WirelessAccessPoint.rawmanagingmne_id and NetworkElement.mngmntaddrss_address=ClientSessionInfo.deviceIpAddress) left outer join ClientSessionIpAddress ClientSessionIpAddress on (ClientSessionIpAddress.clientMacAddress = ClientSessionInfo.clientMacAddress and ClientSessionIpAddress.sessionStartTime = ClientSessionInfo.sessionStartTime) where ClientSessionInfo.connectionType in (0,1)"
*/
    public static void main(String[] args) throws Exception {
        JdbcConfig jdbcConfig = new JdbcConfig();
        jdbcConfig.setConfigId("10.126.218.251");
        jdbcConfig.setJdbcUrl("jdbc:oracle:thin:@//10.126.218.251:1522/wcs");
        jdbcConfig.setJdbcDriver("oracle.jdbc.driver.OracleDriver");
        jdbcConfig.setJdbcPassword("HlTWDQALyoxR4gi4");
        jdbcConfig.setJdbcUser("wcsdba");

        DataAcquisitionConfig c = new DataAcquisitionConfig();
        JdbcToElasticsearchConfig j = new JdbcToElasticsearchConfig();
        j.setDbBatchSize(500);
        j.setEsBatchSize(1000);
        j.setEsIndexName("clients");
        j.setReadFrequencyCron("* * * 5");
        j.setJdbcConfig(jdbcConfig);
        j.setEsBatchSize(1000);
        j.setEsIdFieldName("ID");
        j.setEsUrl("http://localhost:9200");
        j.setSourceSql("select * from ClientSessionInfo left outer join BaseStation BaseStation on (BaseStation.macAddress=ClientSessionInfo.clientMacAddress) left outer join WirelessAccessPoint WirelessAccessPoint on (WirelessAccessPoint.macAddress=ClientSessionInfo.associatedAP) left outer join BaseRadio BaseRadio on (BaseRadio.macAddress=ClientSessionInfo.associatedAP and BaseRadio.slotId=ClientSessionInfo.associatedAPSlotId) left outer join LradIfDot11n LradIfDot11n on (BaseRadio.id = LradIfDot11n.parentId and BaseRadio.slotId = LradIfDot11n.slotId) left outer join NetworkElement NetworkElement on (NetworkELement.NetworkElement_id = WirelessAccessPoint.rawmanagingmne_id and NetworkElement.mngmntaddrss_address=ClientSessionInfo.deviceIpAddress) left outer join ClientSessionIpAddress ClientSessionIpAddress on (ClientSessionIpAddress.clientMacAddress = ClientSessionInfo.clientMacAddress and ClientSessionIpAddress.sessionStartTime = ClientSessionInfo.sessionStartTime) where ClientSessionInfo.connectionType in (0,1)");
        j.setTotalCountSql("select count(*) from ClientSessionInfo left outer join BaseStation BaseStation on (BaseStation.macAddress=ClientSessionInfo.clientMacAddress) left outer join WirelessAccessPoint WirelessAccessPoint on (WirelessAccessPoint.macAddress=ClientSessionInfo.associatedAP) left outer join BaseRadio BaseRadio on (BaseRadio.macAddress=ClientSessionInfo.associatedAP and BaseRadio.slotId=ClientSessionInfo.associatedAPSlotId) left outer join LradIfDot11n LradIfDot11n on (BaseRadio.id = LradIfDot11n.parentId and BaseRadio.slotId = LradIfDot11n.slotId) left outer join NetworkElement NetworkElement on (NetworkELement.NetworkElement_id = WirelessAccessPoint.rawmanagingmne_id and NetworkElement.mngmntaddrss_address=ClientSessionInfo.deviceIpAddress) left outer join ClientSessionIpAddress ClientSessionIpAddress on (ClientSessionIpAddress.clientMacAddress = ClientSessionInfo.clientMacAddress and ClientSessionIpAddress.sessionStartTime = ClientSessionInfo.sessionStartTime) where ClientSessionInfo.connectionType in (0,1)");
        ObjectMapper m = new ObjectMapper();
        c.setConfigId("ClientSessionsConfig");
        c.setJdbcToElasticsearchConfig(j);
        System.out.println(m.writeValueAsString(c));


    }
}
