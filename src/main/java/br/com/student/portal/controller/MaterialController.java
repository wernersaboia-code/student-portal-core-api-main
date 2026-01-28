package br.com.student.portal.controller;

import br.com.student.portal.dto.request.MaterialRequest;
import br.com.student.portal.dto.response.MaterialResponse;
import br.com.student.portal.entity.User;
import br.com.student.portal.service.material.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    public ResponseEntity<MaterialResponse> uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            Authentication authentication) throws IOException {

        var request = MaterialRequest.builder()
                .name(name)
                .description(description)
                .category(category)
                .build();

        var uploadedBy = (User) authentication.getPrincipal();
        var response = materialService.createMaterial(request, file, uploadedBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponse>> getAllMaterials() {
        var materials = materialService.getAllMaterials();
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponse> getMaterial(@PathVariable UUID id) {
        var material = materialService.getMaterialById(id);
        return ResponseEntity.ok(material);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MaterialResponse>> getMaterialsByCategory(@PathVariable String category) {
        var materials = materialService.getMaterialsByCategory(category);
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MaterialResponse>> searchMaterials(@RequestParam String term) {
        var materials = materialService.searchMaterials(term);
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/uploader/{userId}")
    public ResponseEntity<List<MaterialResponse>> getMaterialsByUploader(@PathVariable UUID userId) {
        var materials = materialService.getMaterialsByUploader(userId);
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/most-downloaded")
    public ResponseEntity<List<MaterialResponse>> getMostDownloadedMaterials(
            @RequestParam(defaultValue = "10") int limit) {
        var materials = materialService.getMostDownloadedMaterials(limit);
        return ResponseEntity.ok(materials);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponse> updateMaterial(
            @PathVariable UUID id,
            @RequestBody MaterialRequest request,
            Authentication authentication) {

        var uploadedBy = (User) authentication.getPrincipal();
        var response = materialService.updateMaterial(id, request, uploadedBy);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(
            @PathVariable UUID id,
            Authentication authentication) {

        var uploadedBy = (User) authentication.getPrincipal();
        materialService.deleteMaterial(id, uploadedBy);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/download")
    public ResponseEntity<Void> downloadMaterial(@PathVariable UUID id) {
        materialService.incrementDownloads(id);
        return ResponseEntity.ok().build();
    }
}