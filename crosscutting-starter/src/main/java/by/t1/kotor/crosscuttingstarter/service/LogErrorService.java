package by.t1.kotor.crosscuttingstarter.service;


import by.t1.kotor.crosscuttingstarter.dto.LogErrorMessage;

public interface LogErrorService {
    void saveError(LogErrorMessage message);
}
