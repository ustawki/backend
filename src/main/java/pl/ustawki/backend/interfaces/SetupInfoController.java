package pl.ustawki.backend.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ustawki.backend.domain.dto.SetupInfo;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class SetupInfoController {

    @Autowired
    ResourceLoader resourceLoader;

    @GetMapping("/setups")
    public List<SetupInfo> getAll() throws IOException {
      Resource resource = resourceLoader.getResource(
                "classpath:setups.json");
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(resource.getInputStream(), List.class);
    }
}
