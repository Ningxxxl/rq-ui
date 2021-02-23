package cn.ningxy.rqui.sys.exception;

public class EmptyResultException extends RuntimeException {

    private String message = "无结果。";

    public EmptyResultException() {
    }

    public EmptyResultException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
