package com.example.demo.exception.exception;

import com.example.demo.exception.RegleMetierException;

// pour les erreur 404
public class RessourceIntrouvableException extends RegleMetierException {
    public RessourceIntrouvableException(String message) {
        super(message);
    }
}