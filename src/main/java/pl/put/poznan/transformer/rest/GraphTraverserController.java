package pl.put.poznan.transformer.rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.transformer.logic.GraphTraverser;

import java.util.Arrays;


@RestController
@RequestMapping("/{text}")
public class GraphTraverserController {

    private static final Logger logger = LoggerFactory.getLogger(GraphTraverserController.class);
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public String get(@PathVariable String text,
                              @RequestParam(value="requestedAlgorithm", defaultValue="naive") String[] transforms) {
        if(text.equals("Jmetertest")){
            text="{\"nodes\":[{\"id\":0,\"name\":\"Wezel 0\",\"type\":\"entry\",\"outgoing\":[{\"to\":3,\"from\":0,\"value\":9}],\"incoming\":[{\"to\":0,\"from\":5,\"value\":3}]},{\"id\":1,\"name\":\"Wezel 1\",\"type\":\"regular\",\"outgoing\":[{\"to\":2,\"from\":1,\"value\":4},{\"to\":4,\"from\":1,\"value\":6},{\"to\":5,\"from\":1,\"value\":6}],\"incoming\":[{\"to\":1,\"from\":2,\"value\":6}]},{\"id\":2,\"name\":\"Wezel 2\",\"type\":\"regular\",\"outgoing\":[{\"to\":1,\"from\":2,\"value\":6},{\"to\":5,\"from\":2,\"value\":3}],\"incoming\":[{\"to\":2,\"from\":1,\"value\":4},{\"to\":2,\"from\":3,\"value\":6}]},{\"id\":3,\"name\":\"Wezel 3\",\"type\":\"regular\",\"outgoing\":[{\"to\":2,\"from\":3,\"value\":6}],\"incoming\":[{\"to\":3,\"from\":0,\"value\":9},{\"to\":3,\"from\":5,\"value\":9}]},{\"id\":4,\"name\":\"Wezel 4\",\"type\":\"regular\",\"outgoing\":[{\"to\":5,\"from\":4,\"value\":4}],\"incoming\":[{\"to\":4,\"from\":1,\"value\":6}]},{\"id\":5,\"name\":\"Wezel 5\",\"type\":\"exit\",\"outgoing\":[{\"to\":0,\"from\":5,\"value\":3},{\"to\":3,\"from\":5,\"value\":9}],\"incoming\":[{\"to\":5,\"from\":1,\"value\":6},{\"to\":5,\"from\":2,\"value\":3},{\"to\":5,\"from\":4,\"value\":4}]}],\"connections\":[{\"to\":3,\"from\":0,\"value\":9},{\"to\":2,\"from\":1,\"value\":4},{\"to\":4,\"from\":1,\"value\":6},{\"to\":5,\"from\":1,\"value\":6},{\"to\":1,\"from\":2,\"value\":6},{\"to\":5,\"from\":2,\"value\":3},{\"to\":2,\"from\":3,\"value\":6},{\"to\":5,\"from\":4,\"value\":4},{\"to\":0,\"from\":5,\"value\":3},{\"to\":3,\"from\":5,\"value\":9}]}";
        }
        // log the parameters
        logger.debug(text);
        logger.debug(Arrays.toString(transforms));


        // do the transformation, you should run your logic here, below just a silly example
        GraphTraverser transformer = new GraphTraverser(transforms,logger);
        return transformer.transform(text);
    }
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public String post(@PathVariable String text,
                      @RequestBody String[] transforms) {

        // log the parameters
        logger.debug(text);
        logger.debug(Arrays.toString(transforms));

        // do the transformation, you should run your logic here, below just a silly example
        GraphTraverser transformer = new GraphTraverser(transforms,logger);
        return transformer.transform(text);
    }
}


