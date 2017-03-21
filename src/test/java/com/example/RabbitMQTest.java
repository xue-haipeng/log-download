package com.example;

/**
 * Created by Xue on 02/18/17.
 */
    /*
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQTest {

    @Autowired
    private Sender sender;
    @Test
    public void hello() throws Exception {
        sender.send();
    }*/

/*
    @Autowired
    RestTemplate restTemplate;

    private final String URL = "http://11.11.44.203:9200/logstash-date/_search?q=level:'Error'&q=message:'plate'";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Test
    public void testRestTemplate() {
        List<String> domains = Arrays.asList("YQT", "XS", "LYHG", "TRQ");
        Map<String, Long> count = new HashMap<>();
        domains.forEach(domain -> {
            ESQueryResponse response = restTemplate.getForObject(URL.replace("date", LocalDate.now().format(formatter)).replace("plate", domain), ESQueryResponse.class);
            count.put(domain, response.getHits().getTotal());
        });


        System.out.println("*******************");
        count.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("*******************");
    }



}

*/