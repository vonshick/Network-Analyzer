package pl.put.poznan.transformer.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This is just an example to show that the logic should be outside the REST service.
 */
public class TextTransformer {

    private final String[] transforms;

    public TextTransformer(String[] transforms){
        this.transforms = transforms;
    }

    public String transform(String text){
        ObjectMapper mapper = new ObjectMapper();
        Network network=null;
        try {
            network= mapper.readValue(text, Network.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (network != null) {
            System.out.println(network.getNodes().get(0).getName());
        }
        else{
            System.out.println("netowrk is null");
            return "BLAD";
        }

        //zakladam, że memy Dominika, będą zwracać ArrayList<int>
        ArrayList<Integer> lista = new ArrayList<>();
        for (int i=0;i<11;i++){
            lista.add(i);
        }
        try {
            String jsonInString = mapper.writeValueAsString(lista);
            return "[{\"text\":"+lista+"}]";
        } catch (JsonProcessingException e) {
            return "[{\"text\":"+"[1,2,3,4]"+"}]";
        }
    }
}
