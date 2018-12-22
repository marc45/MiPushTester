package moe.yuuta.server.api;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;

public class ApiVerticle extends AbstractVerticle {
    static final String ROUTE = "/";
    static final String ROUTE_TEST = ROUTE + "test";
    static final String ROUTE_UPDATE = ROUTE + "update";

    @Override
    public void start(Future<Void> startFuture) {
        Router router = Router.router(vertx);
        registerRoutes(router);
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(router);
        server.listen(8080 /* port will be forwarded in Docker, so just hard code it here */,
                ar -> {
                    if (ar.succeeded()) startFuture.complete();
                    else startFuture.fail(ar.cause());
                });
    }

    private void registerRoutes (Router router) {
        ApiHandler handler = getApiHandler();
        router.route(POST, ROUTE_TEST).handler(handler::handlePush);
        router.route(GET, ROUTE).handler(handler::handleFrameworkIndex);
        router.route(GET, ROUTE_TEST).handler(handler::handleTesterIndex);
        router.route(GET, ROUTE_UPDATE).handler(handler::handleUpdate);
    }

    ApiHandler getApiHandler () {
        return ApiHandler.apiHandler(vertx);
    }
}
