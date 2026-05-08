package org.hubay.byteandbuy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Riesi lepsie citatelne vynimky.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Pouziva sa napriklad pri neplatnej ID, nespravny stav hry,...
     * Vracia HTTP request BAD_REQUEST.
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ProblemDetail handleBadRequest(RuntimeException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Neplatna poziadavka");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    /**
     * Pouzije sa pri subeznej aktualizacii hry.
     * Ak sa snazime ulozit hru s inou verziou ako v verzia ktora v DB ulozena je.
     * Vracia HTTP request CONFLICT.
     */
    @ExceptionHandler(ConcurrentGameUpdateException.class)
    public ProblemDetail handleConcurrentGameUpdate(ConcurrentGameUpdateException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Konflikt pri ukladani hry");
        problem.setDetail(ex.getMessage());
        return problem;
    }
}
