package by.t1.kotor.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "error_log")
public class ErrorLogEntity extends BaseEntity {

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    @Column(name = "method_signature")
    private String methodSignature;
    @Column(name = "exception_message")
    private String exceptionMessage;
    @Column(name = "stack_trace")
    private String stackTrace;
    @Column(name = "method_args")
    private String methodArgs;
}
