package org.itmo.controller;

import lombok.RequiredArgsConstructor;
import org.itmo.dto.ImportResponse;
import org.itmo.model.ImportOperation;
import org.itmo.service.ImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {
    
    private final ImportService importService;
    
    @PostMapping
    public ResponseEntity<ImportResponse> importRoutes(@RequestParam("file") MultipartFile file) {
        try {
            ImportOperation operation = importService.importRoutes(file);
            
            if (operation.getStatus() == ImportOperation.Status.SUCCESS) {
                return ResponseEntity.ok(new ImportResponse(
                        operation.getId(),
                        operation.getAddedCount(),
                        "Import successful"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportResponse(
                        operation.getId(),
                        null,
                        operation.getErrorMessage()
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ImportResponse(
                    null,
                    null,
                    "Import failed: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/history")
    public ResponseEntity<List<ImportOperation>> getHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
        
        List<ImportOperation> history = importService.getHistory(username, isAdmin);
        return ResponseEntity.ok(history);
    }
}
