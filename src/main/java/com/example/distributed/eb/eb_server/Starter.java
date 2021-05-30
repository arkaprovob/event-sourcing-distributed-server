package com.example.distributed.eb.eb_server;


import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class Starter {

  private static final  Logger logger = LoggerFactory.getLogger(Starter.class);

  public static void main(String[] args) {

    final ClusterManager mgr = new HazelcastClusterManager(ClusterConfiguratorHelper.getHazelcastConfiguration());
    final VertxOptions options = new VertxOptions().setClusterManager(mgr);
    Vertx.clusteredVertx(options, cluster -> {
      if (cluster.succeeded()) {
        cluster.result().deployVerticle(new SenderVerticle(), res -> {
          if(res.succeeded()){
            logger.info("Deployment id is: {}", res.result());
          } else {
            logger.error("Deployment failed!");
          }
        });
      } else {
        logger.error("Cluster up failed: ", cluster.cause());
      }
    });
  }
  }


