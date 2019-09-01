package com.basaki.k8s.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {@code ErrorInfo} represents an error response object which is exposed to
 * the external client. It is human readable and informative without
 * exposing service implementation details, e.g.,
 * exception type, stack trace, etc.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfo {

    private String path;

    private int code;

    private String type;

    private String message;
}
