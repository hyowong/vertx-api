package producer;

import static commons.Config.IDS_ADDRESS;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class WebServiceStatusProducer extends AbstractVerticle {

   private final Logger logger = Logger.getLogger(WebServiceStatusProducer.class.getName());

   @Override
   public void start(Future<Void> startFuture) throws Exception {
      logger.info("RandomIdsProducer verticle started");

      vertx.setPeriodic(1000, x -> {
         int randomId = ThreadLocalRandom.current().nextInt(0, 3);
         logger.info("Sending data to 'ids' -> " + randomId);
         vertx.eventBus().send(IDS_ADDRESS, randomId);
      });
      startFuture.complete();
   }

   public static void main(String[] args) {
      Vertx.clusteredVertx(new VertxOptions().setClustered(true), ar -> {
         if (ar.failed()) {
            System.err.println("Cannot create vert.x instance : " + ar.cause());
         } else {
            Vertx vertx = ar.result();
            vertx.deployVerticle(WebServiceStatusProducer.class.getName());
         }
      });
   }
}
