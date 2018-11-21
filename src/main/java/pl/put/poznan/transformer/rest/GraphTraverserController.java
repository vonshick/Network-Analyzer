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


