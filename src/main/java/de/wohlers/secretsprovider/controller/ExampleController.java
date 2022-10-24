package de.wohlers.secretsprovider.controller;

import de.wohlers.secretsprovider.secretsprovider.SecretsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class ExampleController {
    private final SecretsProvider secretsProvider;

    @GetMapping("/secrets")
    public ResponseEntity<String> getSecret(@RequestParam String name) {
        String result = secretsProvider.get(name);
        return ResponseEntity.ok(result);
    }

}
