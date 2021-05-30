package com.example.distributed.eb.eb_server;


import com.hazelcast.config.Config;
import com.hazelcast.config.InterfacesConfig;

public class ClusterConfiguratorHelper {
  private ClusterConfiguratorHelper(){}

  public static Config getHazelcastConfiguration(){

    final var config = new Config();
    final var joinConfig = config.getNetworkConfig().getJoin();
    joinConfig.getMulticastConfig().setEnabled(false);
    joinConfig.getTcpIpConfig().setEnabled(true).addMember("127.0.0.1");

    final InterfacesConfig interfaceConfig = config.getNetworkConfig().getInterfaces();
    interfaceConfig.setEnabled(true).addInterface( "127.0.0.1" );

    return config;
  }

}
