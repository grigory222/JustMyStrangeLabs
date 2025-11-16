package org.itmo.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itmo.model.ImportOperation;
import org.itmo.repository.ImportOperationRepository;
import org.itmo.service.MinioService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class TwoPhaseCommitCoordinator {

    private final MinioService minioService;
    private final ImportOperationRepository importOperationRepository;
    
    private volatile boolean simulateDatabaseFailure = false;
    private volatile boolean simulateBusinessLogicError = false;
    
    public void setSimulateDatabaseFailure(boolean simulate) {
        this.simulateDatabaseFailure = simulate;
        log.warn("Database failure simulation: {}", simulate ? "ON" : "OFF");
    }
    
    public boolean isSimulateDatabaseFailure() {
        return simulateDatabaseFailure;
    }
    
    public void setSimulateBusinessLogicError(boolean simulate) {
        this.simulateBusinessLogicError = simulate;
        log.warn("Business logic error simulation: {}", simulate ? "ON" : "OFF");
    }
    
    public boolean isSimulateBusinessLogicError() {
        return simulateBusinessLogicError;
    }

    @Transactional
    public ImportOperation executeImportWithTwoPhaseCommit(
            byte[] fileBytes,
            String fileName,
            String contentType,
            Supplier<ImportOperation> databaseOperation
    ) throws Exception {
        
        String fileKey = null;
        ImportOperation operation = null;
        
        try {
            log.info("2PC Phase 1");

            // upload to minIO    
            fileKey = minioService.uploadBytes(fileName, fileBytes, contentType);
            
            if (simulateBusinessLogicError) {
                throw new RuntimeException("Simulated business logic error");
            }
            
            if (simulateDatabaseFailure) {
                throw new RuntimeException("Simulated database failure");
            }
            
            operation = databaseOperation.get();
            
            // save to DB
            operation.setFileKey(fileKey);
            operation.setFileName(fileName);
            operation.setFileSize((long) fileBytes.length);
            operation = importOperationRepository.save(operation);
            
            log.info("2PC Phase 2: SUCCESS");
            return operation; // COMMIT
            
        } catch (Exception e) {
            log.error("2PC Phase 2: FAILURE", e.getMessage()); // rollback
            
            if (fileKey != null) {
                try {                    
                    minioService.deleteFile(fileKey);
                    log.info("File deleted from MinIO");
                } catch (Exception deleteEx) {
                    log.error("2PC CRITICAL: cannot delete file in MinIO: {}", fileKey, deleteEx);
                }
            }
            
            if (operation != null && operation.getId() != null) {
                try {
                    saveErrorState(operation, e.getMessage());
                } catch (Exception saveEx) {
                    log.error("Failed to save error state", saveEx);
                }
            }
            
            throw e;
        }
    }

    @Transactional
    private void saveErrorState(ImportOperation operation, String errorMessage) {
        operation.setStatus(ImportOperation.Status.FAILED);
        operation.setErrorMessage(errorMessage);
        operation.setFileKey(null);
        operation.setFileName(null);
        operation.setFileSize(null);
        importOperationRepository.save(operation);
    }
}