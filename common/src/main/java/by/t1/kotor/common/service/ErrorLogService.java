package by.t1.kotor.common.service;

import by.t1.kotor.common.model.dto.LogErrorMessage;

public interface ErrorLogService {
    void saveError(LogErrorMessage message);
}
