package com.rickmorty.api.exception;

/** Small, related exceptions grouped in one file to keep the project lean. */
public final class ApiExceptions {

    private ApiExceptions() {
    }

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }

        public static ResourceNotFoundException of(String resource, String id) {
            return new ResourceNotFoundException(resource + " not found with id: " + id);
        }
    }

    public static class DuplicateResourceException extends RuntimeException {
        public DuplicateResourceException(String message) {
            super(message);
        }
    }
}
