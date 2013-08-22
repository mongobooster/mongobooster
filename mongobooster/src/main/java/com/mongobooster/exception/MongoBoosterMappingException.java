package com.mongobooster.exception;

public class MongoBoosterMappingException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -8055769979792348572L;

    private Throwable t;

    public MongoBoosterMappingException() {

    }

    public MongoBoosterMappingException(Throwable t) {
        this.t = t;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Throwable#getStackTrace()
     */
    @Override
    public StackTraceElement[] getStackTrace() {
        // TODO merge stacktraces from super with t.getStackTrace
        // StackTraceElement[] stackTraceElement = super.getStackTrace();
        if (t != null) {
            return t.getStackTrace();
        }
        return super.getStackTrace();
    }

}
