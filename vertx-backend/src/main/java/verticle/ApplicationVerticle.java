package verticle;

import entity.Todo;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import service.JDBCTodoService;
import service.TodoService;

import java.util.Optional;

public class ApplicationVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int PORT = 8000;

    private static final int STATE_CODE_OK = 200;
    private static final int STATE_CODE_CREATE = 201;
    private static final int STATE_CODE_BAD_REQUEST = 400;

    private static final String API_GET = "/person/:id";
    private static final String API_CREATE = "/person";
    private static final String API_DELETE = "/person:id";
    private static final String API_UDPATE = "/person";

    private TodoService service;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        service = new JDBCTodoService(vertx);
        service.initDataSource().setHandler(res -> {
            if(res.failed()) {
                logger.info(res.cause());
            } else {
                logger.info("mysql connection success");
            }
        });

        //CROS support
        //TODO

        Router router = Router.router(vertx);

        router.get(API_GET).handler(this::get);
        router.put(API_CREATE).handler(this::create);
        router.delete(API_DELETE).handler(this::delete);
        router.patch(API_UDPATE).handler(this::update);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(PORT);


    }

    public void get(RoutingContext context) {
        String id = context.request().getParam("id");
        if(id == null) {
            context.response().setStatusCode(STATE_CODE_BAD_REQUEST).end();
        } else {
            Future<Optional<Todo>> future = service.getTodo(id);
            future.setHandler(res -> {
                if (res.failed()) {
                    context.response().setStatusCode(STATE_CODE_OK).end("Todo Not Found");
                }
                if (res.result().isPresent()) {
                    String encoded = Json.encodePrettily(res.result().get());
                    context.response()
                            .setStatusCode(STATE_CODE_OK)
                            .putHeader("content-type", "application/json")
                            .end(encoded);
                }
            });
        }
    }

    public void create(RoutingContext context) {

    }

    public void delete(RoutingContext context) {

    }

    public void update(RoutingContext context) {

    }
}
