package cl.orioneta.bff.infrastructure.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "neta-market-service", url = "${orioneta.services.neta-market}")
public interface NetaMarketClient {

    @GetMapping("/api/neta-market/templates/featured")
    List<Map<String, Object>> findFeaturedTemplates();
}
