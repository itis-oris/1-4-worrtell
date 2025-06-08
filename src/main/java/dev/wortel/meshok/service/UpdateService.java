package dev.wortel.meshok.service;

//@Slf4j
//@Service
//public class UpdateService {
//    public void update() {
//        String token = "f3cfb2cb5903df0f48231453f1877108";
//        MeshokAPI api = new MeshokAPI(token);
//
//        String jsonResponse = api.getItemList();
//        List<Lot> lots = new JsonParser().toLotList(jsonResponse);
//        System.out.println(lots);
//
//        List<Long> lotIdList = lots.stream().map(Lot::getId).toList();
//
//        lots.subList(0,10).forEach(lot -> log.info(api.getItemInfo(lot.getId().toString())));
//
//    }
//}
