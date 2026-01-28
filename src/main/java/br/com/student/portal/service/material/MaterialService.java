package br.com.student.portal.service.material;

import br.com.student.portal.dto.request.MaterialRequest;
import br.com.student.portal.dto.response.MaterialResponse;
import br.com.student.portal.entity.Material;
import br.com.student.portal.entity.User;
import br.com.student.portal.entity.enums.MaterialCategory;
import br.com.student.portal.exception.ObjectNotFoundException;
import br.com.student.portal.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final Path rootLocation = Paths.get("uploads");

    public MaterialResponse getMaterialById(UUID id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Material não encontrado"));
        return mapToResponse(material);
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getAllMaterials() {
        return materialRepository.findAllOrderByNewest()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<MaterialResponse> getAllMaterials(Pageable pageable) {
        return materialRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public MaterialResponse createMaterial(MaterialRequest request, MultipartFile file, User uploadedBy) throws IOException {
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path destinationFile = rootLocation.resolve(filename);
        Files.copy(file.getInputStream(), destinationFile);

        Material material = Material.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(MaterialCategory.valueOf(request.getCategory().toUpperCase()))
                .filename(filename)
                .uploadedBy(uploadedBy)
                .uploadDate(LocalDateTime.now())
                .downloads(0L)
                .build();

        Material savedMaterial = materialRepository.save(material);
        return mapToResponse(savedMaterial);
    }

    public MaterialResponse updateMaterial(UUID id, MaterialRequest request, User uploadedBy) {
        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Material não encontrado"));

        if (!existingMaterial.getUploadedBy().getId().equals(uploadedBy.getId())) {
            throw new RuntimeException("Apenas o uploader pode editar o material");
        }

        existingMaterial.setName(request.getName());
        existingMaterial.setDescription(request.getDescription());
        if (request.getCategory() != null) {
            existingMaterial.setCategory(MaterialCategory.valueOf(request.getCategory().toUpperCase()));
        }

        Material updatedMaterial = materialRepository.save(existingMaterial);
        return mapToResponse(updatedMaterial);
    }

    public void deleteMaterial(UUID id, User requester) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Material não encontrado"));

        boolean isUploader = material.getUploadedBy().getId().equals(requester.getId());
        boolean isAdmin = requester.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));

        if (!isUploader && !isAdmin) {
            throw new RuntimeException("Não autorizado a deletar este material");
        }

        try {
            Path filePath = rootLocation.resolve(material.getFilename());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
        }

        materialRepository.delete(material);
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getMaterialsByCategory(String category) {
        try {
            MaterialCategory categoryEnum =
                    MaterialCategory.valueOf(category.toUpperCase());

            return materialRepository.findByCategory(categoryEnum)
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new ObjectNotFoundException("Categoria não encontrada: " + category);
        }
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> searchMaterials(String term) {
        return materialRepository.searchByName(term)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getMaterialsByUploader(UUID userId) {
        return materialRepository.findByUploadedById(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getMostDownloadedMaterials(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return materialRepository.findAllByOrderByDownloadsDesc(pageable)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void incrementDownloads(UUID id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Material não encontrado"));

        material.setDownloads(material.getDownloads() + 1);
        materialRepository.save(material);
    }

    public byte[] downloadMaterial(UUID id) throws IOException {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Material não encontrado"));

        Path filePath = rootLocation.resolve(material.getFilename());
        if (!Files.exists(filePath)) {
            throw new ObjectNotFoundException("Arquivo físico não encontrado");
        }

        incrementDownloads(id);

        return Files.readAllBytes(filePath);
    }

    private MaterialResponse mapToResponse(Material entity) {
        return MaterialResponse.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .description(entity.getDescription())
                .filename(entity.getFilename())
                .category(entity.getCategory().name())
                .uploaderId(entity.getUploadedBy().getId().toString())
                .uploaderName(entity.getUploadedBy().getName())
                .downloads(entity.getDownloads())
                .uploadDate(entity.getUploadDate() != null ? entity.getUploadDate().toString() : "")
                .build();
    }
}