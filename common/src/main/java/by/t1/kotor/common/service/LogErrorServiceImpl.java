package by.t1.kotor.common.service;

import by.t1.kotor.common.model.ErrorLogEntity;
import by.t1.kotor.common.repository.ErrorLogRepository;
import by.t1.kotor.crosscuttingstarter.dto.LogErrorMessage;
import by.t1.kotor.crosscuttingstarter.service.LogErrorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogErrorServiceImpl implements LogErrorService {

    private final ErrorLogRepository errorLogRepository;
    private final ObjectMapper objectMapper;

    public void saveError(LogErrorMessage message) {
        ErrorLogEntity entity = new ErrorLogEntity();
        entity.setTimestamp(message.timestamp());
        entity.setMethodSignature(message.methodSignature());
        entity.setExceptionMessage(message.exceptionMessage());
        entity.setStackTrace(message.stackTrace());

        try {
            entity.setMethodArgs(objectMapper.writeValueAsString(message.methodArgs()));
        } catch (Exception e) {
            entity.setMethodArgs("Could not serialize args");
        }

        errorLogRepository.save(entity);
    }
}
